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

static void __RV_delete_RV_stack(__RV_stack *stack){
  free(stack->data);
  free(stack);
}

static void __RV_delete_RV_stacks(__RV_stacks *stacks){
  free(stacks->data);
  free(stacks);
}

static void __RV_delete_all_RV_stacks(__RV_stacks *stacks){
  int i;
  for(i = 0; i < stacks->length; ++i){
    __RV_delete_RV_stack(stacks->data[i]);
  }
  free(stacks->data);
  free(stacks);
}

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

static void __RV_add_i(__RV_stacks *stacks, int i, __RV_stack *elem){
  stacks->data[i] = elem;
}

static __RV_stack *__RV_get(__RV_stacks *stacks, int i){
  return stacks->data[i];
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
  int i,j;
  __RV_stacks *stacks = __RV_new_RV_stacks(1);

  for(i = 0; i < 4; ++i){
    __RV_add(stacks, __RV_new_RV_stack(1));
    for(j = 0; j < 20; ++j){
       __RV_push(__RV_get(stacks, i), i + j);
    }
  }

  for(j = 0; j < 20; ++j){
    for(i = 0; i < 4; ++i){
      printf("%d\n", __RV_pop(__RV_get(stacks, i))); 
    }
  }
}
