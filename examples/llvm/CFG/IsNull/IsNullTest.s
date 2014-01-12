	.file	"IsNullTest.bc"
	.text
	.globl	main
	.align	16, 0x90
	.type	main,@function
main:                                   # @main
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp2:
	.cfi_def_cfa_offset 16
.Ltmp3:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp4:
	.cfi_def_cfa_register %rbp
	subq	$64, %rsp
	movl	$0, -4(%rbp)
	movl	$0, -56(%rbp)
	movl	$4, %edi
	callq	malloc
	movq	%rax, -48(%rbp)
	movl	$1, (%rax)
	movl	$4, %edi
	callq	malloc
	movq	%rax, -40(%rbp)
	movl	$2, (%rax)
	movl	$4, %edi
	callq	malloc
	movq	%rax, -32(%rbp)
	movl	$4, (%rax)
	movl	$4, %edi
	callq	malloc
	movq	%rax, -24(%rbp)
	movl	$8, (%rax)
	movl	$0, -52(%rbp)
	jmp	.LBB0_1
	.align	16, 0x90
.LBB0_2:                                # %for.inc
                                        #   in Loop: Header=BB0_1 Depth=1
	callq	__RVC_IsNull_deref
	movslq	-52(%rbp), %rax
	movq	-48(%rbp,%rax,8), %rax
	movl	(%rax), %eax
	addl	%eax, -56(%rbp)
	incl	-52(%rbp)
.LBB0_1:                                # %for.cond
                                        # =>This Inner Loop Header: Depth=1
	cmpl	$3, -52(%rbp)
	jle	.LBB0_2
# BB#3:                                 # %for.end
	movl	-56(%rbp), %esi
	movl	$.L.str, %edi
	xorl	%eax, %eax
	callq	printf
	movl	-4(%rbp), %eax
	addq	$64, %rsp
	popq	%rbp
	ret
.Ltmp5:
	.size	main, .Ltmp5-main
	.cfi_endproc

	.globl	__RVC_IsNull_reset
	.align	16, 0x90
	.type	__RVC_IsNull_reset,@function
__RVC_IsNull_reset:                     # @__RVC_IsNull_reset
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp8:
	.cfi_def_cfa_offset 16
.Ltmp9:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp10:
	.cfi_def_cfa_register %rbp
	subq	$16, %rsp
	movq	__RV_stacks_inst(%rip), %rdi
	callq	__RV_clear
	movl	$10, %edi
	callq	__RV_new_RV_stack
	movq	%rax, -8(%rbp)
	movl	$-2, %esi
	movq	%rax, %rdi
	callq	__RV_push
	movq	-8(%rbp), %rdi
	xorl	%esi, %esi
	callq	__RV_push
	movq	__RV_stacks_inst(%rip), %rdi
	movq	-8(%rbp), %rsi
	callq	__RV_add
	addq	$16, %rsp
	popq	%rbp
	ret
.Ltmp11:
	.size	__RVC_IsNull_reset, .Ltmp11-__RVC_IsNull_reset
	.cfi_endproc

	.globl	__RV_init
	.align	16, 0x90
	.type	__RV_init,@function
__RV_init:                              # @__RV_init
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp14:
	.cfi_def_cfa_offset 16
.Ltmp15:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp16:
	.cfi_def_cfa_register %rbp
	subq	$16, %rsp
	movl	$10, %edi
	callq	__RV_new_RV_stack
	movq	%rax, -8(%rbp)
	movl	$-2, %esi
	movq	%rax, %rdi
	callq	__RV_push
	movq	-8(%rbp), %rdi
	xorl	%esi, %esi
	callq	__RV_push
	movq	__RV_stacks_inst(%rip), %rdi
	movq	-8(%rbp), %rsi
	callq	__RV_add
	addq	$16, %rsp
	popq	%rbp
	ret
.Ltmp17:
	.size	__RV_init, .Ltmp17-__RV_init
	.cfi_endproc

	.globl	__RVC_IsNull_deref
	.align	16, 0x90
	.type	__RVC_IsNull_deref,@function
