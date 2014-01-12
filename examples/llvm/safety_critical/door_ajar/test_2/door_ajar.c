extern void	__RVC_DoorAjar_doorOpen(void);
extern void	__RVC_DoorAjar_doorClose(void);
extern void	__RVC_DoorAjar_startEngine(void);

int main(void) {
	__RVC_DoorAjar_doorOpen();
	__RVC_DoorAjar_startEngine();
	__RVC_DoorAjar_doorClose();
}
