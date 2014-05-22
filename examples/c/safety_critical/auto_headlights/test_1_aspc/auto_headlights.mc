#include <stdio.h>

int main(void) {
    fprintf(stdout, "Test 1 ASPC\n");
	fprintf(stderr, "Test 1 ASPC\n");
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
