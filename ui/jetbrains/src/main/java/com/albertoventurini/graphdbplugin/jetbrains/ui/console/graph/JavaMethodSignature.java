package com.albertoventurini.graphdbplugin.jetbrains.ui.console.graph;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author daozhe@alibaba-inc.com
 * @date 2023/12/2 21:10
 */
public class JavaMethodSignature {
    private String signature;
    private String className;
    private String methodName;
    private final String[] paramTypes;

    private PsiElement element;

    private static final Pattern SIGNATURE_PATTERN = Pattern.compile(
            "^<([^:]+):\\s*(\\S+)\\s+([^\\(]+)\\(([^\\)]*)\\)>$"
    );

    public JavaMethodSignature(String signature){
        this.signature = signature;
        // 假设输入的格式为 <org.apache.logging.log4j.core.filter.Filterable: boolean isFiltered(org.apache.logging.log4j.core.LogEvent)>
        Matcher matcher = SIGNATURE_PATTERN.matcher(signature);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid method signature format");
        }

        this.className = matcher.group(1).trim();
        // Group 2 is the return type which is not used in this implementation
        this.methodName = matcher.group(3).trim();

        if (this.methodName.equals("<init>")){
            this.methodName = className.substring(className.lastIndexOf(".") + 1);
        }

        String params = matcher.group(4).trim();
        this.paramTypes = params.isEmpty() ? new String[0] : params.split(",");
    }

    public String getClassName(){
        return this.methodName;
    }

    public String getMethodName(){
        return this.methodName;
    }

    public String[] getParamTypes(){
        return this.paramTypes;
    }

    public PsiMethod getMethod(Project project) {
        JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
        PsiClass psiClass = facade.findClass(this.className.replace("$", "."), GlobalSearchScope.allScope(project));

        if (psiClass != null) {
            for (PsiMethod method : psiClass.getMethods()) {
                if (method.getName().equals(methodName) && parametersMatch(method.getParameterList().getParameters())) {
                    return method;
                }
            }
        }
        return null;
    }

    private boolean parametersMatch(PsiParameter[] parameters) {
        if (parameters.length != this.paramTypes.length) {
            return false;
        }
        for (int i = 0; i < parameters.length; i++) {
            PsiType type = parameters[i].getType();
            if (!type.getCanonicalText().equals(this.paramTypes[i])) {
                return false;
            }
        }
        return true;
    }
}
