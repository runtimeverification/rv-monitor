#include <stdlib.h>
#include <stdio.h>

typedef struct stack{
  int current_index;
  int length;
  int *data;
} __RV_stack;

typedef struct stacks{
  int current_index;
  int length;
  __RV_stack **data;
} __RV_stacks;

static __RV_stack* __RV_new_RV_stack(int size){
  __RV_stack *ret = (__RV_stack *) malloc(sizeof(__RV_stack));
  ret->current_index = 0;
  ret->length = size;
  ret->data = (int *) malloc(sizeof(int) * size);
  return ret; 
}

static __RV_stacks* __RV_new_RV_stacks(int size){
  __RV_stacks *ret = (__RV_stacks *) malloc(sizeof(__RV_stacks));
  ret->current_index = 0;
  ret->length = size;
  ret->data = (__RV_stack **) malloc(sizeof(__RV_stack *) * size);
  return ret; 
}

static void __RV_add(__RV_stacks *stacks, __RV_stack *elem){
  if(stacks->current_index >= stacks->length) {
    int i;
    //free(stack->data);  
    //stack->length <<= 1;
    //stack->data = (int *) malloc(sizeof(int) * stack->length);
    __RV_stack **tmp 
       = (__RV_stack **) malloc(sizeof(__RV_stack *) * stacks->length * 2);
    for(i = 0; i < stacks->length; ++i){
      tmp[i] = stacks->data[i];
    } 
    stacks->length *= 2;
    free(stacks->data);
    stacks->data = tmp;
  } 
  stacks->data[(stacks->current_index)++] = elem;
}

static int __RV_peek(__RV_stack *stack){
  return stack->data[stack->current_index - 1];
}

static int __RV_pop(__RV_stack *stack){
  return stack->data[--(stack->current_index)];
}

static void __RV_pop_n(__RV_stack *stack, int n){
  stack->current_index -= n;
}

static void __RV_push(__RV_stack *stack, int elem){
  if(stack->current_index >= stack->length) {
    int i;
    //free(stack->data);  
    //stack->length <<= 1;
    //stack->data = (int *) malloc(sizeof(int) * stack->length);
    int *tmp = (int *) malloc(sizeof(int) * stack->length * 2);
    for(i = 0; i < stack->length; ++i){
      tmp[i] = stack->data[i];
    } 
    stack->length *= 2;
    free(stack->data);
    stack->data = tmp;
  } 
  stack->data[(stack->current_index)++] = elem;
}

int main(){
  __RV_stack *stack = __RV_new_RV_stack(10);
  int i;
  for(i =0; i < 19; ++i) __RV_push(stack, i);
  for(i =0; i < 19; ++i) printf("%d\n", __RV_pop(stack));

  for(i =0; i < 9; ++i) __RV_push(stack, i + 10);
  for(i =0; i < 9; ++i) printf("%d\n", __RV_pop(stack));
}
