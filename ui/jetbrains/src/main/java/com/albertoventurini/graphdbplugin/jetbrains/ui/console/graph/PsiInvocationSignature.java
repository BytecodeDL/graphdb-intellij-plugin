package com.albertoventurini.graphdbplugin.jetbrains.ui.console.graph;

import com.albertoventurini.graphdbplugin.database.neo4j.bytecodedl.InvocationSignature;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;

/**
 * @author daozhe@alibaba-inc.com
 * @date 2023/12/3 16:05
 */
public class PsiInvocationSignature extends InvocationSignature{

    public PsiInvocationSignature(String insn){
        super(insn);
    }

    public PsiMethod getCaller(Project project){
        PsiMethodSignature psiMethodSignature = new PsiMethodSignature(this.getCallerSignature());
        return psiMethodSignature.getMethod(project);
    }
}
