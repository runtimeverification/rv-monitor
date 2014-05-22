#include "../CanIf.h"
#include <stdio.h>

int main( int argc, const char* argv[] )
{
    fprintf(stdout, "Test 3\n");
    fprintf(stderr, "Test 3\n");
    CanIf_Transmit();
    CanIf_Init();
    CanIf_Init();
    CanIf_GetControllerMode(5);
    CanIf_Transmit();
    CanIf_Transmit();
}
