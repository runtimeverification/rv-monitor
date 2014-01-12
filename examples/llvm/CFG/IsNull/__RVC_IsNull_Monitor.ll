; ModuleID = '__RVC_IsNull_Monitor.bc'
target datalayout = "e-p:64:64:64-i1:8:8-i8:8:8-i16:16:16-i32:32:32-i64:64:64-f32:32:32-f64:64:64-v64:64:64-v128:128:128-a0:0:64-s0:64:64-f80:128:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"

%struct.stacks = type { i32, i32, %struct.stack** }
%struct.stack = type { i32, i32, i32* }
%struct._IO_FILE = type { i32, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, %struct._IO_marker*, %struct._IO_FILE*, i32, i32, i64, i16, i8, [1 x i8], i8*, i64, i8*, i8*, i8*, i8*, i64, i32, [20 x i8] }
%struct._IO_marker = type { %struct._IO_marker*, %struct._IO_FILE*, i32 }

@__RV_stacks_inst = global %struct.stacks* null, align 8
@__RVC_IsNull_match = global i32 0, align 4
@__RVC_IsNull_fail = global i32 0, align 4
@__RV_cat = internal global i32 0, align 4
@stderr = external global %struct._IO_FILE*
@.str = private unnamed_addr constant [33 x i8] c"isNull not called before deref!\0A\00", align 1
@__RV_at = internal global [11 x [2 x [2 x [3 x i32]]]] [[2 x [2 x [3 x i32]]] [[2 x [3 x i32]] [[3 x i32] [i32 1, i32 7, i32 0], [3 x i32] zeroinitializer], [2 x [3 x i32]] [[3 x i32] [i32 1, i32 6, i32 0], [3 x i32] zeroinitializer]], [2 x [2 x [3 x i32]]] [[2 x [3 x i32]] [[3 x i32] [i32 2, i32 1, i32 3], [3 x i32] zeroinitializer], [2 x [3 x i32]] [[3 x i32] [i32 2, i32 1, i32 3], [3 x i32] zeroinitializer]], [2 x [2 x [3 x i32]]] zeroinitializer, [2 x [2 x [3 x i32]]] zeroinitializer, [2 x [2 x [3 x i32]]] [[2 x [3 x i32]] [[3 x i32] [i32 1, i32 2, i32 0], [3 x i32] zeroinitializer], [2 x [3 x i32]] zeroinitializer], [2 x [2 x [3 x i32]]] [[2 x [3 x i32]] [[3 x i32] [i32 1, i32 4, i32 0], [3 x i32] zeroinitializer], [2 x [3 x i32]] [[3 x i32] [i32 1, i32 8, i32 0], [3 x i32] zeroinitializer]], [2 x [2 x [3 x i32]]] [[2 x [3 x i32]] [[3 x i32] [i32 2, i32 1, i32 1], [3 x i32] [i32 1, i32 9, i32 0]], [2 x [3 x i32]] [[3 x i32] [i32 2, i32 1, i32 1], [3 x i32] zeroinitializer]], [2 x [2 x [3 x i32]]] [[2 x [3 x i32]] [[3 x i32] [i32 1, i32 3, i32 0], [3 x i32] zeroinitializer], [2 x [3 x i32]] zeroinitializer], [2 x [2 x [3 x i32]]] [[2 x [3 x i32]] [[3 x i32] [i32 1, i32 1, i32 0], [3 x i32] [i32 2, i32 1, i32 2]], [2 x [3 x i32]] [[3 x i32] [i32 2, i32 1, i32 2], [3 x i32] zeroinitializer]], [2 x [2 x [3 x i32]]] [[2 x [3 x i32]] [[3 x i32] [i32 2, i32 1, i32 2], [3 x i32] zeroinitializer], [2 x [3 x i32]] [[3 x i32] [i32 2, i32 1, i32 2], [3 x i32] zeroinitializer]], [2 x [2 x [3 x i32]]] zeroinitializer], align 16
@__RV_acc = internal global [11 x i32] [i32 0, i32 0, i32 1, i32 1, i32 0, i32 0, i32 0, i32 0, i32 0, i32 0, i32 1], align 16
@__RV_gt = internal global [11 x [3 x i32]] [[3 x i32] [i32 0, i32 5, i32 10], [3 x i32] [i32 0, i32 -1, i32 -1], [3 x i32] [i32 0, i32 -1, i32 -1], [3 x i32] [i32 0, i32 -1, i32 -1], [3 x i32] [i32 0, i32 -1, i32 -1], [3 x i32] [i32 0, i32 -1, i32 -1], [3 x i32] [i32 0, i32 -1, i32 -1], [3 x i32] [i32 0, i32 -1, i32 -1], [3 x i32] [i32 0, i32 -1, i32 -1], [3 x i32] [i32 0, i32 -1, i32 -1], [3 x i32] [i32 0, i32 -1, i32 -1]], align 16

