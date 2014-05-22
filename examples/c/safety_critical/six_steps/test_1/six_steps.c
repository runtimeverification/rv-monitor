#include "../__RVC_SixSteps_Monitor.h"
#include <stdio.h>

int main(void) {
	fprintf(stdout, "Test 1\n");
	fprintf(stderr, "Test 1\n");
	__RVC_SixSteps_step();
	__RVC_SixSteps_complete();
	__RVC_SixSteps_step();
	__RVC_SixSteps_step();
	__RVC_SixSteps_step();
	__RVC_SixSteps_step();
	__RVC_SixSteps_step();
	__RVC_SixSteps_complete();
	__RVC_SixSteps_step();
	__RVC_SixSteps_step();
	__RVC_SixSteps_step();
	__RVC_SixSteps_step();
	__RVC_SixSteps_step();
	__RVC_SixSteps_step();
	__RVC_SixSteps_step();
	__RVC_SixSteps_complete();
	__RVC_SixSteps_step();
	__RVC_SixSteps_step();
	__RVC_SixSteps_step();
	__RVC_SixSteps_step();
	__RVC_SixSteps_step();
	__RVC_SixSteps_complete();
}
