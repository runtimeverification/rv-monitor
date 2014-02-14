#include "six_steps.h"
#include <stdio.h>
int steps = 0;

void step(void) {
  printf("Step %d\n", ++steps);
}

void done(void) {
  printf("Done in %d step%s.\n", steps, steps == 1 ? "" : "s");
  steps = 0;
}

void procedure(int steps) {
  int i;
  for (i = 0; i < steps; i++) step();
  done();
}
