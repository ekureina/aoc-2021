mod common;
use std::fs::File;
use std::io::{self, BufRead};

struct Position {
    position: u32,
    depth: u32,
}

impl Position {
    fn new(position: u32, depth: u32) -> Self {
        Position {
            position: position,
            depth: depth,
        }
    }
}

struct PositionWithAim {
    position: u32,
    depth: u32,
    aim: u32,
}

impl PositionWithAim {
    fn new(position: u32, depth: u32, aim: u32) -> Self {
        PositionWithAim {
            position: position,
            depth: depth,
            aim: aim,
        }
    }
}

enum Command {
    Forward(u32),
    Up(u32),
    Down(u32),
}

fn solution1(data: &Vec<Command>) -> u32 {
    let final_position = data
        .clone()
        .iter()
        .fold(Position::new(0, 0), |position, command| match command {
            Command::Forward(magnitude) => {
                Position::new(position.position + magnitude, position.depth)
            }
            Command::Up(magnitude) => Position::new(position.position, position.depth - magnitude),
            Command::Down(magnitude) => {
                Position::new(position.position, position.depth + magnitude)
            }
        });
    final_position.position * final_position.depth
}

fn solution2(data: &Vec<Command>) -> u32 {
    let final_position = data.clone().iter().fold(
        PositionWithAim::new(0, 0, 0),
        |position, command| match command {
            Command::Forward(magnitude) => PositionWithAim::new(
                position.position + magnitude,
                position.depth + (position.aim * magnitude),
                position.aim,
            ),
            Command::Up(magnitude) => {
                PositionWithAim::new(position.position, position.depth, position.aim - magnitude)
            }
            Command::Down(magnitude) => {
                PositionWithAim::new(position.position, position.depth, position.aim + magnitude)
            }
        },
    );
    final_position.position * final_position.depth
}

fn convert_lines(lines: io::Lines<io::BufReader<File>>) -> Vec<Command> {
    lines
        .map(|line| {
            if let Some((cmd_type, magnitude)) = line.unwrap().split_once(" ") {
                match cmd_type {
                    "forward" => Command::Forward(magnitude.parse().unwrap()),
                    "up" => Command::Up(magnitude.parse().unwrap()),
                    "down" => Command::Down(magnitude.parse().unwrap()),
                    _ => panic!(),
                }
            } else {
                panic!()
            }
        })
        .collect()
}

fn main() {
    if let Ok(lines) = common::get_test_lines() {
        let input = convert_lines(lines);
        let result = solution1(&input);
        println!("{}", result);
        let result2 = solution2(&input);
        println!("{}", result2);
    } else {
        println!("error reading file!");
    }
    if let Ok(lines) = common::get_lines() {
        let input = convert_lines(lines);
        let result = solution1(&input);
        println!("{}", result);
        let result2 = solution2(&input);
        println!("{}", result2);
    } else {
        println!("error reading file!");
    }
}
