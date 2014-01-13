#include "../__RVC_AutoHeadlights_Monitor.h"

int main(void) {
    changeHeadlights("stop");
    changeHeadlights("on");
    changeHeadlights("manual");
    changeHeadlights("off");
    changeHeadlights("start");
    changeHeadlights("auto");
}

void changeHeadlights(char * mode) {
    // Change to specified headlights mode
}
