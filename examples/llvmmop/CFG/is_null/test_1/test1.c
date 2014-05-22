#include <stdlib.h>
#include <stdio.h>


int isNull(int * x) {
  return x == NULL;
}

int value(int* x) {
  return *x;
}

int main(void) {
	int* v[4], i, sum = 0;
	fprintf(stdout, "Test 1\n");
	fprintf(stderr, "Test 1\n");

	v[0] = (int *)malloc(sizeof(int)); *(v[0]) = 1;
	v[1] = (int *)malloc(sizeof(int)); *(v[1]) = 2;
	v[2] = (int *)malloc(sizeof(int)); *(v[2]) = 4;
	v[3] = (int *)malloc(sizeof(int)); *(v[3]) = 8;

	for (i = 0; i < 4; i++) {
		sum += value(v[i]);
	}

	printf("sum: %d\n", sum);
}