__RVC_IsNull_deref:                     # @__RVC_IsNull_deref
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp20:
	.cfi_def_cfa_offset 16
.Ltmp21:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp22:
	.cfi_def_cfa_register %rbp
	cmpq	$0, __RV_stacks_inst(%rip)
	jne	.LBB3_2
# BB#1:                                 # %if.then
	movl	$5, %edi
	callq	__RV_new_RV_stacks
	movq	%rax, __RV_stacks_inst(%rip)
	callq	__RV_init
.LBB3_2:                                # %if.end
	movl	$1, %edi
	callq	monitor
	cmpl	$0, __RV_cat(%rip)
	sete	%al
	movzbl	%al, %eax
	movl	%eax, __RVC_IsNull_match(%rip)
	cmpl	$2, __RV_cat(%rip)
	sete	%al
	movzbl	%al, %eax
	movl	%eax, __RVC_IsNull_fail(%rip)
	cmpl	$0, __RVC_IsNull_match(%rip)
	je	.LBB3_4
# BB#3:                                 # %if.then4
	movq	stderr(%rip), %rdi
	movl	$.L.str1, %esi
	xorl	%eax, %eax
	callq	fprintf
	callq	__RVC_IsNull_reset
.LBB3_4:                                # %if.end6
	popq	%rbp
	ret
.Ltmp23:
	.size	__RVC_IsNull_deref, .Ltmp23-__RVC_IsNull_deref
	.cfi_endproc

	.globl	__RVC_IsNull_isNull
	.align	16, 0x90
	.type	__RVC_IsNull_isNull,@function
__RVC_IsNull_isNull:                    # @__RVC_IsNull_isNull
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp26:
	.cfi_def_cfa_offset 16
.Ltmp27:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp28:
	.cfi_def_cfa_register %rbp
	cmpq	$0, __RV_stacks_inst(%rip)
	jne	.LBB4_2
# BB#1:                                 # %if.then
	movl	$5, %edi
	callq	__RV_new_RV_stacks
	movq	%rax, __RV_stacks_inst(%rip)
	callq	__RV_init
.LBB4_2:                                # %if.end
	movl	$2, %edi
	callq	monitor
	cmpl	$0, __RV_cat(%rip)
	sete	%al
	movzbl	%al, %eax
	movl	%eax, __RVC_IsNull_match(%rip)
	cmpl	$2, __RV_cat(%rip)
	sete	%al
	movzbl	%al, %eax
	movl	%eax, __RVC_IsNull_fail(%rip)
	cmpl	$0, __RVC_IsNull_match(%rip)
	je	.LBB4_4
# BB#3:                                 # %if.then4
	movq	stderr(%rip), %rdi
	movl	$.L.str1, %esi
	xorl	%eax, %eax
	callq	fprintf
	callq	__RVC_IsNull_reset
.LBB4_4:                                # %if.end6
	popq	%rbp
	ret
.Ltmp29:
	.size	__RVC_IsNull_isNull, .Ltmp29-__RVC_IsNull_isNull
	.cfi_endproc

	.align	16, 0x90
	.type	__RV_clear,@function
__RV_clear:                             # @__RV_clear
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp32:
	.cfi_def_cfa_offset 16
.Ltmp33:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp34:
	.cfi_def_cfa_register %rbp
	subq	$16, %rsp
	testq	%rdi, %rdi
	movq	%rdi, -8(%rbp)
	je	.LBB5_5
# BB#1:                                 # %if.end
	movl	$0, -12(%rbp)
	jmp	.LBB5_2
	.align	16, 0x90
.LBB5_3:                                # %for.inc
                                        #   in Loop: Header=BB5_2 Depth=1
	movslq	-12(%rbp), %rax
	movq	-8(%rbp), %rcx
	movq	8(%rcx), %rcx
	movq	(%rcx,%rax,8), %rdi
	callq	__RV_delete_RV_stack
	incl	-12(%rbp)
