#include "../lib/door_ajar.h"
#include <stdio.h>

int main(void) {
	fprintf(stdout, "Test 1\n");
	fprintf(stderr, "Test 1\n");
	doorOperation("open");
	doorOperation("close");
	engineOperation("start");
}
