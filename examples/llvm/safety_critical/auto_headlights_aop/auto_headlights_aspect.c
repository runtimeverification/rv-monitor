#include <string.h>

extern void __RVC_AutoHeadlights_on(void);
extern void __RVC_AutoHeadlights_stop(void);
extern void __RVC_AutoHeadlights_manual(void);
extern void __RVC_AutoHeadlights_off(void);
extern void __RVC_AutoHeadlights_start(void);
extern void __RVC_AutoHeadlights_auto(void);

void changeHeadlightsAspect(char* argu) {
    if (strcmp(argu, "stop") == 0) {
        __RVC_AutoHeadlights_stop();
    }
    else if (strcmp(argu, "on") == 0) {
        __RVC_AutoHeadlights_on();
    }
    else if (strcmp(argu, "manual") == 0) {
        __RVC_AutoHeadlights_manual();
    }
    else if (strcmp(argu, "off") == 0) {
        __RVC_AutoHeadlights_off();
    }
    else if (strcmp(argu, "start") == 0) {
        __RVC_AutoHeadlights_start();
    }
    else if (strcmp(argu, "auto") == 0) {
        __RVC_AutoHeadlights_auto();
    }
}
