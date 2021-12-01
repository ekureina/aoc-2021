mod common;

fn solution1(data: &Vec<u32>) -> u32 {
    data.windows(2).fold(0, |current, pair| {
        if pair[0] < pair[1] {
            current + 1
        } else {
            current
        }
    })
}

fn solution2(data: &Vec<u32>) -> u32 {
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

fn main() {
    if let Ok(lines) = common::get_test_lines() {
        let ints = common::convert_lines(lines);
        let result = solution1(&ints);
        println!("{}", result);
        let result2 = solution2(&ints);
        println!("{}", result2);
    } else {
        println!("error reading file!");
    }
    if let Ok(lines) = common::get_lines() {
        let ints = common::convert_lines(lines);
        let result = solution1(&ints);
        println!("{}", result);
        let result2 = solution2(&ints);
        println!("{}", result2);
    } else {
        println!("error reading file!");
    }
}
