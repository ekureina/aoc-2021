#ifndef __LIB_H__
#define __LIB_H__
#include <ctype.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>

char* get_test_input_filename(char *args[]) {
    char *progname = args[0];
    while (*progname++ != '/');
    int len = strlen(progname);
    char* filename = malloc(18 + len);
    if (filename == NULL) {
        return filename;
    }
    strncpy(filename, "../data/", 9);
    strncat(filename, progname, len + 1);
    strncat(filename, "_test.txt", 10);
    filename[8] = toupper(filename[8]);
    return filename;
}

char* get_input_filename(char *args[]) {
    char *progname = args[0];
    while (*progname++ != '/');
    int len = strlen(progname);
    char* filename = malloc(13 + len);
    if (filename == NULL) {
        return filename;
    }
    strncpy(filename, "../data/", 9);
    strncat(filename, progname, len + 1);
    strncat(filename, ".txt", 5);
    filename[8] = toupper(filename[8]);
    return filename;
}

void free_str_array(char** array) {
    if (array != NULL) {
        char** cleanup_helper = array;
        for (char** cleanup_helper = array; *cleanup_helper; cleanup_helper++) {
            free(*cleanup_helper);
        }
        free(array);
    }
}

char** load_strs(FILE* file) {
    int array_cap = 5;
    int array_len = 0;
    size_t str_size = 0;
    char** array = malloc(array_cap * sizeof(char*));
    if (array == NULL) {
        return NULL;
    }

    int old_errno = errno;
    errno = 0;
    do {
        if (array_len == array_cap) {
            array_cap *= 2;
            array = realloc(array, array_cap * sizeof(char*));
            if (array == NULL) {
                return NULL;
            }
        }
        array[array_len] = NULL;
        if (getline(array + array_len, &str_size, file) == -1) {
            if (errno != 0) {
                if (array != NULL) {
                    for (int i = 0; i < array_len; i++) {
                        free(array[i]);
                    }
                    free(array);
                }
                errno = old_errno;
                return NULL;
            } else {
                break;
            }
        }
        array_len++;
    } while (1);
    if (feof(file)) {
        // Free extra line allocation created if needed
        if (array[array_len] != NULL) {
            free(array[array_len]);
            array[array_len] = NULL;
        }
        return array;
    } else { // Some error processing the file
        if (array != NULL) {
            for (int i = 0; i < array_len; i++) {
                free(array[i]);
            }
            free(array);
        }
        return NULL;
    }
}

int load_ints(FILE* file, int** pointer) {
    int array_cap = 5;
    int array_len = 0;
    *pointer = malloc(array_cap * sizeof(int));
    if (*pointer == NULL) {
        return -1;
    }
    char buf[11]; // Up to 10 characters in u32
    while (fgets(buf, sizeof(buf), file) != NULL) {
        if (array_len == array_cap) {
            array_cap *= 2;
            *pointer = realloc(*pointer, array_cap * sizeof(int));
            if (*pointer == NULL) {
                return -1;
            }
        }
        int parsed_int = atoi(buf);
        (*pointer)[array_len++] = parsed_int;
    }
    if (feof(file)) {
        return array_len;
    } else { // Some error processing the file
        free(*pointer);
        *pointer = NULL;
        return -1;
    }
}
#endif