; Function Attrs: nounwind uwtable
define void @__RVC_IsNull_reset() #0 {
entry:
  %stack = alloca %struct.stack*, align 8
  %0 = load %struct.stacks** @__RV_stacks_inst, align 8
  call void @__RV_clear(%struct.stacks* %0)
  %call = call %struct.stack* @__RV_new_RV_stack(i32 10)
  store %struct.stack* %call, %struct.stack** %stack, align 8
  %1 = load %struct.stack** %stack, align 8
  call void @__RV_push(%struct.stack* %1, i32 -2)
  %2 = load %struct.stack** %stack, align 8
  call void @__RV_push(%struct.stack* %2, i32 0)
  %3 = load %struct.stacks** @__RV_stacks_inst, align 8
  %4 = load %struct.stack** %stack, align 8
  call void @__RV_add(%struct.stacks* %3, %struct.stack* %4)
  ret void
}

; Function Attrs: nounwind uwtable
define internal void @__RV_clear(%struct.stacks* %stacks) #0 {
entry:
  %stacks.addr = alloca %struct.stacks*, align 8
  %i = alloca i32, align 4
  store %struct.stacks* %stacks, %struct.stacks** %stacks.addr, align 8
  %0 = load %struct.stacks** %stacks.addr, align 8
  %cmp = icmp eq %struct.stacks* %0, null
  br i1 %cmp, label %if.then, label %if.end

if.then:                                          ; preds = %entry
  br label %return

if.end:                                           ; preds = %entry
  store i32 0, i32* %i, align 4
  br label %for.cond

for.cond:                                         ; preds = %for.inc, %if.end
  %1 = load i32* %i, align 4
  %2 = load %struct.stacks** %stacks.addr, align 8
  %current_index = getelementptr inbounds %struct.stacks* %2, i32 0, i32 0
  %3 = load i32* %current_index, align 4
  %cmp1 = icmp slt i32 %1, %3
  br i1 %cmp1, label %for.body, label %for.end

for.body:                                         ; preds = %for.cond
  %4 = load i32* %i, align 4
  %idxprom = sext i32 %4 to i64
  %5 = load %struct.stacks** %stacks.addr, align 8
  %data = getelementptr inbounds %struct.stacks* %5, i32 0, i32 2
  %6 = load %struct.stack*** %data, align 8
  %arrayidx = getelementptr inbounds %struct.stack** %6, i64 %idxprom
  %7 = load %struct.stack** %arrayidx, align 8
  call void @__RV_delete_RV_stack(%struct.stack* %7)
  br label %for.inc

for.inc:                                          ; preds = %for.body
  %8 = load i32* %i, align 4
  %inc = add nsw i32 %8, 1
  store i32 %inc, i32* %i, align 4
  br label %for.cond

for.end:                                          ; preds = %for.cond
  %9 = load %struct.stacks** %stacks.addr, align 8
  %current_index2 = getelementptr inbounds %struct.stacks* %9, i32 0, i32 0
  store i32 0, i32* %current_index2, align 4
  br label %return

return:                                           ; preds = %for.end, %if.then
  ret void
}

; Function Attrs: nounwind uwtable
define internal %struct.stack* @__RV_new_RV_stack(i32 %size) #0 {
entry:
  %size.addr = alloca i32, align 4
  %ret = alloca %struct.stack*, align 8
  store i32 %size, i32* %size.addr, align 4
  %call = call noalias i8* @malloc(i64 16) #3
  %0 = bitcast i8* %call to %struct.stack*
  store %struct.stack* %0, %struct.stack** %ret, align 8
  %1 = load %struct.stack** %ret, align 8
  %current_index = getelementptr inbounds %struct.stack* %1, i32 0, i32 0
  store i32 0, i32* %current_index, align 4
  %2 = load i32* %size.addr, align 4
  %3 = load %struct.stack** %ret, align 8
  %length = getelementptr inbounds %struct.stack* %3, i32 0, i32 1
  store i32 %2, i32* %length, align 4
  %4 = load i32* %size.addr, align 4
  %conv = sext i32 %4 to i64
  %mul = mul i64 4, %conv
  %call1 = call noalias i8* @malloc(i64 %mul) #3
  %5 = bitcast i8* %call1 to i32*
  %6 = load %struct.stack** %ret, align 8
  %data = getelementptr inbounds %struct.stack* %6, i32 0, i32 2
  store i32* %5, i32** %data, align 8
  %7 = load %struct.stack** %ret, align 8
  ret %struct.stack* %7
}

