import Day16Helper.decodePacket
import Day16Helper.versionSum
import java.lang.Integer.parseInt
import java.lang.Long.parseLong

object Day16Helper {
    fun decodePacket(data: String): Pair<Packet, String> {
        val header = decodeHeader(data)
        if (header.id == 4) {
            val (literal, remaining) = decodeLiteral(data.drop(6))
            return LiteralPacket(header, literal) to remaining
        }
        val lenId = parseInt(data[6].toString(), 2)
        return if (lenId == 0) {
            val subPacketLen = parseInt(data.substring(7..21), 2)
            val subPackets = decodePackets(data.drop(22).substring(0, subPacketLen))
            createOperatorPacket(header, subPackets) to data.drop(22 + subPacketLen)
        } else {
            val subPacketNum = parseInt(data.substring(7..17), 2)
            val (subPackets, remaining) = decodePackets(data.drop(18), subPacketNum)
            createOperatorPacket(header, subPackets) to remaining
        }
    }

    private fun decodePackets(data: String): List<Packet> {
        return buildList {
            var (packet, remaining) = decodePacket(data)
            add(packet)
            while (remaining != "") {
                val output = decodePacket(remaining)
                remaining = output.second
                add(output.first)
            }
        }
    }

    private fun decodePackets(data: String, maxPackets: Int): Pair<List<Packet>, String> {
        var remaining = data
        return buildList {
            (0 until maxPackets).forEach { _ ->
                val output = decodePacket(remaining)
                remaining = output.second
                add(output.first)
            }
        } to remaining
    }

    private fun createOperatorPacket(header: PacketHeader, subPackets: List<Packet>): OperatorPacket {
        return when (header.id) {
            0 -> SumPacket(header, subPackets)
            1 -> ProductPacket(header, subPackets)
            2 -> MinimumPacket(header, subPackets)
            3 -> MaximumPacket(header, subPackets)
            5 -> GreaterPacket(header, subPackets)
            6 -> LessPacket(header, subPackets)
            7 -> EqualPacket(header, subPackets)
            else -> throw IllegalStateException("Bad Header Id ${header.id}!")
        }
    }

    data class PacketHeader(val version: Int, val id: Int)
    abstract class Packet(val header: PacketHeader, val subPackets: List<Packet>) {
        abstract fun value(): Long
    }
    class LiteralPacket(header: PacketHeader, val value: Long) : Packet(header, listOf()) {
        override fun value(): Long {
            return value
        }
    }
    abstract class OperatorPacket(header: PacketHeader, subPackets: List<Packet>) : Packet(header, subPackets)
    class SumPacket(header: PacketHeader, subPackets: List<Packet>) : OperatorPacket(header, subPackets) {
        override fun value(): Long {
            return subPackets.map(Packet::value).sum()
        }
    }
    class ProductPacket(header: PacketHeader, subPackets: List<Packet>) : OperatorPacket(header, subPackets) {
        override fun value(): Long {
            return subPackets.map(Packet::value).fold(1) { intermediate, value ->
                intermediate * value
            }
        }
    }
    class MinimumPacket(header: PacketHeader, subPackets: List<Packet>) : OperatorPacket(header, subPackets) {
        override fun value(): Long {
            return subPackets.minOf(Packet::value)
        }
    }
    class MaximumPacket(header: PacketHeader, subPackets: List<Packet>) : OperatorPacket(header, subPackets) {
        override fun value(): Long {
            return subPackets.maxOf(Packet::value)
        }
    }
    class GreaterPacket(header: PacketHeader, subPackets: List<Packet>) : OperatorPacket(header, subPackets) {
        override fun value(): Long {
            check(subPackets.size == 2) { "Sub Packet size must be 2, was ${subPackets.size}" }
            return if (subPackets[0].value() > subPackets[1].value()) {
                1
            } else {
                0
            }
        }
    }
    class LessPacket(header: PacketHeader, subPackets: List<Packet>) : OperatorPacket(header, subPackets) {
        override fun value(): Long {
            check(subPackets.size == 2) { "Sub Packet size must be 2, was ${subPackets.size}" }
            return if (subPackets[0].value() < subPackets[1].value()) {
                1
            } else {
                0
            }
        }
    }
    class EqualPacket(header: PacketHeader, subPackets: List<Packet>) : OperatorPacket(header, subPackets) {
        override fun value(): Long {
            check(subPackets.size == 2) { "Sub Packet size must be 2, was ${subPackets.size}" }
            return if (subPackets[0].value() == subPackets[1].value()) {
                1
            } else {
                0
            }
        }
    }

    private fun decodeHeader(packetBitString: String): PacketHeader {
        val version = parseInt(packetBitString.substring(0..2), 2)
        val id = parseInt(packetBitString.substring(3..5), 2)
        return PacketHeader(version, id)
    }

    private fun decodeLiteral(literalBits: String): Pair<Long, String> {
        val data = buildList {
            var terminate = false
            val bits = literalBits.chunked(5).iterator()
            do {
                val chunk = bits.next()
                terminate = chunk[0] == '0'
                add(chunk.substring(1..4))
            } while (!terminate)
        }
        val remaining = literalBits.drop(data.size * 5)
        return parseLong(data.joinToString(""), 2) to remaining
    }


    fun versionSum(packet: Packet): Int {
        return if (packet.subPackets.isEmpty()) {
            packet.header.version
        } else {
            packet.header.version + packet.subPackets.sumOf(::versionSum)
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val bitString = input[0].map { char ->
            parseInt(char.toString(), 16).toString(2).padStart(4, '0')
        }.joinToString("")
        return versionSum(decodePacket(bitString).first)
    }

    fun part2(input: List<String>): Long {
        val bitString = input[0].map { char ->
            parseInt(char.toString(), 16).toString(2).padStart(4, '0')
        }.joinToString("")

        return decodePacket(bitString).first.value()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 16) { "${part1(testInput)}" }
    check(part2(testInput) == 15L) { "${part2(testInput)}" }

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
