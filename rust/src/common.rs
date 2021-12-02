use std::env::args;
use std::fs::File;
use std::io::{self, BufRead};
use std::str::FromStr;

pub fn get_test_lines() -> io::Result<io::Lines<io::BufReader<File>>> {
    let mut day_name = String::from(args().next_back().unwrap().split("/").last().unwrap());
    day_name.get_mut(0..1).unwrap().make_ascii_uppercase();
    println!("{}", "../data/".to_owned() + &day_name + "_test.txt");
    let file = File::open("../data/".to_owned() + &day_name + "_test.txt")?;
    Ok(io::BufReader::new(file).lines())
}

pub fn get_lines() -> io::Result<io::Lines<io::BufReader<File>>> {
    let mut day_name = String::from(args().next_back().unwrap().split("/").last().unwrap());
    day_name.get_mut(0..1).unwrap().make_ascii_uppercase();
    let file = File::open("../data/".to_owned() + &day_name + ".txt")?;
    Ok(io::BufReader::new(file).lines())
}

pub fn convert_lines<T: FromStr>(lines: io::Lines<io::BufReader<File>>) -> Vec<T> {
    lines
        .map(|line| {
            if let Ok(parsed) = line.unwrap().parse::<T>() {
                parsed
            } else {
                panic!()
            }
        })
        .collect::<Vec<T>>()
}