; Function Attrs: nounwind uwtable
define internal void @__RV_push(%struct.stack* %stack, i32 %elem) #0 {
entry:
  %stack.addr = alloca %struct.stack*, align 8
  %elem.addr = alloca i32, align 4
  %i = alloca i32, align 4
  %tmp = alloca i32*, align 8
  store %struct.stack* %stack, %struct.stack** %stack.addr, align 8
  store i32 %elem, i32* %elem.addr, align 4
  %0 = load %struct.stack** %stack.addr, align 8
  %current_index = getelementptr inbounds %struct.stack* %0, i32 0, i32 0
  %1 = load i32* %current_index, align 4
  %2 = load %struct.stack** %stack.addr, align 8
  %length = getelementptr inbounds %struct.stack* %2, i32 0, i32 1
  %3 = load i32* %length, align 4
  %cmp = icmp sge i32 %1, %3
  br i1 %cmp, label %if.then, label %if.end

if.then:                                          ; preds = %entry
  %4 = load %struct.stack** %stack.addr, align 8
  %length1 = getelementptr inbounds %struct.stack* %4, i32 0, i32 1
  %5 = load i32* %length1, align 4
  %conv = sext i32 %5 to i64
  %mul = mul i64 4, %conv
  %mul2 = mul i64 %mul, 2
  %call = call noalias i8* @malloc(i64 %mul2) #3
  %6 = bitcast i8* %call to i32*
  store i32* %6, i32** %tmp, align 8
  store i32 0, i32* %i, align 4
  br label %for.cond

for.cond:                                         ; preds = %for.inc, %if.then
  %7 = load i32* %i, align 4
  %8 = load %struct.stack** %stack.addr, align 8
  %length3 = getelementptr inbounds %struct.stack* %8, i32 0, i32 1
  %9 = load i32* %length3, align 4
  %cmp4 = icmp slt i32 %7, %9
  br i1 %cmp4, label %for.body, label %for.end

for.body:                                         ; preds = %for.cond
  %10 = load i32* %i, align 4
  %idxprom = sext i32 %10 to i64
  %11 = load %struct.stack** %stack.addr, align 8
  %data = getelementptr inbounds %struct.stack* %11, i32 0, i32 2
  %12 = load i32** %data, align 8
  %arrayidx = getelementptr inbounds i32* %12, i64 %idxprom
  %13 = load i32* %arrayidx, align 4
  %14 = load i32* %i, align 4
  %idxprom6 = sext i32 %14 to i64
  %15 = load i32** %tmp, align 8
  %arrayidx7 = getelementptr inbounds i32* %15, i64 %idxprom6
  store i32 %13, i32* %arrayidx7, align 4
  br label %for.inc

for.inc:                                          ; preds = %for.body
  %16 = load i32* %i, align 4
  %inc = add nsw i32 %16, 1
  store i32 %inc, i32* %i, align 4
  br label %for.cond

for.end:                                          ; preds = %for.cond
  %17 = load %struct.stack** %stack.addr, align 8
  %length8 = getelementptr inbounds %struct.stack* %17, i32 0, i32 1
  %18 = load i32* %length8, align 4
  %mul9 = mul nsw i32 %18, 2
  store i32 %mul9, i32* %length8, align 4
  %19 = load %struct.stack** %stack.addr, align 8
  %data10 = getelementptr inbounds %struct.stack* %19, i32 0, i32 2
  %20 = load i32** %data10, align 8
  %21 = bitcast i32* %20 to i8*
  call void @free(i8* %21) #3
  %22 = load i32** %tmp, align 8
  %23 = load %struct.stack** %stack.addr, align 8
  %data11 = getelementptr inbounds %struct.stack* %23, i32 0, i32 2
  store i32* %22, i32** %data11, align 8
  br label %if.end

if.end:                                           ; preds = %for.end, %entry
  %24 = load i32* %elem.addr, align 4
  %25 = load %struct.stack** %stack.addr, align 8
  %current_index12 = getelementptr inbounds %struct.stack* %25, i32 0, i32 0
  %26 = load i32* %current_index12, align 4
  %inc13 = add nsw i32 %26, 1
  store i32 %inc13, i32* %current_index12, align 4
  %idxprom14 = sext i32 %26 to i64
  %27 = load %struct.stack** %stack.addr, align 8
  %data15 = getelementptr inbounds %struct.stack* %27, i32 0, i32 2
  %28 = load i32** %data15, align 8
  %arrayidx16 = getelementptr inbounds i32* %28, i64 %idxprom14
  store i32 %24, i32* %arrayidx16, align 4
  ret void
}

