package com.github.tmoson.gameoflife.display;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

public class CustomToolBar extends ToolBar {
  private MainView view;

  public CustomToolBar(MainView view) {
    this.view = view;
    Button draw = new Button("Edit");
    draw.setOnAction(this::handleEdit);
    Button step = new Button("Step");
    step.setOnAction(this::handleStep);
    this.getItems().addAll(draw, step);
  }

  private void handleEdit(ActionEvent actionEvent) {
    view.setEdit();
  }

  private void handleStep(ActionEvent actionEvent) {
    view.stepSimulation();
    view.draw();
  }
}
