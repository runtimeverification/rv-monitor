#include "../__RVC_SeatBelt_Monitor.h"

int main() {
	__RVC_SeatBelt_seatBeltAttached();
	__RVC_SeatBelt_seatBeltRemoved();
    while (1) {}
	__RVC_SeatBelt_seatBeltAttached();
}