; Function Attrs: nounwind uwtable
define internal void @__RV_add(%struct.stacks* %stacks, %struct.stack* %elem) #0 {
entry:
  %stacks.addr = alloca %struct.stacks*, align 8
  %elem.addr = alloca %struct.stack*, align 8
  %i = alloca i32, align 4
  %tmp = alloca %struct.stack**, align 8
  store %struct.stacks* %stacks, %struct.stacks** %stacks.addr, align 8
  store %struct.stack* %elem, %struct.stack** %elem.addr, align 8
  %0 = load %struct.stacks** %stacks.addr, align 8
  %current_index = getelementptr inbounds %struct.stacks* %0, i32 0, i32 0
  %1 = load i32* %current_index, align 4
  %2 = load %struct.stacks** %stacks.addr, align 8
  %length = getelementptr inbounds %struct.stacks* %2, i32 0, i32 1
  %3 = load i32* %length, align 4
  %cmp = icmp sge i32 %1, %3
  br i1 %cmp, label %if.then, label %if.end

if.then:                                          ; preds = %entry
  %4 = load %struct.stacks** %stacks.addr, align 8
  %length1 = getelementptr inbounds %struct.stacks* %4, i32 0, i32 1
  %5 = load i32* %length1, align 4
  %conv = sext i32 %5 to i64
  %mul = mul i64 8, %conv
  %mul2 = mul i64 %mul, 2
  %call = call noalias i8* @malloc(i64 %mul2) #3
  %6 = bitcast i8* %call to %struct.stack**
  store %struct.stack** %6, %struct.stack*** %tmp, align 8
  store i32 0, i32* %i, align 4
  br label %for.cond

for.cond:                                         ; preds = %for.inc, %if.then
  %7 = load i32* %i, align 4
  %8 = load %struct.stacks** %stacks.addr, align 8
  %length3 = getelementptr inbounds %struct.stacks* %8, i32 0, i32 1
  %9 = load i32* %length3, align 4
  %cmp4 = icmp slt i32 %7, %9
  br i1 %cmp4, label %for.body, label %for.end

for.body:                                         ; preds = %for.cond
  %10 = load i32* %i, align 4
  %idxprom = sext i32 %10 to i64
  %11 = load %struct.stacks** %stacks.addr, align 8
  %data = getelementptr inbounds %struct.stacks* %11, i32 0, i32 2
  %12 = load %struct.stack*** %data, align 8
  %arrayidx = getelementptr inbounds %struct.stack** %12, i64 %idxprom
  %13 = load %struct.stack** %arrayidx, align 8
  %14 = load i32* %i, align 4
  %idxprom6 = sext i32 %14 to i64
  %15 = load %struct.stack*** %tmp, align 8
  %arrayidx7 = getelementptr inbounds %struct.stack** %15, i64 %idxprom6
  store %struct.stack* %13, %struct.stack** %arrayidx7, align 8
  br label %for.inc

for.inc:                                          ; preds = %for.body
  %16 = load i32* %i, align 4
  %inc = add nsw i32 %16, 1
  store i32 %inc, i32* %i, align 4
  br label %for.cond

for.end:                                          ; preds = %for.cond
  %17 = load %struct.stacks** %stacks.addr, align 8
  %length8 = getelementptr inbounds %struct.stacks* %17, i32 0, i32 1
  %18 = load i32* %length8, align 4
  %mul9 = mul nsw i32 %18, 2
  store i32 %mul9, i32* %length8, align 4
  %19 = load %struct.stacks** %stacks.addr, align 8
  %data10 = getelementptr inbounds %struct.stacks* %19, i32 0, i32 2
  %20 = load %struct.stack*** %data10, align 8
  %21 = bitcast %struct.stack** %20 to i8*
  call void @free(i8* %21) #3
  %22 = load %struct.stack*** %tmp, align 8
  %23 = load %struct.stacks** %stacks.addr, align 8
  %data11 = getelementptr inbounds %struct.stacks* %23, i32 0, i32 2
  store %struct.stack** %22, %struct.stack*** %data11, align 8
  br label %if.end

if.end:                                           ; preds = %for.end, %entry
  %24 = load %struct.stack** %elem.addr, align 8
  %25 = load %struct.stacks** %stacks.addr, align 8
  %current_index12 = getelementptr inbounds %struct.stacks* %25, i32 0, i32 0
  %26 = load i32* %current_index12, align 4
  %inc13 = add nsw i32 %26, 1
  store i32 %inc13, i32* %current_index12, align 4
  %idxprom14 = sext i32 %26 to i64
  %27 = load %struct.stacks** %stacks.addr, align 8
  %data15 = getelementptr inbounds %struct.stacks* %27, i32 0, i32 2
  %28 = load %struct.stack*** %data15, align 8
  %arrayidx16 = getelementptr inbounds %struct.stack** %28, i64 %idxprom14
  store %struct.stack* %24, %struct.stack** %arrayidx16, align 8
  ret void
}

