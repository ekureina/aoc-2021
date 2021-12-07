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

#[aoc_generator(day2)]
fn convert_lines(input: &str) -> Vec<Command> {
    String::from(input).split("\n")
        .map(|line| {
            if let Some((cmd_type, magnitude)) = line.split_once(" ") {
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

#[aoc(day2, part1)]
fn solution1(data: &[Command]) -> u32 {
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

#[aoc(day2, part2)]
fn solution2(data: &[Command]) -> u32 {
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

