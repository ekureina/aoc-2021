#[aoc_generator(day3)]
fn parse_binary(input: &str) -> Vec<u16> {
    String::from(input)
        .split("\n")
        .map(|line| u16::from_str_radix(line, 2).unwrap())
        .collect()
}

#[aoc(day3, part1)]
fn solution1(data: &[u16]) -> u32 {
    // Solution bitstrings are 12
    let mut one_count = [0; 12];
    for value in data.iter() {
        for bit in 0..12 {
            one_count[bit] += usize::from(value & (1 << bit)) >> bit;
        }
    }
    let mut gamma = 0;
    let mut epsilon = 0;
    for bit_count in one_count.iter().rev() {
        gamma <<= 1;
        epsilon <<= 1;
        if bit_count > &(data.len() / 2) {
            gamma += 1;
        } else {
            epsilon += 1;
        }
    }
    gamma * epsilon
}

#[aoc(day3, part2)]
fn solution2(data: &[u16]) -> u32 {
    let mut oxy_data = Vec::from(data.clone());
    for round in (0..12).rev() {
        if oxy_data.len() > 1 {
            let one_count = oxy_data.iter().fold(0, |count, data| {
                if data & (1 << round) != 0 {
                    count + 1
                } else {
                    count
                }
            });
            let filter_len = oxy_data.len();
            oxy_data = oxy_data
                .iter()
                .filter(|value| {
                    if one_count >= (filter_len - one_count) {
                        *value & &(1 << round) != 0
                    } else {
                        *value & &(1 << round) == 0
                    }
                })
                .map(|value| *value)
                .collect::<Vec<u16>>();
        }
    }
    let oxy: u32 = oxy_data[0].into();
    let mut co2_data = Vec::from(data.clone());
    for round in (0..12).rev() {
        if co2_data.len() > 1 {
            let one_count = co2_data.iter().fold(0, |count, data| {
                if data & (1 << round) != 0 {
                    count + 1
                } else {
                    count
                }
            });
            let filter_len = co2_data.len();
            co2_data = co2_data
                .iter()
                .filter(|value| {
                    if one_count >= (filter_len - one_count) {
                        *value & &(1 << round) == 0
                    } else {
                        *value & &(1 << round) != 0
                    }
                })
                .map(|value| *value)
                .collect::<Vec<u16>>();
        }
    }
    let co2: u32 = co2_data[0].into();
    oxy * co2
}
