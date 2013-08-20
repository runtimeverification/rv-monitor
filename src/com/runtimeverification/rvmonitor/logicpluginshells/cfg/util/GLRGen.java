package com.runtimeverification.rvmonitor.logicpluginshells.cfg.util;

import java.util.*;

public class GLRGen {
	static String gen(CFG g, HashMap<Terminal, Integer> tmap, String name) {
		return gen(new LR(g, tmap), name);
	}

	static String gen(CFG g) {
		return gen(g, "Foo");
	}

	static String gen(CFG g, String name) {
		HashMap<Terminal, Integer> tmap = new HashMap<Terminal, Integer>();
		int tint = 1;
		for (Terminal t : g.terminals())
			tmap.put(t, tint++);
		return gen(g, tmap, name);
	}

	static String gen(LR lr, String name) {
		return "package com.runtimeverification.rvmonitor.java.rvj.LogicPluginShells.JavaCFG.CFGUtil;\nimport java.util.*;\n"
				+ "class "
				+ name
				+ " {\n"
				+ state(lr)
				+ "\n"
				+ name
				+ "() {"
				+ init(lr)
				+ "\n}\n"
				+ "public void process(int $event$) {\n"
				+ body()
				+ "\n}\n"
				+ "public Category cat() { switch ($cat$) { case 0: return Category.ACCEPT; case 1: return Category.UNKNOWN; case 2: return Category.FAIL; } return Category.FAIL;}}";
	}

	// See body.txt
	public static String body() {
		return "if ($cat$ != 2) {\n" 
                        + "$event$--;\n" 
                        + "$cat$ = 1;\n" 
                        + "for (int $i$ = $stacks$.size()-1; $i$ >=0; $i$--) {\n"
		        + "IntStack stack = $stacks$.get($i$);\n" 
                        + "$stacks$.set($i$,null);\n" 
                        + "while (stack != null) {\n" 
                        + "int s = stack.peek();\n"
			+ "if (s >= 0 && $at$[s][$event$].length >= 0) {\n" 
                        + "/* not in an error state and something to do? */\n"
		        + "for (int j = 0; j < $at$[s][$event$].length; j++) {\n" 
                        + "IntStack tstack;\n" 
                        + "if ($at$[s][$event$].length > 1){\n"
			+ "tstack = stack.fclone();\n" 
                        + "} else{\n" 
                        + "tstack = stack;\n" 
                        + "}\n" 
                        + "switch ($at$[s][$event$][j].length) {\n" 
                        + "case 1:/* Shift */\n"
			+ "tstack.push($at$[s][$event$][j][0]);\n" 
                        + "$stacks$.add(tstack);\n" 
                        + "if ($acc$[$at$[s][$event$][j][0]]) $cat$ = 0;\n" 
                        + "break;\n"
			+ "case 2: /* Reduce */\n" 
                        + "tstack.pop($at$[s][$event$][j][1]);\n" 
                        + "int $old$ = tstack.peek();\n"
			+ "tstack.push($gt$[$old$][$at$[s][$event$][j][0]]);\n" 
                        + "$stacks$.add($i$,tstack);\n" 
                        + "break;\n" 
                        + "}\n" 
                        + "}\n" 
                        + "}\n"
			+ "stack = $stacks$.get($i$);\n" 
                        + "$stacks$.remove($i$);\n" 
                        + "}\n"
                        + "}\n" 
                        + "if ($stacks$.isEmpty())\n" 
                        + "$cat$ = 2;\n" 
                        + "}\n";
	}

        public static String cbody() {
		return "if (__RV_cat != 2) {\n"
                        + "  int i,j;\n" 
                        + "  event--;\n" 
                        + "  __RV_cat = 1;\n" 
                        + "  for (i = __RV_stacks->current_index-1; i >=0; --i) {\n"
		        + "     __RV_stack *stack = __RV_get(__RV_stacks, i);\n" 
                        + "     __RV_add_i(__RV_stacks, i, NULL);\n" 
                        + "     while (stack != NULL) {\n" 
                        + "     int s = __RV_peek(stack);\n"
			+ "     if (s >= 0 && sizeof(__RV_at[s][event]) >= 0) {\n" 
                        + "     /* not in an error state and something to do? */\n"
		        + "for (int j = 0; j < $at$[s][$event$].length; j++) {\n" 
                        + "IntStack tstack;\n" 
                        + "if ($at$[s][$event$].length > 1){\n"
			+ "tstack = stack.fclone();\n" 
                        + "} else{\n" 
                        + "tstack = stack;\n" 
                        + "}\n" 
                        + "switch ($at$[s][$event$][j].length) {\n" 
                        + "case 1:/* Shift */\n"
			+ "tstack.push($at$[s][$event$][j][0]);\n" 
                        + "$stacks$.add(tstack);\n" 
                        + "if ($acc$[$at$[s][$event$][j][0]]) $cat$ = 0;\n" 
                        + "break;\n"
			+ "case 2: /* Reduce */\n" 
                        + "tstack.pop($at$[s][$event$][j][1]);\n" 
                        + "int $old$ = tstack.peek();\n"
			+ "tstack.push($gt$[$old$][$at$[s][$event$][j][0]]);\n" 
                        + "$stacks$.add($i$,tstack);\n" 
                        + "break;\n" 
                        + "}\n" 
                        + "}\n" 
                        + "}\n"
			+ "stack = $stacks$.get($i$);\n" 
                        + "$stacks$.remove($i$);\n" 
                        + "}\n"
                        + "}\n" 
                        + "if ($stacks$.isEmpty())\n" 
                        + "$cat$ = 2;\n" 
                        + "}\n";
        }

