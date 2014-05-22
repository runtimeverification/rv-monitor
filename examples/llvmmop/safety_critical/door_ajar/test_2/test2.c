#include "../lib/door_ajar.h"
#include <stdio.h>

int main(void) {
	fprintf(stdout, "Test 2\n");
	fprintf(stderr, "Test 2\n");
	doorOperation("open");
	engineOperation("start");
	doorOperation("close");
}
