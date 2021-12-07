#[aoc_generator(day1)]
fn generator(input: &str) -> Vec<u32> {
    String::from(input).split("\n")
        .map(|line| {
            if let Ok(parsed) = line.parse() {
                parsed
            } else {
                panic!()
            }
        }).collect()
}

#[aoc(day1, part1)]
fn solution1(data: &[u32]) -> u32 {
    data.windows(2).fold(0, |current, pair| {
        if pair[0] < pair[1] {
            current + 1
        } else {
            current
        }
    })
}

#[aoc(day1, part2)]
fn solution2(data: &[u32]) -> u32 {
    data.windows(3)
        .map(|window| window.iter().fold(0, |sum, i| sum + i))
        .collect::<Vec<u32>>()
        .windows(2)
        .fold(0, |current, sum_pair| {
            if sum_pair[0] < sum_pair[1] {
                current + 1
            } else {
                current
            }
        })
}

