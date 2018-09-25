#include <stdio.h>
extern int inc(int);

int incAspect(int arg) {
  int ret = inc(arg);
  if (ret > 10) {
    printf("Maximum capacity reached.  Returning 10.\n");
    return 10;
  }
  return ret;
}