.LBB5_2:                                # %for.cond
                                        # =>This Inner Loop Header: Depth=1
	movl	-12(%rbp), %eax
	movq	-8(%rbp), %rcx
	cmpl	(%rcx), %eax
	jl	.LBB5_3
# BB#4:                                 # %for.end
	movq	-8(%rbp), %rax
	movl	$0, (%rax)
.LBB5_5:                                # %return
	addq	$16, %rsp
	popq	%rbp
	ret
.Ltmp35:
	.size	__RV_clear, .Ltmp35-__RV_clear
	.cfi_endproc

	.align	16, 0x90
	.type	__RV_new_RV_stack,@function
__RV_new_RV_stack:                      # @__RV_new_RV_stack
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp38:
	.cfi_def_cfa_offset 16
.Ltmp39:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp40:
	.cfi_def_cfa_register %rbp
	subq	$16, %rsp
	movl	%edi, -4(%rbp)
	movl	$16, %edi
	callq	malloc
	movq	%rax, -16(%rbp)
	movl	$0, (%rax)
	movl	-4(%rbp), %eax
	movq	-16(%rbp), %rcx
	movl	%eax, 4(%rcx)
	movslq	-4(%rbp), %rdi
	shlq	$2, %rdi
	callq	malloc
	movq	-16(%rbp), %rcx
	movq	%rax, 8(%rcx)
	movq	-16(%rbp), %rax
	addq	$16, %rsp
	popq	%rbp
	ret
.Ltmp41:
	.size	__RV_new_RV_stack, .Ltmp41-__RV_new_RV_stack
	.cfi_endproc

	.align	16, 0x90
	.type	__RV_push,@function
__RV_push:                              # @__RV_push
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp44:
	.cfi_def_cfa_offset 16
.Ltmp45:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp46:
	.cfi_def_cfa_register %rbp
	subq	$32, %rsp
	movq	%rdi, -8(%rbp)
	movl	%esi, -12(%rbp)
	movq	-8(%rbp), %rax
	movl	(%rax), %ecx
	cmpl	4(%rax), %ecx
	jl	.LBB7_5
# BB#1:                                 # %if.then
	movq	-8(%rbp), %rax
	movslq	4(%rax), %rdi
	shlq	$3, %rdi
	callq	malloc
	movq	%rax, -24(%rbp)
	movl	$0, -16(%rbp)
	jmp	.LBB7_2
	.align	16, 0x90
.LBB7_3:                                # %for.inc
                                        #   in Loop: Header=BB7_2 Depth=1
	movslq	-16(%rbp), %rax
	movq	-8(%rbp), %rcx
	movq	8(%rcx), %rcx
	movl	(%rcx,%rax,4), %ecx
	movq	-24(%rbp), %rdx
	movl	%ecx, (%rdx,%rax,4)
	incl	-16(%rbp)
.LBB7_2:                                # %for.cond
                                        # =>This Inner Loop Header: Depth=1
	movl	-16(%rbp), %eax
	movq	-8(%rbp), %rcx
	cmpl	4(%rcx), %eax
	jl	.LBB7_3
# BB#4:                                 # %for.end
	movq	-8(%rbp), %rax
	shll	4(%rax)
	movq	-8(%rbp), %rax
	movq	8(%rax), %rdi
	callq	free
	movq	-24(%rbp), %rax
	movq	-8(%rbp), %rcx
	movq	%rax, 8(%rcx)
.LBB7_5:                                # %if.end
	movl	-12(%rbp), %eax
	movq	-8(%rbp), %rcx
	movslq	(%rcx), %rdx
	leal	1(%rdx), %esi
	movl	%esi, (%rcx)
	movq	-8(%rbp), %rcx
	movq	8(%rcx), %rcx
	movl	%eax, (%rcx,%rdx,4)
	addq	$32, %rsp
	popq	%rbp
	ret
.Ltmp47:
	.size	__RV_push, .Ltmp47-__RV_push
	.cfi_endproc

	.align	16, 0x90
	.type	__RV_add,@function
