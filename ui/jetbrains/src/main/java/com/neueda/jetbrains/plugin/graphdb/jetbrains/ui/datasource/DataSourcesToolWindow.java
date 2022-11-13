package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

public class DataSourcesToolWindow implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        project.getService(DataSourcesView.class).initToolWindow(project, toolWindow);
    }
}
