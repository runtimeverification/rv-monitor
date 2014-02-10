#include "../__RVC_SeatBelt_Monitor.h"
#include <time.h>
#include <stdio.h>
int main() {
    struct timespec tim;
    tim.tv_sec  = 1;
    tim.tv_nsec = 0;
	__RVC_SeatBelt_seatBeltAttached();
	__RVC_SeatBelt_seatBeltRemoved();
    if(nanosleep(&tim , NULL) < 0 ) {
      return -1;
    }
	__RVC_SeatBelt_seatBeltAttached();
}