; Function Attrs: nounwind uwtable
define void @__RV_init() #0 {
entry:
  %stack = alloca %struct.stack*, align 8
  %call = call %struct.stack* @__RV_new_RV_stack(i32 10)
  store %struct.stack* %call, %struct.stack** %stack, align 8
  %0 = load %struct.stack** %stack, align 8
  call void @__RV_push(%struct.stack* %0, i32 -2)
  %1 = load %struct.stack** %stack, align 8
  call void @__RV_push(%struct.stack* %1, i32 0)
  %2 = load %struct.stacks** @__RV_stacks_inst, align 8
  %3 = load %struct.stack** %stack, align 8
  call void @__RV_add(%struct.stacks* %2, %struct.stack* %3)
  ret void
}

; Function Attrs: nounwind uwtable
define void @__RVC_IsNull_deref() #0 {
entry:
  %0 = load %struct.stacks** @__RV_stacks_inst, align 8
  %cmp = icmp eq %struct.stacks* %0, null
  br i1 %cmp, label %if.then, label %if.end

if.then:                                          ; preds = %entry
  %call = call %struct.stacks* @__RV_new_RV_stacks(i32 5)
  store %struct.stacks* %call, %struct.stacks** @__RV_stacks_inst, align 8
  call void @__RV_init()
  br label %if.end

if.end:                                           ; preds = %if.then, %entry
  call void @monitor(i32 1)
  %1 = load i32* @__RV_cat, align 4
  %cmp1 = icmp eq i32 %1, 0
  %conv = zext i1 %cmp1 to i32
  store i32 %conv, i32* @__RVC_IsNull_match, align 4
  %2 = load i32* @__RV_cat, align 4
  %cmp2 = icmp eq i32 %2, 2
  %conv3 = zext i1 %cmp2 to i32
  store i32 %conv3, i32* @__RVC_IsNull_fail, align 4
  %3 = load i32* @__RVC_IsNull_match, align 4
  %tobool = icmp ne i32 %3, 0
  br i1 %tobool, label %if.then4, label %if.end6

if.then4:                                         ; preds = %if.end
  %4 = load %struct._IO_FILE** @stderr, align 8
  %call5 = call i32 (%struct._IO_FILE*, i8*, ...)* @fprintf(%struct._IO_FILE* %4, i8* getelementptr inbounds ([33 x i8]* @.str, i32 0, i32 0))
  call void @__RVC_IsNull_reset()
  br label %if.end6

if.end6:                                          ; preds = %if.then4, %if.end
  ret void
}

; Function Attrs: nounwind uwtable
define internal %struct.stacks* @__RV_new_RV_stacks(i32 %size) #0 {
entry:
  %size.addr = alloca i32, align 4
  %ret = alloca %struct.stacks*, align 8
  store i32 %size, i32* %size.addr, align 4
  %call = call noalias i8* @malloc(i64 16) #3
  %0 = bitcast i8* %call to %struct.stacks*
  store %struct.stacks* %0, %struct.stacks** %ret, align 8
  %1 = load %struct.stacks** %ret, align 8
  %current_index = getelementptr inbounds %struct.stacks* %1, i32 0, i32 0
  store i32 0, i32* %current_index, align 4
  %2 = load i32* %size.addr, align 4
  %3 = load %struct.stacks** %ret, align 8
  %length = getelementptr inbounds %struct.stacks* %3, i32 0, i32 1
  store i32 %2, i32* %length, align 4
  %4 = load i32* %size.addr, align 4
  %conv = sext i32 %4 to i64
  %mul = mul i64 8, %conv
  %call1 = call noalias i8* @malloc(i64 %mul) #3
  %5 = bitcast i8* %call1 to %struct.stack**
  %6 = load %struct.stacks** %ret, align 8
  %data = getelementptr inbounds %struct.stacks* %6, i32 0, i32 2
  store %struct.stack** %5, %struct.stack*** %data, align 8
  %7 = load %struct.stacks** %ret, align 8
  ret %struct.stacks* %7
}

; Function Attrs: nounwind uwtable
define internal void @monitor(i32 %event) #0 {
entry:
  %event.addr = alloca i32, align 4
  %i = alloca i32, align 4
  %j = alloca i32, align 4
  %old = alloca i32, align 4
  %s = alloca i32, align 4
  %stack = alloca %struct.stack*, align 8
  store i32 %event, i32* %event.addr, align 4
  %0 = load i32* @__RV_cat, align 4
  %cmp = icmp ne i32 %0, 2
  br i1 %cmp, label %if.then, label %if.end50

if.then:                                          ; preds = %entry
  %1 = load i32* %event.addr, align 4
  %dec = add nsw i32 %1, -1
  store i32 %dec, i32* %event.addr, align 4
  store i32 1, i32* @__RV_cat, align 4
  %2 = load %struct.stacks** @__RV_stacks_inst, align 8
  %call = call %struct.stack* @__RV_get(%struct.stacks* %2, i32 0)
  store %struct.stack* %call, %struct.stack** %stack, align 8
  br label %while.cond

