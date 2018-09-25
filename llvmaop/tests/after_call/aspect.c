#include <stdio.h>
#include <stdlib.h>

void incAspect(int ret, int arg) {
  if (ret>10) {
    printf("Function inc was called with argument %d and returned %d\n",
        arg, ret);
    printf("Maximum limit passed.  Exiting...\n");
    exit(1);
  }
}