	public static String init(LR lr) {
		return "IntStack stack = new IntStack();\n"
                   +   "stack.push(-2);\n"
                   +   "stack.push(" + Integer.toString(lr.q0) + ");\n"
                   +   "$stacks$.add(stack);";
	}

	public static String cinit(LR lr) {
                return 
                       "__RV_stack  *stack = __RV_new_RV_stack(10);\n"
                   +   "__RV_push(stack, -2);\n"
                   +   "__RV_push(stack, " + lr.q0 + ");\n"
                   +   "__RV_add(__RV_stacks_inst, stack);\n";
	}

	public static String reset(LR lr) {
		return "$stacks$.clear();\n" + init(lr);
	}

	public static String creset(LR lr) {
		return "__RV_clear(__RV_stacks_inst);\n" + cinit(lr);
	}

	public static String state(LR lr) {
		return "/* %%_%_CFG_%_%% */" 
                    + "ArrayList<IntStack> $stacks$ = new ArrayList<IntStack>();\nstatic int[][] $gt$ = " 
                    + lr.gtString()
		    + ";\nstatic int[][][][] $at$ = " 
                    + lr.atString() + ";\n" 
                    + "static boolean[] $acc$ = " 
                    + lr.accString() + ";\n"
		    + "int $cat$; // ACCEPT = 0, UNKNOWN = 1, FAIL = 2\nint $event$ = -1;";
	}

        public static String cstate(LR lr) {
          return "__RV_stacks *__RV_stacks_inst = NULL;\n"
              +  lr.cgtString()
              +  lr.catString()
              +  lr.caccString()
              + "static int __RV_cat; //ACCEPT = 0, UNKNOWN = 1, FAIL = 2\n"
              + "static int __RV_event$ = -1;\n";
        }

	public static String match() {
		return "$cat$ == 0";
	}

	public static String fail() {
		return "$cat$ == 2";
	}

