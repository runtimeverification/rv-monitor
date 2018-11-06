#ifndef FORWARD_DRIVING_CAR_H
#define FORWARD_DRIVING_CAR_H

typedef struct parameters {
  long double A; // maximum acceleration
  long double B; // maximum braking
} parameters;

typedef struct state {
  long double a;
  long double v;
  long double x;
} state;

extern state ctrlStep(state curr, const parameters* const params);

#endif
