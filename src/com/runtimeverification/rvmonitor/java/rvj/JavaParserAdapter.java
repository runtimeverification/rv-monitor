package com.runtimeverification.rvmonitor.java.rvj;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.runtimeverification.rvmonitor.util.RVMException;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.ImportDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.PackageDeclaration;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.ModifierSet;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.NameExpr;;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.QualifiedNameExpr;;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.SpecModifierSet;

import com.runtimeverification.rvmonitor.java.rvj.parser.astex.RVMSpecFileExt;

import com.runtimeverification.rvmonitor.java.rvj.parser.astex.mopspec.RVMonitorSpecExt;

import com.runtimeverification.rvmonitor.core.ast.Event;
import com.runtimeverification.rvmonitor.core.ast.MonitorFile;
import com.runtimeverification.rvmonitor.core.ast.Property;
import com.runtimeverification.rvmonitor.core.ast.PropertyHandler;
import com.runtimeverification.rvmonitor.core.ast.Specification;
import com.runtimeverification.rvmonitor.core.parser.RVParser;


public class JavaParserAdapter {
    
    /**
     * Produce a RVMSpecFileExt by reading a file through the language-independent RVM parser.
     * @param file The file to read from.
     * @return A java-specific RVM specification object.
     */
    public static RVMSpecFileExt parse(File file) throws RVMException {
        try {
            final Reader source = new InputStreamReader(new FileInputStream(file));
            final MonitorFile spec = RVParser.parse(source);
            return convert(spec);
        } catch(Exception e) {
            throw new RVMException(e);
        }
    }
    
    /**
     * Convert a language-independent specification into one with java-specific information.
     * @param spec The specification to convert.
     * @return The Java-specific specification.
     */
    private static RVMSpecFileExt convert(MonitorFile file) {
        final PackageDeclaration filePackage = getPackage(file.getPreamble());
        final List<ImportDeclaration> imports = getImports(file.getPreamble());
        final ArrayList<RVMonitorSpecExt> specs = new ArrayList<RVMonitorSpecExt>();
        for(Specification spec : file.getSpecifications()) {
            specs.add(convert(spec));
        }
        return new RVMSpecFileExt(0, 0, filePackage, imports, specs);
    }
    
    /**
     * Extract the package from the package statement in the preamble.
     * @param preamble The beginning of the specification file.
     * @return The package the class should be in.
     */
    private static PackageDeclaration getPackage(String preamble) {
        final Pattern pattern = Pattern.compile(
            "^\\s*package\\s+(?<name>(?:[a-zA-Z_](?:[a-zA-Z_0-9]*)\\.)*[a-zA-Z_](?:[a-zA-Z_0-9]*))\\s*;\\s*$");
        final Matcher matcher = pattern.matcher(preamble);
        if(matcher.find()) {
            final String fullName = matcher.group("name");
            if(fullName == null) {
                return null;
            }
            final String[] periodSplit = fullName.split("\\.");
            NameExpr name = new NameExpr(0, 0, periodSplit[0]);
            for(int i = 1; i < periodSplit.length; i++) {
                name = new QualifiedNameExpr(0, 0, name, periodSplit[i]);
            }
            return new PackageDeclaration(0, 0, null, name);
        } else {
            return null;
        }
    }
    
    /**
     * Extract the imports from the import statements in the preamble.
     * @param preamble The beginning of the specification file.
     * @return The package the class should be in.
     */
    private static List<ImportDeclaration> getImports(final String preamble) {
        final Pattern pattern = Pattern.compile(
            "^\\s*import(?<isStatic>(?:\\s+static)?)\\s+" +
            "(?<name>(?:[a-zA-Z_](?:[a-zA-Z_0-9]*)\\.)*[a-zA-Z_](?:[a-zA-Z_0-9]*))(?<asterisk>(?:\\.\\*)?)\\s*;\\s*$");
        final Matcher matcher = pattern.matcher(preamble);
        final ArrayList<ImportDeclaration> imports = new ArrayList<ImportDeclaration>();
        while(matcher.find()) {
            final boolean isStatic = matcher.group("isStatic").trim().equals("static");
            final boolean isAsterisk = matcher.group("asterisk").trim().equals(".*");
            final String fullName = matcher.group("name");
            if(fullName == null) {
                return null;
            }
            final String[] periodSplit = fullName.split("\\.");
            NameExpr name = new NameExpr(0, 0, periodSplit[0]);
            for(int i = 1; i < periodSplit.length; i++) {
                name = new QualifiedNameExpr(0, 0, name, periodSplit[i]);
            }
            imports.add(new ImportDeclaration(0, 0, name, isStatic, isAsterisk));
        }
        return imports;
    }
    
    /**
     * 
     */
    private static RVMonitorSpecExt convert(final Specification spec) {
        final List<String> modifierList = spec.getLanguageModifiers();
        final boolean isPublic = modifierList.contains("public");
        final int modifierBitfield = extractModifierBitfield(modifierList);
        final String name = spec.getName();
        final List<RVMParameter> parameters = convertParameters(spec.getLanguageParameters());
        return null;
    }
    
    private static int extractModifierBitfield(final List<String> modifierList) {
        int modifierBitfield = 0;
        if(modifierList.contains("unsynchronized")) {
            modifierBitfield &= SpecModifierSet.UNSYNC;
        }
        if(modifierList.contains("decentralized")) {
            modifierBitfield &= SpecModifierSet.DECENTRL;
        }
        if(modifierList.contains("perthread")) {
            modifierBitfield &= SpecModifierSet.PERTHREAD;
        }
        if(modifierList.contains("suffix")) {
            modifierBitfield &= SpecModifierSet.SUFFIX;
        }
        if(modifierList.contains("full-binding")) {
            modifierBitfield &= SpecModifierSet.FULLBINDING;
        }
        if(modifierList.contains("avoid")) {
            modifierBitfield &= SpecModifierSet.AVOID;
        }
        if(modifierList.contains("enforce")) {
            modifierBitfield &= SpecModifierSet.ENFORCE;
        }
        if(modifierList.contains("connected")) {
            modifierBitfield &= SpecModifierSet.CONNECTED;
        }
        return modifierBitfield;
    }
    
    private static List<RVMParameter> convertParameters(String paramString) {
        return null;
    }
}
