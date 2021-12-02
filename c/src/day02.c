#include <stdio.h>
#include "lib.h"

typedef struct Position {
    int position;
    int depth;
    int aim;
} Position;

typedef struct Command {
    char type;
    int magnitude;
} Command;

Command parse_command(char* str) {
    Command command;
    if (str != NULL) {
        command.type = *str;
        while (*str++ != ' ');
        command.magnitude = atoi(str);
    }
    return command;
}

int solution1(char** array) {
    Position position;
    position.position = 0;
    position.depth = 0;
    position.aim = 0; // Unessary, but for clean code
    while (*array != NULL) {
        Command command = parse_command(*array);
        switch (command.type) {
            case 'f':
                position.position += command.magnitude;
                break;
            case 'd':
                position.depth += command.magnitude;
                break;
            case 'u':
                position.depth -= command.magnitude;
                break;
            default:
                return -1;
        }
        array++;
    }
    return position.position * position.depth;
}

int solution2(char** array) {
    Position position;
    position.position = 0;
    position.depth = 0;
    position.aim = 0;
    while (*array != NULL) {
        Command command = parse_command(*array);
        switch (command.type) {
            case 'f':
                position.position += command.magnitude;
                position.depth += position.aim * command.magnitude;
                break;
            case 'd':
                position.aim += command.magnitude;
                break;
            case 'u':
                position.aim -= command.magnitude;
                break;
            default:
                return -1;
        }
        array++;
    }
    return position.position * position.depth;
}

int main(int argc, char *args[]) {
    char* test_filename = get_test_input_filename(args);
    FILE* test_file = fopen(test_filename, "r");
    free(test_filename);
    if (test_file == NULL) {
        return -1;
    }
    char** test_array = load_strs(test_file);
    if (test_array == NULL) {
        return -1;
    }
    fclose(test_file);
    printf("%d\n", solution1(test_array));
    printf("%d\n", solution2(test_array));
    free_str_array(test_array);

    char* filename = get_input_filename(args);
    FILE* file = fopen(filename, "r");
    free(filename);
    if (file == NULL) {
        return -1;
    }
    char** array = load_strs(file);
    if (array == NULL) {
        return -1;
    }
    fclose(file);
    printf("%d\n", solution1(array));
    printf("%d\n", solution2(array));
    free_str_array(array);
    return 0;
}
