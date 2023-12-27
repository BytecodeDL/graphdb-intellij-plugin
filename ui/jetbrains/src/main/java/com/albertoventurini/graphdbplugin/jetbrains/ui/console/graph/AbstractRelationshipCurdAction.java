package com.albertoventurini.graphdbplugin.jetbrains.ui.console.graph;

import com.albertoventurini.graphdbplugin.database.api.data.GraphRelationship;
import com.albertoventurini.graphdbplugin.jetbrains.actions.execute.ExecuteQueryPayload;
import com.albertoventurini.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;
import com.albertoventurini.graphdbplugin.jetbrains.database.QueryExecutionService;
import com.google.common.collect.ImmutableMap;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author daozhe@alibaba-inc.com
 * @date 2023/12/10 16:33
 */
public abstract class AbstractRelationshipCurdAction extends AnAction {
    protected DataSourceApi dataSource;
    protected GraphRelationship relationship;

    AbstractRelationshipCurdAction(String title, String description, Icon icon, DataSourceApi dataSource, GraphRelationship relationship) {
        super(title, description, icon);
        this.dataSource = dataSource;
        this.relationship = relationship;
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = getEventProject(e);
        if (project != null) {
            QueryExecutionService service = new QueryExecutionService(project, project.getMessageBus());

            service.executeQuery(dataSource, this.getQuery());
        }
    }

    public abstract ExecuteQueryPayload getQuery();
}
