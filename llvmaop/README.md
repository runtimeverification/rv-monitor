This directory contains the LLVM plugin for instrumenting code.

This code depends only on the LLVM 6 development libraries, which can be
installed from the `llvm-6-dev` package from apt.llvm.org .
The CMake files were written following the LLVM documentation on setting up
out-of-tree plugin builds.

To build, create a subdirectory `build`, run `cmake ..` inside that directory
to create makefiles, and run `make` to compile the library.
This will produce the plugin library at `build/llvmmop/LLVMAOP.so`.

   mkdir build
   cd build
   cmake ..
   make

To use it, load the shared library with clang or the opt tool.
This requires either giving an absolute path to the library or
installing it on the system library path.

With clang merely loading the plugin will arrange to run the
instrumentation pass on any files being compiled.
With opt loading the plugin just adds an `-aop` option which
can be used to run the pass.
In Linux, assuming the path to `LLVMAOP.so` is included in the library path,
this will compile and instrument a C program:

    clang -Xclang -load -Xclang LLVMAOP.so -emit-llvm -c input.c -o output.bc

or, opt can be used to instrument an existing bitcode file

    # clang -emit-llvm -c input.c
    opt -load LLVMAOP.so -aop input.bc -o output.bc

The instrumentation pass reads a configuration file that describes where
to insert instrumentation, and what functions to call at the instrumented
points.
The path to the file will be taken from the `RVX_ASPECT` environment variable.
If that is not present it defaults to look for an `aspect.map` file in the current
directory.

The aspect file must consist of lines of the form:

    <when> <what> <fname> call <hname>

where `<when> ::= before | after | instead-of`
and `<what> ::= executing | calling`
(currently "executing" is only supported for "before")

`<hname>` is supposed to have the same signature as `<fname>` except that its
return type is void and, for "after calling" instrumentation point, it has
an additional first argument to hold the return value if `<fname>` has a
non-void return type.