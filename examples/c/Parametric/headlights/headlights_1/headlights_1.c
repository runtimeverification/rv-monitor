#include <stdlib.h>
#include <stdio.h>
#include "../__RVC_headlights_Monitor.h"

int main(void) {
   void *car1, *car2, *car3;
   fprintf(stdout, "Test 1\n");
   fprintf(stderr, "Test 1\n");

   car1 = (void *) 15;
   car2 = (void *) 30;
   car3 = (void *) 45;

   __RVC_headlights_on(car1);
   __RVC_headlights_on(car2);
   __RVC_headlights_on(car3);
   __RVC_headlights_on(car1);
   __RVC_headlights_on(car1);
   __RVC_headlights_off(car3);
   __RVC_headlights_on(car2);
}



