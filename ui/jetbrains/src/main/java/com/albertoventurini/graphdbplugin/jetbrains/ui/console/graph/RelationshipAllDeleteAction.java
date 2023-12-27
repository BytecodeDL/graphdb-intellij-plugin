package com.albertoventurini.graphdbplugin.jetbrains.ui.console.graph;

import com.albertoventurini.graphdbplugin.database.api.data.GraphRelationship;
import com.albertoventurini.graphdbplugin.jetbrains.actions.execute.ExecuteQueryPayload;
import com.albertoventurini.graphdbplugin.jetbrains.component.datasource.state.DataSourceApi;
import com.google.common.collect.ImmutableMap;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author daozhe@alibaba-inc.com
 * @date 2023/12/10 16:31
 */
public class RelationshipAllDeleteAction extends AbstractRelationshipCurdAction {

    RelationshipAllDeleteAction(String title, String description, Icon icon, DataSourceApi dataSource, GraphRelationship relationship) {
        super(title, description, icon, dataSource, relationship);
    }

    @Override
    public ExecuteQueryPayload getQuery() {
        return new ExecuteQueryPayload("MATCH ()-[n:Call]->() WHERE n.insn = $insn DELETE n",
                ImmutableMap.of(
                        "insn", relationship.getPropertyContainer().getProperties().get("insn")
                ),
                null);
    }
}
