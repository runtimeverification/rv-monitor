#include "../__RVC_SeatBelt_Monitor.h"
#include <time.h>
#include <stdio.h>

int main() {
	struct timespec tim;
	fprintf(stdout, "Test 3\n");
	fprintf(stderr, "Test 3\n");
    tim.tv_sec  = 0;
    tim.tv_nsec = 970000000L;
	__RVC_SeatBelt_seatBeltAttached();
	__RVC_SeatBelt_seatBeltRemoved();
    if(nanosleep(&tim , NULL) < 0 ) {
      return -1;
    }
	__RVC_SeatBelt_seatBeltAttached();
}
