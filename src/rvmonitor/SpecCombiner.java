package rvmonitor;

import java.util.ArrayList;
import java.util.List;

import rvmonitor.parser.ast.ImportDeclaration;
import rvmonitor.parser.ast.RVMSpecFile;
import rvmonitor.parser.ast.PackageDeclaration;
import rvmonitor.parser.ast.mopspec.RVMonitorSpec;

public class SpecCombiner {

	static public RVMSpecFile process(ArrayList<RVMSpecFile> specFiles) throws RVMException {
		PackageDeclaration pakage = null;
		List<ImportDeclaration> imports = new ArrayList<ImportDeclaration>();
		List<RVMonitorSpec> specList = new ArrayList<RVMonitorSpec>();
		
		for(RVMSpecFile specFile : specFiles){
			//package decl
			PackageDeclaration pakage2 = specFile.getPakage();
			if(pakage == null)
				pakage = pakage2;
			else {
				if(!pakage2.getName().getName().equals(pakage.getName().getName()))
					throw new RVMException("Specifications need to be in the same package to be combined.");
			}
			
			//imports
			List<ImportDeclaration> imports2 = specFile.getImports();
			
			for(ImportDeclaration imp2 : imports2){
				boolean included = false;
				for(ImportDeclaration imp : imports){
					if(imp2.getName().getName().equals(imp.getName().getName())){
						included = true;
						break;
					}
				}
				
				if(!included)
					imports.add(imp2);
			}
			
			//specs
			List<RVMonitorSpec> specList2 = specFile.getSpecs();
			
			for(RVMonitorSpec spec2 : specList2){
				boolean included = false;
				for(RVMonitorSpec spec : specList){
					if(spec2.getName().equals(spec.getName())){
						included = true;
						break;
					}
				}
				
				if(!included)
					specList.add(spec2);
			}
		}
		
		return new RVMSpecFile(0, 0, pakage, imports, specList);
	}
}