	public static String intstack = "class IntStack implements java.io.Serializable {\n" 
            + "int[] data;\n" + "int curr_index = 0;\n" + "public IntStack(){\n"
	    + "data = new int[32];\n" + "}\n" + "public String toString(){\n" 
            + "String ret = \"[\";\n" + "for (int i = curr_index; i>=0; i--){\n"
            + "ret += Integer.toString(data[i])+\",\";\n" + "}\n" 
            + "return ret+\"]\";\n" + "}\n" + "public int hashCode() {\n" 
            + "return curr_index^peek();\n"
            + "}\n" + "public boolean equals(Object o) {\n" 
            + "if (o == null) return false;\n" + "if (!(o instanceof IntStack)) return false;\n"
            + "IntStack s = (IntStack)o;\n" + "if(curr_index != s.curr_index) return false;\n" 
            + "for(int i = 0; i < curr_index; i++){\n"
            + "if(data[i] != s.data[i]) return false;\n" + "}\n" 
            + "return true;\n" + "}\n" + "public IntStack(int size){\n" 
            + "data = new int[size];\n" + "}\n"
            + "public int peek(){\n" + "return data[curr_index - 1];\n" 
            + "}\n" + "public int pop(){\n" + "return data[--curr_index];\n" + "}\n"
            + "public void pop(int num){\n" + "curr_index -= num;\n" + "}\n" 
            + "public void push(int datum){\n" + "if(curr_index < data.length){\n"
            + "data[curr_index++] = datum;\n" + "} else{\n" 
            + "int len = data.length;\n" + "int[] old = data;\n" 
            + "data = new int[len * 2];\n"
            + "for(int i = 0; i < len; ++i){\n" + "data[i] = old[i];\n" 
            + "}\n" + "data[curr_index++] = datum;\n" + "}\n" + "}\n" 
            + "public IntStack fclone(){\n"
            + "IntStack ret = new IntStack(data.length);\n" 
            + "ret.curr_index = curr_index;\n" 
            + "for(int i = 0; i < curr_index; ++i){\n"
            + "ret.data[i] = data[i];\n" + "}\n" + "return ret;\n" + "}\n" 
            + "public void clear(){\n" + "curr_index = 0;\n" + "}\n" + "}\n";

public static String cintstack =
    "typedef struct stack{\n"
  + "  int current_index;\n"
  + "  int length;\n"
  + "  int *data;\n"
  + "} __RV_stack;\n"
  + "\n"
  + "typedef struct stacks{\n"
  + "  int current_index;\n"
  + "  int length;\n"
  + "  __RV_stack **data;\n"
  + "} __RV_stacks;\n"
  + "\n"
  + "static void __RV_delete_RV_stack(__RV_stack *stack){\n"
  + "  if(stack == NULL) return;\n"
  + "  free(stack->data);\n"
  + "  free(stack);\n"
  + "}\n"
  + "\n"
  + "static void __RV_delete_RV_stacks(__RV_stacks *stacks){\n"
  + "  if(stacks == NULL) return;\n"
  + "  free(stacks->data);\n"
  + "  free(stacks);\n"
  + "}\n"
  + "\n"
  + "static void __RV_clear(__RV_stacks *stacks){\n"
  + "  if(stacks == NULL) return;\n"
  + "  int i;\n"
  + "  for(i = 0; i < stacks->length; ++i){\n"
  + "    __RV_delete_RV_stack(stacks->data[i]);\n"
  + "  }\n"
  + "  stacks->current_index = 0;\n"
  + "}\n"
  + "\n"
  + "static void __RV_delete_all_RV_stacks(__RV_stacks *stacks){\n"
  + "  __RV_clear(stacks);\n"
  + "  if(stacks == NULL) return;\n"
  + "  free(stacks->data);\n"
  + "  free(stacks);\n"
  + "}\n"
  + "\n"
  + "static __RV_stack* __RV_new_RV_stack(int size){\n"
  + "  __RV_stack *ret = (__RV_stack *) malloc(sizeof(__RV_stack));\n"
  + "  ret->current_index = 0;\n"
  + "  ret->length = size;\n"
  + "  ret->data = (int *) malloc(sizeof(int) * size);\n"
  + "  return ret; \n"
  + "}\n"
  + "\n"
  + "static __RV_stacks* __RV_new_RV_stacks(int size){\n"
  + "  __RV_stacks *ret = (__RV_stacks *) malloc(sizeof(__RV_stacks));\n"
  + "  ret->current_index = 0;\n"
  + "  ret->length = size;\n"
  + "  ret->data = (__RV_stack **) malloc(sizeof(__RV_stack *) * size);\n"
  + "  return ret; \n"
  + "}\n"
  + "\n"
  + "static void __RV_add(__RV_stacks *stacks, __RV_stack *elem){\n"
  + "  if(stacks->current_index >= stacks->length) {\n"
  + "    int i;\n"
  + "    __RV_stack **tmp \n"
  + "       = (__RV_stack **) malloc(sizeof(__RV_stack *) * stacks->length * 2);\n"
  + "    for(i = 0; i < stacks->length; ++i){\n"
  + "      tmp[i] = stacks->data[i];\n"
  + "    } \n"
  + "    stacks->length *= 2;\n"
  + "    free(stacks->data);\n"
  + "    stacks->data = tmp;\n"
  + "  } \n"
  + "  stacks->data[(stacks->current_index)++] = elem;\n"
  + "}\n"
  + "\n"
  + "static void __RV_add_i(__RV_stacks *stacks, int i, __RV_stack *elem){\n"
  + "  stacks->data[i] = elem;\n"
  + "}\n"
  + "\n"
  + "static __RV_stack *__RV_remove(__RV_stacks *stacks, int i){\n"
  + "  __RV_stack *ret = stacks->data[i];\n"
  + "  stacks->data[i] = NULL;\n"
  + "  return ret;\n"
  + "}\n"
  + "\n"
  + "static __RV_stack *__RV_get(__RV_stacks *stacks, int i){\n"
  + "  return stacks->data[i];\n"
  + "}\n"
  + "\n"
  + "static int __RV_peek(__RV_stack *stack){\n"
  + "  return stack->data[stack->current_index - 1];\n"
  + "}\n"
  + "\n"
  + "static int __RV_pop(__RV_stack *stack){\n"
  + "  return stack->data[--(stack->current_index)];\n"
  + "}\n"
  + "\n"
  + "static void __RV_pop_n(__RV_stack *stack, int n){\n"
  + "  stack->current_index -= n;\n"
  + "}\n"
  + "\n"
  + "static void __RV_push(__RV_stack *stack, int elem){\n"
  + "  if(stack->current_index >= stack->length) {\n"
  + "    int i;\n"
  + "    int *tmp = (int *) malloc(sizeof(int) * stack->length * 2);\n"
  + "    for(i = 0; i < stack->length; ++i){\n"
  + "      tmp[i] = stack->data[i];\n"
  + "    } \n"
  + "    stack->length *= 2;\n"
  + "    free(stack->data);\n"
  + "    stack->data = tmp;\n"
  + "  } \n"
  + "  stack->data[(stack->current_index)++] = elem;\n"
  + "}\n";

}
