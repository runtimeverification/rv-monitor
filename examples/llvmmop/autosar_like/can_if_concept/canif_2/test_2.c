#include "../CanIf.h"
#include <stdio.h>

int main( int argc, const char* argv[] )
{
    fprintf(stdout, "Test 2\n");
    fprintf(stderr, "Test 2\n");
    CanIf_Init();
    CanIf_GetControllerMode(5);
    CanIf_Transmit();
    CanIf_Transmit();
}
