package com.albertoventurini.graphdbplugin.jetbrains.ui.console.graph;

import com.albertoventurini.graphdbplugin.database.neo4j.bytecodedl.MethodSignature;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * @author daozhe@alibaba-inc.com
 * @date 2023/12/2 21:10
 */
public class PsiMethodSignature extends MethodSignature{

    public PsiMethodSignature(String signature) {
        super(signature);
    }

    public PsiMethod getMethod(Project project) {
        JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
        PsiClass psiClass = facade.findClass(this.getClassName(), GlobalSearchScope.allScope(project));

        if (psiClass != null) {
            for (PsiMethod method : psiClass.getMethods()) {
                if (method.getName().equals(this.getMethodName()) && parametersMatch(method.getParameterList().getParameters())) {
                    return method;
                }
            }
        }
        return null;
    }

    private boolean parametersMatch(PsiParameter[] parameters) {
        if (parameters.length != this.getParamTypes().size()) {
            return false;
        }
        for (int i = 0; i < parameters.length; i++) {
            PsiType type = parameters[i].getType();
            if (!matchType(type, this.getParamTypes().get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean matchType(PsiType type, String strType){
        // type.getCanonicalText return value contains the generic type
        String typeText = type.getCanonicalText();
        int index = typeText.indexOf("<");
        if(index > 0){
            typeText = typeText.substring(0, index);
        }

        return typeText.equals(strType);
    }
}