__RV_add:                               # @__RV_add
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp50:
	.cfi_def_cfa_offset 16
.Ltmp51:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp52:
	.cfi_def_cfa_register %rbp
	subq	$32, %rsp
	movq	%rdi, -8(%rbp)
	movq	%rsi, -16(%rbp)
	movq	-8(%rbp), %rax
	movl	(%rax), %ecx
	cmpl	4(%rax), %ecx
	jl	.LBB8_5
# BB#1:                                 # %if.then
	movq	-8(%rbp), %rax
	movslq	4(%rax), %rdi
	shlq	$4, %rdi
	callq	malloc
	movq	%rax, -32(%rbp)
	movl	$0, -20(%rbp)
	jmp	.LBB8_2
	.align	16, 0x90
.LBB8_3:                                # %for.inc
                                        #   in Loop: Header=BB8_2 Depth=1
	movslq	-20(%rbp), %rax
	movq	-8(%rbp), %rcx
	movq	8(%rcx), %rcx
	movq	(%rcx,%rax,8), %rcx
	movq	-32(%rbp), %rdx
	movq	%rcx, (%rdx,%rax,8)
	incl	-20(%rbp)
.LBB8_2:                                # %for.cond
                                        # =>This Inner Loop Header: Depth=1
	movl	-20(%rbp), %eax
	movq	-8(%rbp), %rcx
	cmpl	4(%rcx), %eax
	jl	.LBB8_3
# BB#4:                                 # %for.end
	movq	-8(%rbp), %rax
	shll	4(%rax)
	movq	-8(%rbp), %rax
	movq	8(%rax), %rdi
	callq	free
	movq	-32(%rbp), %rax
	movq	-8(%rbp), %rcx
	movq	%rax, 8(%rcx)
.LBB8_5:                                # %if.end
	movq	-16(%rbp), %rax
	movq	-8(%rbp), %rcx
	movslq	(%rcx), %rdx
	leal	1(%rdx), %esi
	movl	%esi, (%rcx)
	movq	-8(%rbp), %rcx
	movq	8(%rcx), %rcx
	movq	%rax, (%rcx,%rdx,8)
	addq	$32, %rsp
	popq	%rbp
	ret
.Ltmp53:
	.size	__RV_add, .Ltmp53-__RV_add
	.cfi_endproc

	.align	16, 0x90
	.type	__RV_new_RV_stacks,@function
__RV_new_RV_stacks:                     # @__RV_new_RV_stacks
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp56:
	.cfi_def_cfa_offset 16
.Ltmp57:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp58:
	.cfi_def_cfa_register %rbp
	subq	$16, %rsp
	movl	%edi, -4(%rbp)
	movl	$16, %edi
	callq	malloc
	movq	%rax, -16(%rbp)
	movl	$0, (%rax)
	movl	-4(%rbp), %eax
	movq	-16(%rbp), %rcx
	movl	%eax, 4(%rcx)
	movslq	-4(%rbp), %rdi
	shlq	$3, %rdi
	callq	malloc
	movq	-16(%rbp), %rcx
	movq	%rax, 8(%rcx)
	movq	-16(%rbp), %rax
	addq	$16, %rsp
	popq	%rbp
	ret
.Ltmp59:
	.size	__RV_new_RV_stacks, .Ltmp59-__RV_new_RV_stacks
	.cfi_endproc

	.align	16, 0x90
	.type	monitor,@function
monitor:                                # @monitor
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp62:
	.cfi_def_cfa_offset 16
.Ltmp63:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp64:
	.cfi_def_cfa_register %rbp
	subq	$32, %rsp
	movl	%edi, -4(%rbp)
	cmpl	$2, __RV_cat(%rip)
	je	.LBB10_11
# BB#1:                                 # %if.then
	decl	-4(%rbp)
	movl	$1, __RV_cat(%rip)
	movq	__RV_stacks_inst(%rip), %rdi
	xorl	%esi, %esi
	callq	__RV_get
	movq	%rax, -32(%rbp)
	jmp	.LBB10_2
	.align	16, 0x90
