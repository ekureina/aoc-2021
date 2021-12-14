#[aoc_generator(day6)]
fn blowfish_states(input: &str) -> Vec<u64> {
    let mut states = Vec::with_capacity(9);
    (0..9).for_each(|_index| {
        states.push(0);
    });

    String::from(input)
        .split(",")
        .for_each(|state| {
            if let Ok(parsed) = state.parse::<usize>() {
                states[parsed] += 1;
            } else {
                panic!()
            }
        });

    states
}

fn simulate(input: &[u64], iteration_count: usize) -> u64 {
    let mut states = [0; 9];
    for item in input.iter().enumerate() {
        let (i, state) = item;
        states[i] = *state;
    }
    let mut birthed = 0;
    (0..iteration_count)
        .for_each(|_iteration| {
            let mut temp_states = [0; 9];
            for item in states.iter().enumerate() {
                let (i, state) = item;
                if i == 0 {
                    birthed = *state;
                } else {
                    temp_states[i - 1] = *state;
                }
            }

            for item in temp_states.iter().enumerate() {
                let (i, state) = item;
                states[i] = *state;
            }
            states[6] += birthed;
            states[8] += birthed;

        });
    states.iter().sum()
}

#[aoc(day6, part1)]
fn solution1(input: &[u64]) -> u64 {
    simulate(input, 80)
}

#[aoc(day6, part2)]
fn solution2(input: &[u64]) -> u64 {
    simulate(input, 256)
}
