#include <string.h>
extern void __RVC_DoorAjar_doorOpen();
extern void __RVC_DoorAjar_doorClose();
extern void __RVC_DoorAjar_startEngine();

void dispatchDoor(const char * argu) {
    if (strcmp(argu, "open") == 0) {
        __RVC_DoorAjar_doorOpen();
    }
    else if (strcmp(argu, "close") == 0) {
        __RVC_DoorAjar_doorClose();
    }
}

void dispatchEngine(const char * argu) {
    if (strcmp(argu, "start") == 0) {
        __RVC_DoorAjar_startEngine();
    }
}
