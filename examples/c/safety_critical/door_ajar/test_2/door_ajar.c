#include "../__RVC_DoorAjar_Monitor.h"
#include <stdio.h>

int main(void) {
	fprintf(stdout, "Test 2\n");
	fprintf(stderr, "Test 2\n");
	__RVC_DoorAjar_doorOpen();
	__RVC_DoorAjar_startEngine();
	__RVC_DoorAjar_doorClose();
}
