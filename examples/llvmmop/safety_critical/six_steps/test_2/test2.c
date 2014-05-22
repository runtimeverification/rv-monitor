#include "../lib/six_steps.h"
#include <stdio.h>

int main(void) {
	fprintf(stdout, "Test 2\n");
	fprintf(stderr, "Test 2\n");
	step();
	done();
  procedure(5);
  step();
  step();
  procedure(4);
  step();
  step();
  procedure(3);
}
