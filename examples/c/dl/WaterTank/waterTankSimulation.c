#include "waterTankSimulation.h"


static volatile int run_flag;


void intHandler(int dummy) {
    printf("Ctrl-C called, simulation ended \n");
    printf("Water level is %Lf\n", new.l);
    free(par_ptr);
    run_flag = 0;
}

/**
 * Use the controller logic to get a new control step.
 * In this case, controller just returns the previous flow,
 * which is positive.
 **/
state ctrlDecision(state curr, const parameters* const params) {
    state newState;
    newState.f = curr.f + 0.0005;
    return newState;
}

/**
 * Use the control values to actuate.
 * In this case, the environment behaves exactly as intended.
 **/
state actuateDecision(state curr, state decision, const parameters* const params) {
    state newState;
    newState.f = decision.f;
    newState.l = curr.l + (decision.f * 0.2);
    newState.c = curr.c + 0.2;
    usleep(200000); // simulate actuation time
    return newState;
}

/**
 * Combines control decision and actuate decision into a control cycle.
 **/
state ctrlStep(state curr, const parameters* const params) {
    ctrl_decision = ctrlDecision(curr, params);
    printf("Decision flow is %Lf \n", ctrl_decision.f);
    return actuateDecision(curr, ctrl_decision, params);
}

/* Assume the following units -
 * Time in s
 * max level in m
 * flow in m/s
 */
void init() {
  elapsed_time = 0;
  run_flag = 1;

  par_ptr = malloc(sizeof(parameters));


  par_ptr->m = 0.4;
  par_ptr->ep = 1;

  old.f = 0.05;
  old.l = 0.1;
  old.c = 0;

  ctrl_decision = old;
  new = old;

  signal(SIGINT, intHandler);
}

void simulation_loop() {
  while(run_flag) {
    old = new;
    // reset clock
    old.c = 0;
    new = ctrlStep(old, par_ptr);
  }
}

int main() {
  init();
  simulation_loop();
}

