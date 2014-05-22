#include <stdio.h>

int main(void) {
    fprintf(stdout, "Test 2 ASPC\n");
    fprintf(stderr, "Test 2 ASPC\n");
    changeHeadlights("start");
    changeHeadlights("on");
    changeHeadlights("manual");
    changeHeadlights("off");
    changeHeadlights("stop");
    changeHeadlights("auto");
}

void changeHeadlights(char * mode) {
    // Change to specified headlights mode
}
