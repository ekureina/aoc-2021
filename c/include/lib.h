#ifndef __LIB_H__
#define __LIB_H__
#include <ctype.h>
#include <string.h>
#include <stdlib.h>

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
