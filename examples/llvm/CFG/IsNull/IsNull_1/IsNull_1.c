#include <stdlib.h>
#include <stdio.h>

extern void __RVC_IsNull_deref(void);

int main(void) {
	int* v[4], i, sum = 0;

	v[0] = (int *)malloc(sizeof(int)); *(v[0]) = 1;
	v[1] = (int *)malloc(sizeof(int)); *(v[1]) = 2;
	v[2] = (int *)malloc(sizeof(int)); *(v[2]) = 4;
	v[3] = (int *)malloc(sizeof(int)); *(v[3]) = 8;

	for (i = 0; i < 4; i++) {
		__RVC_IsNull_deref();
		sum += *(v[i]);
	}

	printf("sum: %d\n", sum);
}



