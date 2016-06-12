package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.console.graph;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.popup.BalloonPopupBuilderImpl;
import com.intellij.ui.table.JBTable;
import com.intellij.util.messages.MessageBus;
import com.neueda.jetbrains.plugin.graphdb.database.api.data.GraphEntity;
import com.neueda.jetbrains.plugin.graphdb.database.api.data.GraphNode;
import com.neueda.jetbrains.plugin.graphdb.database.api.data.GraphRelationship;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.GraphConstants;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.console.ConsoleToolWindow;
import com.neueda.jetbrains.plugin.graphdb.visualization.PrefuseVisualization;
import com.neueda.jetbrains.plugin.graphdb.visualization.services.LookAndFeelService;
import prefuse.visual.VisualItem;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.StringJoiner;

public class GraphPanel {

    private static final int LABEL_TEXT_WIDTH = 300;

    private PrefuseVisualization visualization;
    private LookAndFeelService lookAndFeelService;
    private JLabel entityDataTableLabel;
    private JBTable entityDataTable;
    private BalloonBuilder balloonPopupBuilder;
    private Balloon balloon;
    private JBLabel balloonLabel = new JBLabel();
    private DefaultTableModel entityDataTableModel;
    private GraphPanelInteractions interactions;

    public GraphPanel() {
        entityDataTableModel = new DefaultTableModel();
    }

    public void initialize(ConsoleToolWindow consoleToolWindow, MessageBus messageBus) {
        this.lookAndFeelService = consoleToolWindow.getLookAndFeelService();
        this.entityDataTableLabel = consoleToolWindow.getGraphEntityDataTableLabel();
        this.entityDataTable = consoleToolWindow.getGraphEntityDataTable();

        // Actions
        final ActionGroup consoleActionGroup = (ActionGroup)
                ActionManager.getInstance().getAction(GraphConstants.Actions.CONSOLE_ACTIONS);

        ActionToolbar consoleToolbar = ActionManager.getInstance()
                .createActionToolbar(GraphConstants.ToolWindow.CONSOLE_TOOL_WINDOW, consoleActionGroup, false);
        final Box toolBarBox = Box.createHorizontalBox();
        toolBarBox.add(consoleToolbar.getComponent());
        consoleToolWindow.getGraphToolbarPanel().add(toolBarBox);

        // Bootstrap visualisation
        visualization = new PrefuseVisualization(lookAndFeelService);
        consoleToolWindow.getGraphCanvas().add(visualization.getCanvas());

        // Entity data table
        entityDataTableModel.addColumn("key");
        entityDataTableModel.addColumn("value");
        entityDataTable.setModel(entityDataTableModel);

        // Tooltips
        balloonBuilder();

        // Interactions
        this.interactions = new GraphPanelInteractions(
                consoleToolWindow,
                messageBus,
                visualization);
    }

    public void showNodeData(GraphNode node, VisualItem item, MouseEvent e) {
        StringJoiner stringJoiner = new StringJoiner(":", ":", "");
        node.getTypes().forEach(stringJoiner::add);
        entityDataTableLabel.setText(node.getRepresentation());
        showEntityData(node);
    }

    public void showRelationshipData(GraphRelationship relationship, VisualItem item, MouseEvent e) {
        StringJoiner stringJoiner = new StringJoiner(":", ":", "");
        relationship.getTypes().forEach(stringJoiner::add);
        entityDataTableLabel.setText(relationship.getRepresentation());
        showEntityData(relationship);
    }

    private void showEntityData(GraphEntity entity) {
        for (int i = entityDataTableModel.getRowCount() - 1; i >= 0; i--) {
            entityDataTableModel.removeRow(i);
        }

        for (Map.Entry<String, Object> entry : entity.getPropertyContainer().getProperties().entrySet()) {
            Object[] data = {entry.getKey(), entry.getValue()};
            entityDataTableModel.addRow(data);
        }
    }

    public void showTooltip(GraphEntity entity, VisualItem item, MouseEvent e) {
        if (balloon != null && !balloon.isDisposed())
            balloon.hide();

        balloonPopupBuilder.setTitle(entity.getRepresentation());
        balloonLabel.setText(getFiveProperties(entity.getPropertyContainer().getProperties()));

        balloon = balloonPopupBuilder.createBalloon();
        Container panel = e.getComponent().getParent();

        final int MAGIC_NUMBER = 15;
        int heightOffset = balloon.getPreferredSize().height / 2 + MAGIC_NUMBER;

        int widthOffset;
        if (e.getX() > panel.getWidth() / 2) {
            widthOffset = balloon.getPreferredSize().width / 2;
        } else {
            widthOffset = panel.getWidth() - balloon.getPreferredSize().width / 2;
        }

        balloon.show(new RelativePoint(panel, new Point(widthOffset, heightOffset)), Balloon.Position.below);
    }

    public void resetPan(GraphNode n, VisualItem item, MouseEvent e) {
        visualization.resetPan();
    }

    private String getFiveProperties(Map<String, Object> properties) {
        StringBuilder sb = new StringBuilder();

        properties.entrySet().stream()
                .limit(5)
                .forEach(entry -> sb
                        .append("<p width=\"" + LABEL_TEXT_WIDTH + "px\"><b>")
                        .append(entry.getKey())
                        .append("</b>: ")
                        .append(entry.getValue())
                        .append("</p>"));

        return "<html>" + sb.toString() + "</html>";
    }

    private void balloonBuilder() {
        final BalloonPopupBuilderImpl builder = new BalloonPopupBuilderImpl(null, balloonLabel);

        final Color bg = lookAndFeelService.getBackgroundColor();
        final Color borderOriginal = lookAndFeelService.getEdgeStrokeColor();
        final Color border = ColorUtil.toAlpha(borderOriginal, 75);
        builder
                .setShowCallout(false)
                .setDialogMode(false)
                .setAnimationCycle(20)
                .setFillColor(bg).setBorderColor(border).setHideOnClickOutside(true)
                .setHideOnKeyOutside(false)
                .setHideOnAction(false)
                .setCloseButtonEnabled(false)
                .setShadow(true);

        balloonPopupBuilder = builder;
    }
}