.LBB10_9:                               # %sw.bb31
                                        #   in Loop: Header=BB10_2 Depth=1
	movq	-32(%rbp), %rdi
	movslq	-4(%rbp), %rax
	movslq	-20(%rbp), %rcx
	leaq	(%rcx,%rcx,2), %rcx
	shlq	$4, %rcx
	leaq	(%rax,%rax,2), %rax
	movl	__RV_at+8(%rcx,%rax,8), %esi
	callq	__RV_pop_n
	movq	-32(%rbp), %rdi
	callq	__RV_peek
	movl	%eax, -16(%rbp)
	movq	-32(%rbp), %rdi
	movslq	-4(%rbp), %rax
	movslq	-20(%rbp), %rcx
	leaq	(%rcx,%rcx,2), %rcx
	shlq	$4, %rcx
	leaq	(%rax,%rax,2), %rax
	movslq	__RV_at+4(%rcx,%rax,8), %rax
	movslq	-16(%rbp), %rcx
	leaq	(%rcx,%rcx,2), %rcx
	shlq	$2, %rax
	movl	__RV_gt(%rax,%rcx,4), %esi
	callq	__RV_push
.LBB10_2:                               # %while.cond
                                        # =>This Inner Loop Header: Depth=1
	movq	-32(%rbp), %rax
	cmpl	$0, (%rax)
	js	.LBB10_11
# BB#3:                                 # %while.body
                                        #   in Loop: Header=BB10_2 Depth=1
	movq	-32(%rbp), %rdi
	callq	__RV_peek
	testl	%eax, %eax
	movl	%eax, -20(%rbp)
	js	.LBB10_10
# BB#4:                                 # %land.lhs.true
                                        #   in Loop: Header=BB10_2 Depth=1
	movslq	-4(%rbp), %rax
	movslq	-20(%rbp), %rcx
	leaq	(%rcx,%rcx,2), %rcx
	shlq	$4, %rcx
	leaq	(%rax,%rax,2), %rax
	cmpl	$0, __RV_at(%rcx,%rax,8)
	je	.LBB10_10
# BB#5:                                 # %if.then9
                                        #   in Loop: Header=BB10_2 Depth=1
	movslq	-4(%rbp), %rax
	movslq	-20(%rbp), %rcx
	leaq	(%rcx,%rcx,2), %rcx
	shlq	$4, %rcx
	leaq	(%rax,%rax,2), %rax
	movl	__RV_at(%rcx,%rax,8), %eax
	cmpl	$2, %eax
	je	.LBB10_9
# BB#6:                                 # %if.then9
                                        #   in Loop: Header=BB10_2 Depth=1
	cmpl	$1, %eax
	jne	.LBB10_2
# BB#7:                                 # %sw.bb
	movq	-32(%rbp), %rdi
	movslq	-4(%rbp), %rax
	movslq	-20(%rbp), %rcx
	leaq	(%rcx,%rcx,2), %rcx
	shlq	$4, %rcx
	leaq	(%rax,%rax,2), %rax
	movl	__RV_at+4(%rcx,%rax,8), %esi
	callq	__RV_push
	movslq	-4(%rbp), %rax
	movslq	-20(%rbp), %rcx
	leaq	(%rcx,%rcx,2), %rcx
	shlq	$4, %rcx
	leaq	(%rax,%rax,2), %rax
	movslq	__RV_at+4(%rcx,%rax,8), %rax
	cmpl	$0, __RV_acc(,%rax,4)
	je	.LBB10_11
# BB#8:                                 # %if.then30
	movl	$0, __RV_cat(%rip)
	jmp	.LBB10_11
.LBB10_10:                              # %if.else
	movl	$2, __RV_cat(%rip)
.LBB10_11:                              # %if.end50
	addq	$32, %rsp
	popq	%rbp
	ret
.Ltmp65:
	.size	monitor, .Ltmp65-monitor
	.cfi_endproc

	.align	16, 0x90
	.type	__RV_delete_RV_stack,@function
