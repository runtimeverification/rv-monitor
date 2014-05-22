#include "../lib_aspc/door_ajar.h"
#include <stdio.h>

int main(void) {
	fprintf(stdout, "Test 1 ASPC\n");
	fprintf(stderr, "Test 1 ASPC\n");
	doorOperation("open");
	doorOperation("close");
	engineOperation("start");
}
