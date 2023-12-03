package com.albertoventurini.graphdbplugin.jetbrains.ui.console.graph;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;

/**
 * @author daozhe@alibaba-inc.com
 * @date 2023/12/3 16:05
 */
public class JavaInvokeInsn {
    private JavaMethodSignature callerSignature;
    private String callee;
    private Integer index;

    public JavaInvokeInsn(String insn){
        String[] parts = insn.split("/");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid instruction format");
        }

        this.callerSignature = new JavaMethodSignature(parts[0]);
        this.callee = parts[1].replace("$", ".");

        try {
            this.index = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid index value", e);
        }
    }

    public PsiMethod getCaller(Project project){
        return this.callerSignature.getMethod(project);
    }

    public String getCallee(){
        return this.callee;
    }

    public String getCalleeMethodName(){
        return this.callee.substring(callee.lastIndexOf(".")+1);
    }


}
