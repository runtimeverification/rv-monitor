#include <stdlib.h>
#include <stdio.h>

int isNull(int * x) {
  return x == NULL;
}

int value(int* x) {
  return *x;
}


int main(void) {
	int* v[5], i, sum = 0;
	fprintf(stdout, "Test 2\n");
	fprintf(stderr, "Test 2\n");

	v[0] = (int *)malloc(sizeof(int)); *(v[0]) = 1;
	v[1] = (int *)malloc(sizeof(int)); *(v[1]) = 2;
	v[2] = NULL;
	v[3] = (int *)malloc(sizeof(int)); *(v[3]) = 4;
	v[4] = (int *)malloc(sizeof(int)); *(v[4]) = 8;

	for (i = 0; i < 5; i++) {
    if (!isNull(v[i])) {
			sum += value(v[i]);
		}
	}

	printf("sum: %d\n", sum);
}



