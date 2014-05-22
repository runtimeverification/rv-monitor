#include "../__RVC_SeatBelt_Monitor.h"
#include <time.h>
#include <stdio.h>

int main() {
	struct timespec tim;
	fprintf(stdout, "Test 4\n");
	fprintf(stderr, "Test 4\n");
    tim.tv_sec  = 1;
    tim.tv_nsec = 0;
	__RVC_SeatBelt_seatBeltAttached();
	__RVC_SeatBelt_seatBeltRemoved();
    if(nanosleep(&tim , NULL) < 0 ) {
      return -1;
    }
	__RVC_SeatBelt_seatBeltAttached();
}
