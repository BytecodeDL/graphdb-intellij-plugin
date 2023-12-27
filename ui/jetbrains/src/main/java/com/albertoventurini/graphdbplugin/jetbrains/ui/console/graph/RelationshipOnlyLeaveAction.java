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
 * @date 2023/12/10 12:27
 */
public class RelationshipOnlyLeaveAction extends AbstractRelationshipCurdAction {

    RelationshipOnlyLeaveAction(String title, String description, Icon icon, DataSourceApi dataSource, GraphRelationship relationship) {
        super(title, description, icon, dataSource, relationship);
    }

    public ExecuteQueryPayload getQuery(){
        return new ExecuteQueryPayload("MATCH ()-[n:Call]->() WHERE elementId(n) <> $id and n.insn = $insn DELETE n",
                ImmutableMap.of(
                        "id", relationship.getId(),
                        "insn", relationship.getPropertyContainer().getProperties().get("insn")
                ),
                null);
    }
}