__RV_delete_RV_stack:                   # @__RV_delete_RV_stack
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp68:
	.cfi_def_cfa_offset 16
.Ltmp69:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp70:
	.cfi_def_cfa_register %rbp
	subq	$16, %rsp
	testq	%rdi, %rdi
	movq	%rdi, -8(%rbp)
	je	.LBB11_2
# BB#1:                                 # %if.end
	movq	-8(%rbp), %rax
	movq	8(%rax), %rdi
	callq	free
	movq	-8(%rbp), %rdi
	callq	free
.LBB11_2:                               # %return
	addq	$16, %rsp
	popq	%rbp
	ret
.Ltmp71:
	.size	__RV_delete_RV_stack, .Ltmp71-__RV_delete_RV_stack
	.cfi_endproc

	.align	16, 0x90
	.type	__RV_get,@function
__RV_get:                               # @__RV_get
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp74:
	.cfi_def_cfa_offset 16
.Ltmp75:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp76:
	.cfi_def_cfa_register %rbp
	movq	%rdi, -8(%rbp)
	movl	%esi, -12(%rbp)
	movslq	-12(%rbp), %rax
	movq	-8(%rbp), %rcx
	movq	8(%rcx), %rcx
	movq	(%rcx,%rax,8), %rax
	popq	%rbp
	ret
.Ltmp77:
	.size	__RV_get, .Ltmp77-__RV_get
	.cfi_endproc

	.align	16, 0x90
	.type	__RV_peek,@function
__RV_peek:                              # @__RV_peek
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp80:
	.cfi_def_cfa_offset 16
.Ltmp81:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp82:
	.cfi_def_cfa_register %rbp
	movq	%rdi, -8(%rbp)
	movl	(%rdi), %eax
	decl	%eax
	cltq
	movq	8(%rdi), %rcx
	movl	(%rcx,%rax,4), %eax
	popq	%rbp
	ret
.Ltmp83:
	.size	__RV_peek, .Ltmp83-__RV_peek
	.cfi_endproc

	.align	16, 0x90
	.type	__RV_pop_n,@function
__RV_pop_n:                             # @__RV_pop_n
	.cfi_startproc
# BB#0:                                 # %entry
	pushq	%rbp
.Ltmp86:
	.cfi_def_cfa_offset 16
.Ltmp87:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp88:
	.cfi_def_cfa_register %rbp
	movq	%rdi, -8(%rbp)
	movl	%esi, -12(%rbp)
	movq	-8(%rbp), %rax
	subl	%esi, (%rax)
	popq	%rbp
	ret
.Ltmp89:
	.size	__RV_pop_n, .Ltmp89-__RV_pop_n
	.cfi_endproc

	.type	.L.str,@object          # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"sum: %d\n"
	.size	.L.str, 9

	.type	__RV_stacks_inst,@object # @__RV_stacks_inst
	.bss
	.globl	__RV_stacks_inst
	.align	8
__RV_stacks_inst:
	.quad	0
	.size	__RV_stacks_inst, 8

	.type	__RVC_IsNull_match,@object # @__RVC_IsNull_match
	.globl	__RVC_IsNull_match
	.align	4
__RVC_IsNull_match:
	.long	0                       # 0x0
	.size	__RVC_IsNull_match, 4

	.type	__RVC_IsNull_fail,@object # @__RVC_IsNull_fail
	.globl	__RVC_IsNull_fail
	.align	4
__RVC_IsNull_fail:
	.long	0                       # 0x0
	.size	__RVC_IsNull_fail, 4

	.type	__RV_cat,@object        # @__RV_cat
	.local	__RV_cat
	.comm	__RV_cat,4,4
	.type	.L.str1,@object         # @.str1
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str1:
	.asciz	"isNull not called before deref!\n"
	.size	.L.str1, 33

	.type	__RV_at,@object         # @__RV_at
	.data
	.align	16
