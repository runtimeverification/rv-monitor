#include <stdio.h>

void changeHeadlights(char * mode) {
    // Change to specified headlights mode
}

int main(void) {
    fprintf(stdout, "Test 1\n");
    fprintf(stderr, "Test 1\n");
    changeHeadlights("stop");
    changeHeadlights("on");
    changeHeadlights("manual");
    changeHeadlights("off");
    changeHeadlights("start");
    changeHeadlights("auto");
}
