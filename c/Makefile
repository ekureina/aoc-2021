CC=gcc
CFLAGS=-Wall -Werror -Wno-unused-variable -Iinclude -std=gnu17 -O3 -g
SRCS=$(wildcard src/*.c)
INCLUDES=$(wildcard include/*.h)
EXECUTABLES=$(patsubst src/%.c, build/%, $(SRCS))
EXECUTABLE_NAMES=$(notdir $(EXECUTABLES))

all: $(EXECUTABLES)

build:
	mkdir build

clean:
	rm -rf build

build/%: src/%.c build $(INCLUDES)
	$(CC) $(CFLAGS) $< -o $@

$(EXECUTABLE_NAMES): %: build/%
