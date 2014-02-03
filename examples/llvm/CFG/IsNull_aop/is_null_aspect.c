extern	void __RVC_IsNull_isNull(void);
extern	void __RVC_IsNull_deref(void);

void isNullAspect(int isnull, int* p) {
  __RVC_IsNull_isNull();
} 

void derefAspect(int* p) {
  __RVC_IsNull_deref();
}
