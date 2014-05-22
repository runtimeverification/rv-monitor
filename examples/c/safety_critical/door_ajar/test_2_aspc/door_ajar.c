#include "../lib_aspc/door_ajar.h"
#include <stdio.h>

int main(void) {
	fprintf(stdout, "Test 2 ASPC\n");
	fprintf(stderr, "Test 2 ASPC\n");
	doorOperation("open");
	engineOperation("start");
	doorOperation("close");
}