while.cond:                                       ; preds = %if.end49, %if.then
  %3 = load %struct.stack** %stack, align 8
  %current_index = getelementptr inbounds %struct.stack* %3, i32 0, i32 0
  %4 = load i32* %current_index, align 4
  %cmp1 = icmp sge i32 %4, 0
  br i1 %cmp1, label %while.body, label %while.end

while.body:                                       ; preds = %while.cond
  %5 = load %struct.stack** %stack, align 8
  %call2 = call i32 @__RV_peek(%struct.stack* %5)
  store i32 %call2, i32* %s, align 4
  %6 = load i32* %s, align 4
  %cmp3 = icmp sge i32 %6, 0
  br i1 %cmp3, label %land.lhs.true, label %if.else

land.lhs.true:                                    ; preds = %while.body
  %7 = load i32* %event.addr, align 4
  %idxprom = sext i32 %7 to i64
  %8 = load i32* %s, align 4
  %idxprom4 = sext i32 %8 to i64
  %arrayidx = getelementptr inbounds [11 x [2 x [2 x [3 x i32]]]]* @__RV_at, i32 0, i64 %idxprom4
  %arrayidx5 = getelementptr inbounds [2 x [2 x [3 x i32]]]* %arrayidx, i32 0, i64 %idxprom
  %arrayidx6 = getelementptr inbounds [2 x [3 x i32]]* %arrayidx5, i32 0, i64 0
  %arrayidx7 = getelementptr inbounds [3 x i32]* %arrayidx6, i32 0, i64 0
  %9 = load i32* %arrayidx7, align 4
  %cmp8 = icmp ne i32 %9, 0
  br i1 %cmp8, label %if.then9, label %if.else

if.then9:                                         ; preds = %land.lhs.true
  %10 = load i32* %event.addr, align 4
  %idxprom10 = sext i32 %10 to i64
  %11 = load i32* %s, align 4
  %idxprom11 = sext i32 %11 to i64
  %arrayidx12 = getelementptr inbounds [11 x [2 x [2 x [3 x i32]]]]* @__RV_at, i32 0, i64 %idxprom11
  %arrayidx13 = getelementptr inbounds [2 x [2 x [3 x i32]]]* %arrayidx12, i32 0, i64 %idxprom10
  %arrayidx14 = getelementptr inbounds [2 x [3 x i32]]* %arrayidx13, i32 0, i64 0
  %arrayidx15 = getelementptr inbounds [3 x i32]* %arrayidx14, i32 0, i64 0
  %12 = load i32* %arrayidx15, align 4
  switch i32 %12, label %sw.epilog [
    i32 1, label %sw.bb
    i32 2, label %sw.bb31
  ]

sw.bb:                                            ; preds = %if.then9
  %13 = load %struct.stack** %stack, align 8
  %14 = load i32* %event.addr, align 4
  %idxprom16 = sext i32 %14 to i64
  %15 = load i32* %s, align 4
  %idxprom17 = sext i32 %15 to i64
  %arrayidx18 = getelementptr inbounds [11 x [2 x [2 x [3 x i32]]]]* @__RV_at, i32 0, i64 %idxprom17
  %arrayidx19 = getelementptr inbounds [2 x [2 x [3 x i32]]]* %arrayidx18, i32 0, i64 %idxprom16
  %arrayidx20 = getelementptr inbounds [2 x [3 x i32]]* %arrayidx19, i32 0, i64 0
  %arrayidx21 = getelementptr inbounds [3 x i32]* %arrayidx20, i32 0, i64 1
  %16 = load i32* %arrayidx21, align 4
  call void @__RV_push(%struct.stack* %13, i32 %16)
  %17 = load i32* %event.addr, align 4
  %idxprom22 = sext i32 %17 to i64
  %18 = load i32* %s, align 4
  %idxprom23 = sext i32 %18 to i64
  %arrayidx24 = getelementptr inbounds [11 x [2 x [2 x [3 x i32]]]]* @__RV_at, i32 0, i64 %idxprom23
  %arrayidx25 = getelementptr inbounds [2 x [2 x [3 x i32]]]* %arrayidx24, i32 0, i64 %idxprom22
  %arrayidx26 = getelementptr inbounds [2 x [3 x i32]]* %arrayidx25, i32 0, i64 0
  %arrayidx27 = getelementptr inbounds [3 x i32]* %arrayidx26, i32 0, i64 1
  %19 = load i32* %arrayidx27, align 4
  %idxprom28 = sext i32 %19 to i64
  %arrayidx29 = getelementptr inbounds [11 x i32]* @__RV_acc, i32 0, i64 %idxprom28
  %20 = load i32* %arrayidx29, align 4
  %tobool = icmp ne i32 %20, 0
  br i1 %tobool, label %if.then30, label %if.end

