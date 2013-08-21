#include "../__RVC_DoorAjar_Monitor.h"

int main(void) {
	__RVC_DoorAjar_doorOpen();
	__RVC_DoorAjar_startEngine();
	__RVC_DoorAjar_doorClose();
}