__RV_at:
	.long	1                       # 0x1
	.long	7                       # 0x7
	.long	0                       # 0x0
	.zero	12
	.long	1                       # 0x1
	.long	6                       # 0x6
	.long	0                       # 0x0
	.zero	12
	.long	2                       # 0x2
	.long	1                       # 0x1
	.long	3                       # 0x3
	.zero	12
	.long	2                       # 0x2
	.long	1                       # 0x1
	.long	3                       # 0x3
	.zero	12
	.zero	48
	.zero	48
	.long	1                       # 0x1
	.long	2                       # 0x2
	.long	0                       # 0x0
	.zero	12
	.zero	24
	.long	1                       # 0x1
	.long	4                       # 0x4
	.long	0                       # 0x0
	.zero	12
	.long	1                       # 0x1
	.long	8                       # 0x8
	.long	0                       # 0x0
	.zero	12
	.long	2                       # 0x2
	.long	1                       # 0x1
	.long	1                       # 0x1
	.long	1                       # 0x1
	.long	9                       # 0x9
	.long	0                       # 0x0
	.long	2                       # 0x2
	.long	1                       # 0x1
	.long	1                       # 0x1
	.zero	12
	.long	1                       # 0x1
	.long	3                       # 0x3
	.long	0                       # 0x0
	.zero	12
	.zero	24
	.long	1                       # 0x1
	.long	1                       # 0x1
	.long	0                       # 0x0
	.long	2                       # 0x2
	.long	1                       # 0x1
	.long	2                       # 0x2
	.long	2                       # 0x2
	.long	1                       # 0x1
	.long	2                       # 0x2
	.zero	12
	.long	2                       # 0x2
	.long	1                       # 0x1
	.long	2                       # 0x2
	.zero	12
	.long	2                       # 0x2
	.long	1                       # 0x1
	.long	2                       # 0x2
	.zero	12
	.zero	48
	.size	__RV_at, 528

	.type	__RV_acc,@object        # @__RV_acc
	.align	16
__RV_acc:
	.long	0                       # 0x0
	.long	0                       # 0x0
	.long	1                       # 0x1
	.long	1                       # 0x1
	.long	0                       # 0x0
	.long	0                       # 0x0
	.long	0                       # 0x0
	.long	0                       # 0x0
	.long	0                       # 0x0
	.long	0                       # 0x0
	.long	1                       # 0x1
	.size	__RV_acc, 44

	.type	__RV_gt,@object         # @__RV_gt
	.align	16
__RV_gt:
	.long	0                       # 0x0
	.long	5                       # 0x5
	.long	10                      # 0xa
	.long	0                       # 0x0
	.long	4294967295              # 0xffffffff
	.long	4294967295              # 0xffffffff
	.long	0                       # 0x0
	.long	4294967295              # 0xffffffff
	.long	4294967295              # 0xffffffff
	.long	0                       # 0x0
	.long	4294967295              # 0xffffffff
	.long	4294967295              # 0xffffffff
	.long	0                       # 0x0
	.long	4294967295              # 0xffffffff
	.long	4294967295              # 0xffffffff
	.long	0                       # 0x0
	.long	4294967295              # 0xffffffff
	.long	4294967295              # 0xffffffff
	.long	0                       # 0x0
	.long	4294967295              # 0xffffffff
	.long	4294967295              # 0xffffffff
	.long	0                       # 0x0
	.long	4294967295              # 0xffffffff
	.long	4294967295              # 0xffffffff
	.long	0                       # 0x0
	.long	4294967295              # 0xffffffff
	.long	4294967295              # 0xffffffff
	.long	0                       # 0x0
	.long	4294967295              # 0xffffffff
	.long	4294967295              # 0xffffffff
	.long	0                       # 0x0
	.long	4294967295              # 0xffffffff
	.long	4294967295              # 0xffffffff
	.size	__RV_gt, 132


	.ident	"clang version 3.4 (trunk 194993)"
	.ident	"clang version 3.4 (trunk 194993)"
	.section	".note.GNU-stack","",@progbits
