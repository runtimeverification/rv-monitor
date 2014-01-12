#include <stdlib.h>
#include <stdio.h>

extern	void __RVC_IsNull_isNull(void);
extern	void __RVC_IsNull_deref(void);

int main(void) {
	int* v[5], i, sum = 0;

	v[0] = (int *)malloc(sizeof(int)); *(v[0]) = 1;
	v[1] = (int *)malloc(sizeof(int)); *(v[1]) = 2;
	v[2] = NULL;
	v[3] = (int *)malloc(sizeof(int)); *(v[3]) = 4;
	v[4] = (int *)malloc(sizeof(int)); *(v[4]) = 8;

	for (i = 0; i < 5; i++) {
		if (v[i]) {
			__RVC_IsNull_isNull();
			__RVC_IsNull_deref();
			sum += *(v[i]);
		} else {
			__RVC_IsNull_isNull();
		}
	}

	printf("sum: %d\n", sum);
}



