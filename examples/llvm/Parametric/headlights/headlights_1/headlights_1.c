#include <stdlib.h>
#include <stdio.h>

extern void    __RVC_headlights_on(void*);
extern void    __RVC_headlights_on(void*);
extern void    __RVC_headlights_on(void*);
extern void    __RVC_headlights_on(void*);
extern void    __RVC_headlights_on(void*);
extern void    __RVC_headlights_off(void*);
extern void    __RVC_headlights_on(void*);

int main(void) {
   void *car1, *car2, *car3;

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



