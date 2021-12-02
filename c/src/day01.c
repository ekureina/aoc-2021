#include <stdio.h>
#include "lib.h"

int solution1(int* array, int array_len) {
    int decrease_count = 0;
    for (int i = 0; i < array_len-1; i++) {
        decrease_count += array[i] < array[i+1];
    }
    return decrease_count;
}

int solution2(int* array, int array_len) {
    int decrease_count = 0;
    for (int i = 0; i < array_len - 3; i++) {
        decrease_count += array[i] < array[i+3];
    }
    return decrease_count;
}

int main(int argc, char *args[]) {
    char* test_filename = get_test_input_filename(args);
    FILE* test_file = fopen(test_filename, "r");
    if (test_file == NULL) {
        return -1;
    }
    int* test_array = NULL;
    int test_array_len = load_ints(test_file, &test_array);
    if (test_array_len == -1) {
        return -1;
    }
    fclose(test_file);
    printf("%d\n", solution1(test_array, test_array_len));
    printf("%d\n", solution2(test_array, test_array_len));
    free(test_array);
    char* filename = get_input_filename(args);
    FILE* file = fopen(filename, "r");
    if (file == NULL) {
        return -1;
    }
    int* array = NULL;
    int array_len = load_ints(file, &array);
    if (array == NULL) {
        return -1;
    }
    fclose(file);
    printf("%d\n", solution1(array, array_len));
    printf("%d\n", solution2(array, array_len));
    free(array);
    return 0;
}