if.then30:                                        ; preds = %sw.bb
  store i32 0, i32* @__RV_cat, align 4
  br label %if.end

if.end:                                           ; preds = %if.then30, %sw.bb
  br label %if.end50

sw.bb31:                                          ; preds = %if.then9
  %21 = load %struct.stack** %stack, align 8
  %22 = load i32* %event.addr, align 4
  %idxprom32 = sext i32 %22 to i64
  %23 = load i32* %s, align 4
  %idxprom33 = sext i32 %23 to i64
  %arrayidx34 = getelementptr inbounds [11 x [2 x [2 x [3 x i32]]]]* @__RV_at, i32 0, i64 %idxprom33
  %arrayidx35 = getelementptr inbounds [2 x [2 x [3 x i32]]]* %arrayidx34, i32 0, i64 %idxprom32
  %arrayidx36 = getelementptr inbounds [2 x [3 x i32]]* %arrayidx35, i32 0, i64 0
  %arrayidx37 = getelementptr inbounds [3 x i32]* %arrayidx36, i32 0, i64 2
  %24 = load i32* %arrayidx37, align 4
  call void @__RV_pop_n(%struct.stack* %21, i32 %24)
  %25 = load %struct.stack** %stack, align 8
  %call38 = call i32 @__RV_peek(%struct.stack* %25)
  store i32 %call38, i32* %old, align 4
  %26 = load %struct.stack** %stack, align 8
  %27 = load i32* %event.addr, align 4
  %idxprom39 = sext i32 %27 to i64
  %28 = load i32* %s, align 4
  %idxprom40 = sext i32 %28 to i64
  %arrayidx41 = getelementptr inbounds [11 x [2 x [2 x [3 x i32]]]]* @__RV_at, i32 0, i64 %idxprom40
  %arrayidx42 = getelementptr inbounds [2 x [2 x [3 x i32]]]* %arrayidx41, i32 0, i64 %idxprom39
  %arrayidx43 = getelementptr inbounds [2 x [3 x i32]]* %arrayidx42, i32 0, i64 0
  %arrayidx44 = getelementptr inbounds [3 x i32]* %arrayidx43, i32 0, i64 1
  %29 = load i32* %arrayidx44, align 4
  %idxprom45 = sext i32 %29 to i64
  %30 = load i32* %old, align 4
  %idxprom46 = sext i32 %30 to i64
  %arrayidx47 = getelementptr inbounds [11 x [3 x i32]]* @__RV_gt, i32 0, i64 %idxprom46
  %arrayidx48 = getelementptr inbounds [3 x i32]* %arrayidx47, i32 0, i64 %idxprom45
  %31 = load i32* %arrayidx48, align 4
  call void @__RV_push(%struct.stack* %26, i32 %31)
  br label %sw.epilog

sw.epilog:                                        ; preds = %sw.bb31, %if.then9
  br label %if.end49

if.else:                                          ; preds = %land.lhs.true, %while.body
  store i32 2, i32* @__RV_cat, align 4
  br label %if.end50

if.end49:                                         ; preds = %sw.epilog
  br label %while.cond

while.end:                                        ; preds = %while.cond
  br label %if.end50

if.end50:                                         ; preds = %while.end, %if.else, %if.end, %entry
  ret void
}

declare i32 @fprintf(%struct._IO_FILE*, i8*, ...) #1

; Function Attrs: nounwind uwtable
define void @__RVC_IsNull_isNull() #0 {
entry:
  %0 = load %struct.stacks** @__RV_stacks_inst, align 8
  %cmp = icmp eq %struct.stacks* %0, null
  br i1 %cmp, label %if.then, label %if.end

if.then:                                          ; preds = %entry
  %call = call %struct.stacks* @__RV_new_RV_stacks(i32 5)
  store %struct.stacks* %call, %struct.stacks** @__RV_stacks_inst, align 8
  call void @__RV_init()
  br label %if.end

if.end:                                           ; preds = %if.then, %entry
  call void @monitor(i32 2)
  %1 = load i32* @__RV_cat, align 4
  %cmp1 = icmp eq i32 %1, 0
  %conv = zext i1 %cmp1 to i32
  store i32 %conv, i32* @__RVC_IsNull_match, align 4
  %2 = load i32* @__RV_cat, align 4
  %cmp2 = icmp eq i32 %2, 2
  %conv3 = zext i1 %cmp2 to i32
  store i32 %conv3, i32* @__RVC_IsNull_fail, align 4
  %3 = load i32* @__RVC_IsNull_match, align 4
  %tobool = icmp ne i32 %3, 0
  br i1 %tobool, label %if.then4, label %if.end6

