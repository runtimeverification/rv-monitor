extern void	__RVC_AutoHeadlights_stop(void);
extern void	__RVC_AutoHeadlights_on(void);
extern void	__RVC_AutoHeadlights_manual(void);
extern void	__RVC_AutoHeadlights_off(void);
extern void	__RVC_AutoHeadlights_start(void);
extern void	__RVC_AutoHeadlights_auto(void);

int main(void) {
	__RVC_AutoHeadlights_stop();
	__RVC_AutoHeadlights_on();
	__RVC_AutoHeadlights_manual();
	__RVC_AutoHeadlights_off();
	__RVC_AutoHeadlights_start();
	__RVC_AutoHeadlights_auto();
}

