package com.albertoventurini.graphdbplugin.jetbrains.ui.console.graph;

import com.albertoventurini.graphdbplugin.database.api.data.GraphNode;
import com.intellij.ide.SelectInEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.usageView.UsageInfo;
import prefuse.visual.VisualItem;

import java.awt.event.MouseEvent;

/**
 * @author daozhe@alibaba-inc.com
 * @date 2023/12/2 20:40
 */
public class NodeNavigateAction {
    private final Project project;
    public NodeNavigateAction(Project project){
        this.project = project;
    }
    public void navigateToMethodDeclaration(GraphNode node, VisualItem item, MouseEvent e){
        PsiMethod method = getPsiMethod(node);
        navigate(method);
        //navigateToMethod(method);
    }

    public PsiMethod getPsiMethod(GraphNode node){
        String methodSignature = (String)node.getPropertyContainer().getProperties().get("method");
        JavaMethodSignature signature = new JavaMethodSignature(methodSignature);
        PsiMethod method = signature.getMethod(this.project);
        return method;
    }

    private void navigateToMethod(PsiMethod method) {
        PsiFile containingFile = method.getContainingFile();
        if (containingFile != null) {
            VirtualFile virtualFile = containingFile.getVirtualFile();
            if (virtualFile != null) {
                new OpenFileDescriptor(method.getProject(), virtualFile, method.getTextOffset()).navigate(true);
            }
        }
    }

    private void navigate(PsiMethod method){
        UsageInfo usage = new UsageInfo(method);
        SelectInEditorManager.getInstance(this.project)
                .selectInEditor(usage.getVirtualFile(), usage.getSegment().getStartOffset(), usage.getSegment().getEndOffset(), true, false);
    }
}
