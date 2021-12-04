#include <stdio.h>
#include "lib.h"

typedef struct BitCount {
    int ones;
    int zeros;
} BitCount;

void BitCount_clear(BitCount* counts, int countLen) {
    for (int i = 0; i < countLen; ++i) {
        counts[i].ones = 0;
        counts[i].zeros = 0;
    }
}

int solution1(char** array) {
    int bit_len = strlen(array[0]) - 1;
    BitCount* bit_counts = malloc(bit_len * sizeof(BitCount));
    if (bit_counts == NULL) {
        exit(EXIT_FAILURE);
    }
    BitCount_clear(bit_counts, bit_len);
    int gamma = 0;
    int epsilon = 0;
    for (int i = 0; i < bit_len; ++i) {
        char** iterator = array;
        while (*iterator != NULL) {
            if ((*iterator)[i] == '0') {
                bit_counts[i].zeros++;
            } else {
                bit_counts[i].ones++;
            }
            ++iterator;
        }
        gamma <<= 1;
        gamma += (bit_counts[i].ones > bit_counts[i].zeros);
        epsilon <<= 1;
        epsilon += (bit_counts[i].zeros > bit_counts[i].ones);
    }
    free(bit_counts);

    return gamma * epsilon; 
}

int solution2(char** array, int array_len) {
    int bit_len = strlen(array[0]) - 1;
    BitCount* bit_counts1 = malloc(bit_len * sizeof(BitCount));
    if (bit_counts1 == NULL) {
        exit(EXIT_FAILURE);
    }
    int* excluded1 = calloc(array_len, sizeof(int));
    int included_count1 = array_len;
    if (excluded1 == NULL) {
        exit(EXIT_FAILURE);
    }
    BitCount_clear(bit_counts1, bit_len);
    BitCount* bit_counts2 = malloc(bit_len * sizeof(BitCount));
    if (bit_counts2 == NULL) {
        exit(EXIT_FAILURE);
    }
    BitCount_clear(bit_counts2, bit_len);
    int* excluded2 = calloc(array_len, sizeof(int));
    int included_count2 = array_len;
    if (excluded2 == NULL) {
        exit(EXIT_FAILURE);
    }

    for (int i = 0; i < bit_len; ++i) {
        for (int j = 0; j < array_len; ++j) {
            if (included_count1 > 1 && !excluded1[j]) {
                if (array[j][i] == '0') {
                    bit_counts1[i].zeros++;
                } else {
                    bit_counts1[i].ones++;
                }
            }

            if (included_count2 > 1 && !excluded2[j]) {
                if (array[j][i] == '0') {
                    bit_counts2[i].zeros++;
                } else {
                    bit_counts2[i].ones++;
                }
            }
        }

        for (int j = 0; j < array_len; ++j) {
            if (included_count1 > 1) {
                if (bit_counts1[i].ones >= bit_counts1[i].zeros) {
                    included_count1 -= (array[j][i] == '0') && !excluded1[j];
                    excluded1[j] |= (array[j][i] == '0');
                } else {
                    included_count1 -= (array[j][i] == '1') && !excluded1[j];
                    excluded1[j] |= (array[j][i] == '1');
                }
            }

            if (included_count2 > 1) {
                if (bit_counts2[i].zeros <= bit_counts2[i].ones) {
                    included_count2 -= (array[j][i] == '1') && !excluded2[j];
                    excluded2[j] |= (array[j][i] == '1');
                } else {
                    included_count2 -= (array[j][i] == '0') && !excluded2[j];
                    excluded2[j] |= (array[j][i] == '0');
                }
            }
            if (included_count1 == 1 && included_count2 == 1) break;
        }

        if (included_count1 == 1 && included_count2 == 1) break;
    }
    
    if (included_count1 > 1 || included_count2 > 1) return -1;

    int oxy_rating = -1;
    int co2_rating = -1;
    for (int i = 0; i < array_len && (oxy_rating == -1 || co2_rating == -1); ++i) {
        if (!excluded1[i]) {
            oxy_rating = strtol(array[i], NULL, 2);
        }

        if (!excluded2[i]) {
            co2_rating = strtol(array[i], NULL, 2);
        }
    }

    free(bit_counts1);
    free(bit_counts2);
    free(excluded1);
    free(excluded2);

    return oxy_rating * co2_rating;
}

int main(int argc, char *args[]) {
    char* test_filename = get_test_input_filename(args);
    FILE* test_file = fopen(test_filename, "r");
    free(test_filename);
    if (test_file == NULL) {
        exit(EXIT_FAILURE);
    }
    char** test_array = load_strs(test_file);
    if (test_array == NULL) {
        exit(EXIT_FAILURE);
    }
    fclose(test_file);
    printf("%d\n", solution1(test_array));
    int test_array_len = get_array_len(test_array);
    printf("%d\n", solution2(test_array, test_array_len));
    free_str_array(test_array);

    char* filename = get_input_filename(args);
    FILE* file = fopen(filename, "r");
    free(filename);
    if (file == NULL) {
        exit(EXIT_FAILURE);
    }
    char** array = load_strs(file);
    if (array == NULL) {
        exit(EXIT_FAILURE);
    }
    fclose(file);
    printf("%d\n", solution1(array));
    int array_len = get_array_len(array);
    printf("%d\n", solution2(array, array_len));
    free_str_array(array);
    return 0;
}
