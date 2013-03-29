package rvmonitor.parser.ast;

import java.util.*;

import rvmonitor.parser.ast.mopspec.RVMonitorSpec;
import rvmonitor.parser.ast.visitor.GenericVisitor;
import rvmonitor.parser.ast.visitor.VoidVisitor;

public class RVMSpecFile extends Node {
	PackageDeclaration pakage = null;
	List<ImportDeclaration> imports = null;
	List<RVMonitorSpec> specList = null;
	
    public RVMSpecFile(int line, int column, PackageDeclaration pakage, List<ImportDeclaration> imports, List<RVMonitorSpec> specList) {
        super(line, column);
        this.pakage = pakage;
        this.imports = imports;
        this.specList = specList;
    }
    public PackageDeclaration getPakage() {
        return pakage;
    }

    public List<ImportDeclaration> getImports() {
        return imports;
    }
	
    public List<RVMonitorSpec> getSpecs() {
        return specList;
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
