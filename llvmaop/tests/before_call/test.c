#include <stdio.h>

int inc(int i) {
  printf("Incrementing i=%d\n",i);
  return i + 1;
}

void print(const char* str) {
  printf("%s", str);
}

int main() {
  print("Hello World!\n");
  int i = 7;
  i = inc(i);
  i = inc(i);
  i = inc(i);
  i = inc(i);
  i = inc(i);
  return 0;
}
