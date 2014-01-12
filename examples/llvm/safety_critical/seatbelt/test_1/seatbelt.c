extern void	__RVC_SeatBelt_seatBeltAttached(void);
extern void	__RVC_SeatBelt_seatBeltRemoved(void);

int main() {
	__RVC_SeatBelt_seatBeltAttached();
	__RVC_SeatBelt_seatBeltRemoved();
	__RVC_SeatBelt_seatBeltAttached();
}
