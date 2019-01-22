#ifndef WATERTANKSIMULATION_H
#define WATERTANKSIMULATION_H
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#include <signal.h>
#include <stdbool.h>

// Struct holding constant values
typedef struct parameters {
  long double ep;
  long double m;
} parameters;

// Struct holding state related information
// f is flow controlled via the controller
// l is the level of water sensed
// c is update clock
typedef struct state {
  long double f;
  long double l;
  long double c;
} state;

typedef struct input {
} input;

state old, ctrl_decision, new;
parameters * par_ptr;
long elapsed_time;

#endif
