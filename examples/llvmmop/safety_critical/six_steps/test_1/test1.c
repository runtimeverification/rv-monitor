#include "../lib/six_steps.h"
#include <stdio.h>

int main(void) {
  fprintf(stdout, "Test 1\n");
  fprintf(stderr, "Test 1\n");
  step();
  done();
  procedure(5);
  step();
  step();
  procedure(5);
  step();
  procedure(4);
}
