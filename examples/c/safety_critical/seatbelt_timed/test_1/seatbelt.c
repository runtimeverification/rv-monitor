#include "../__RVC_SeatBelt_Monitor.h"
#include <stdio.h>

int main() {
	fprintf(stdout, "Test 1\n");
	fprintf(stderr, "Test 1\n");
	__RVC_SeatBelt_seatBeltAttached();
	__RVC_SeatBelt_seatBeltRemoved();
	__RVC_SeatBelt_seatBeltAttached();
}