if.then4:                                         ; preds = %if.end
  %4 = load %struct._IO_FILE** @stderr, align 8
  %call5 = call i32 (%struct._IO_FILE*, i8*, ...)* @fprintf(%struct._IO_FILE* %4, i8* getelementptr inbounds ([33 x i8]* @.str, i32 0, i32 0))
  call void @__RVC_IsNull_reset()
  br label %if.end6

if.end6:                                          ; preds = %if.then4, %if.end
  ret void
}

; Function Attrs: nounwind uwtable
define internal %struct.stack* @__RV_get(%struct.stacks* %stacks, i32 %i) #0 {
entry:
  %stacks.addr = alloca %struct.stacks*, align 8
  %i.addr = alloca i32, align 4
  store %struct.stacks* %stacks, %struct.stacks** %stacks.addr, align 8
  store i32 %i, i32* %i.addr, align 4
  %0 = load i32* %i.addr, align 4
  %idxprom = sext i32 %0 to i64
  %1 = load %struct.stacks** %stacks.addr, align 8
  %data = getelementptr inbounds %struct.stacks* %1, i32 0, i32 2
  %2 = load %struct.stack*** %data, align 8
  %arrayidx = getelementptr inbounds %struct.stack** %2, i64 %idxprom
  %3 = load %struct.stack** %arrayidx, align 8
  ret %struct.stack* %3
}

; Function Attrs: nounwind uwtable
define internal i32 @__RV_peek(%struct.stack* %stack) #0 {
entry:
  %stack.addr = alloca %struct.stack*, align 8
  store %struct.stack* %stack, %struct.stack** %stack.addr, align 8
  %0 = load %struct.stack** %stack.addr, align 8
  %current_index = getelementptr inbounds %struct.stack* %0, i32 0, i32 0
  %1 = load i32* %current_index, align 4
  %sub = sub nsw i32 %1, 1
  %idxprom = sext i32 %sub to i64
  %2 = load %struct.stack** %stack.addr, align 8
  %data = getelementptr inbounds %struct.stack* %2, i32 0, i32 2
  %3 = load i32** %data, align 8
  %arrayidx = getelementptr inbounds i32* %3, i64 %idxprom
  %4 = load i32* %arrayidx, align 4
  ret i32 %4
}

; Function Attrs: nounwind uwtable
define internal void @__RV_pop_n(%struct.stack* %stack, i32 %n) #0 {
entry:
  %stack.addr = alloca %struct.stack*, align 8
  %n.addr = alloca i32, align 4
  store %struct.stack* %stack, %struct.stack** %stack.addr, align 8
  store i32 %n, i32* %n.addr, align 4
  %0 = load i32* %n.addr, align 4
  %1 = load %struct.stack** %stack.addr, align 8
  %current_index = getelementptr inbounds %struct.stack* %1, i32 0, i32 0
  %2 = load i32* %current_index, align 4
  %sub = sub nsw i32 %2, %0
  store i32 %sub, i32* %current_index, align 4
  ret void
}

; Function Attrs: nounwind
declare noalias i8* @malloc(i64) #2

; Function Attrs: nounwind
declare void @free(i8*) #2

; Function Attrs: nounwind uwtable
define internal void @__RV_delete_RV_stack(%struct.stack* %stack) #0 {
entry:
  %stack.addr = alloca %struct.stack*, align 8
  store %struct.stack* %stack, %struct.stack** %stack.addr, align 8
  %0 = load %struct.stack** %stack.addr, align 8
  %cmp = icmp eq %struct.stack* %0, null
  br i1 %cmp, label %if.then, label %if.end

if.then:                                          ; preds = %entry
  br label %return

if.end:                                           ; preds = %entry
  %1 = load %struct.stack** %stack.addr, align 8
  %data = getelementptr inbounds %struct.stack* %1, i32 0, i32 2
  %2 = load i32** %data, align 8
  %3 = bitcast i32* %2 to i8*
  call void @free(i8* %3) #3
  %4 = load %struct.stack** %stack.addr, align 8
  %5 = bitcast %struct.stack* %4 to i8*
  call void @free(i8* %5) #3
  br label %return

return:                                           ; preds = %if.end, %if.then
  ret void
}

attributes #0 = { nounwind uwtable "less-precise-fpmad"="false" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-nans-fp-math"="false" "stack-protector-buffer-size"="8" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #1 = { "less-precise-fpmad"="false" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-nans-fp-math"="false" "stack-protector-buffer-size"="8" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #2 = { nounwind "less-precise-fpmad"="false" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-nans-fp-math"="false" "stack-protector-buffer-size"="8" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #3 = { nounwind }

!llvm.ident = !{!0}

!0 = metadata !{metadata !"clang version 3.4 (trunk 194993)"}
