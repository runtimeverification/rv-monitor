#include <stdio.h>

void changeHeadlights(char * mode) {
    // Change to specified headlights mode
}

int main(void) {
    fprintf(stdout, "Test 2\n");
    fprintf(stderr, "Test 2\n");
    changeHeadlights("start");
    changeHeadlights("on");
    changeHeadlights("manual");
    changeHeadlights("off");
    changeHeadlights("stop");
    changeHeadlights("auto");
}
