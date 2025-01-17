package com.albertoventurini.graphdbplugin.jetbrains.ui.console.graph;

import com.albertoventurini.graphdbplugin.database.api.data.GraphRelationship;
import com.intellij.ide.SelectInEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.usageView.UsageInfo;
import prefuse.visual.VisualItem;

import java.awt.event.MouseEvent;
import java.util.Collection;

/**
 * @author daozhe@alibaba-inc.com
 * @date 2023/12/3 16:14
 */
public class EdgeNavigateAction {
    private final Project project;
    public EdgeNavigateAction(Project project){
        this.project = project;
    }

    public void navigateToInvocation(GraphRelationship relationship, VisualItem item, MouseEvent e){
        PsiInvocationSignature invocation =  getJavaInvokeInsn(relationship);
        PsiMethod method = getPsiMethod(invocation);
        PsiCallExpression methodCall = getPsiMethodCall(invocation, method);
        navigate(methodCall, method.getContainingFile().getVirtualFile());
    }

    public PsiCallExpression getPsiMethodCall(PsiInvocationSignature invocation, PsiMethod method){
        PsiMethod psiMethod = null;
        if (method.getContainingFile().getName().endsWith(".java")){
            psiMethod = method;
        }else{
            UsageInfo usage = new UsageInfo(method);
            psiMethod = PsiTreeUtil.getParentOfType(usage.getFile().findElementAt(usage.getSegment().getEndOffset()), PsiMethod.class);
        }
        Collection<PsiCallExpression> callExpressions = PsiTreeUtil.collectElementsOfType(psiMethod, PsiCallExpression.class);
        return callExpressions.stream().filter(call -> matchInvocation(call, invocation)).findFirst().get();
    }

    public PsiCallExpression getPsiMethodCall1(PsiInvocationSignature invocation, PsiMethod method){
        UsageInfo usage = new UsageInfo(method);
        PsiMethod psiMethod = PsiTreeUtil.getParentOfType(usage.getFile().findElementAt(usage.getSegment().getEndOffset()), PsiMethod.class);
        Collection<PsiCallExpression> callExpressions = PsiTreeUtil.collectElementsOfType(psiMethod, PsiCallExpression.class);
        // only find the first match ignore the index
        // TODO: consider the index
        return callExpressions.stream().filter(call -> matchInvocation(call, invocation)).findFirst().get();
    }

    public boolean matchInvocation(PsiCallExpression call, PsiInvocationSignature invocation){
        if (call instanceof PsiMethodCallExpression) {
            String fqn = getFqn((PsiMethodCallExpression) call);
            return invocation.getCallee().endsWith(fqn);
        }

        PsiMethod callee = call.resolveMethod();
        if (callee == null){
            return call.getText().contains(invocation.getCalleeMethodName() + "(");
        }else{
            PsiClass calleeClass = callee.getContainingClass();
            String methodName = callee.getName();
            String className = calleeClass.getQualifiedName();
            String fqn = className + "." + methodName;
            return fqn.equals(invocation.getCallee());
        }
    }

    public String getFqn(PsiMethodCallExpression expression){

        PsiReferenceExpression methodRef = expression.getMethodExpression();
        String methodName = methodRef.getReferenceName();
        String fqn = methodName;
        PsiElement qualifier = methodRef.getQualifier();
        if (qualifier != null){
            if (qualifier instanceof PsiExpression){
                PsiExpression qualifierExpression = (PsiExpression)  qualifier;
                PsiType type = qualifierExpression.getType();
                if (type != null){
                    fqn = type.getCanonicalText() + "." + methodName;
                }else{
                    fqn = qualifierExpression.getText() + "." + methodName;
                }
            }
        }

        return fqn;
    }

    public PsiInvocationSignature getJavaInvokeInsn(GraphRelationship relationship){
        String insn = (String)relationship.getPropertyContainer().getProperties().get("insn");
        PsiInvocationSignature invocation = new PsiInvocationSignature(insn);
        return invocation;
    }

    public PsiMethod getPsiMethod(PsiInvocationSignature invocation){
        PsiMethod method = invocation.getCaller(this.project);
        return method;
    }



    private void navigate(PsiElement element, VirtualFile virtualFile){
        UsageInfo usage = new UsageInfo(element);
        SelectInEditorManager.getInstance(this.project)
                .selectInEditor(virtualFile, usage.getSegment().getStartOffset(), usage.getSegment().getEndOffset(), true, false);
    }
}
