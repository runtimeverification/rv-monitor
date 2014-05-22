#include "../__RVC_DoorAjar_Monitor.h"
#include <stdio.h>

int main(void) {
	fprintf(stdout, "Test 1\n");
	fprintf(stderr, "Test 1\n");
	__RVC_DoorAjar_doorOpen();
	__RVC_DoorAjar_doorClose();
	__RVC_DoorAjar_startEngine();
}
