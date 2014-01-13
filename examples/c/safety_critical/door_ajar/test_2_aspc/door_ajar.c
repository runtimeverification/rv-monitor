#include "../lib_aspc/door_ajar.h"

int main(void) {
	doorOperation("open");
	engineOperation("start");
	doorOperation("close");
}
