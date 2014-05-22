#include "../__RVC_AutoHeadlights_Monitor.h"
#include <stdio.h>

int main(void) {
	fprintf(stdout, "Test 1\n");
	fprintf(stderr, "Test 1\n");
	__RVC_AutoHeadlights_stop();
	__RVC_AutoHeadlights_on();
	__RVC_AutoHeadlights_manual();
	__RVC_AutoHeadlights_off();
	__RVC_AutoHeadlights_start();
	__RVC_AutoHeadlights_auto();
}

