package com.github.tmoson.gameoflife.display;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;

public class CustomToolBar extends ToolBar {
  private MainView view;
  private Button step, clear, last;
  private ToggleButton draw, simulate;

  public CustomToolBar(MainView view) {
    this.view = view;
    draw = new ToggleButton("Edit");
    draw.setSelected(false);
    step = new Button("Step");
    clear = new Button("Clear");
    last = new Button("Last");
    simulate = new ToggleButton("Simulate");
    simulate.setSelected(false);


    this.getItems().addAll(draw, clear, step, last, simulate);
  }

  private void setButtonActions(){
    draw.setOnAction(this::handleEdit);
    step.setOnAction(this::handleStep);
    clear.setOnAction(this::handleClear);
    last.setOnAction(this::handleLast);
    simulate.setOnAction(this::handleSimulate);
  }

  private void handleSimulate(ActionEvent actionEvent) {
    if(view.isSimulating()){
      draw.setDisable(false);
      step.setDisable(false);
      clear.setDisable(false);
      last.setDisable(false);
      simulate.setSelected(false);
      //toggle simulation
    } else {
      draw.setDisable(true);
      step.setDisable(true);
      clear.setDisable(true);
      last.setDisable(true);
      simulate.setSelected(true);
    }
  }

  private void handleLast(ActionEvent actionEvent) {
    view.lastSimulation();
  }

  private void handleClear(ActionEvent actionEvent) {
    view.setSimulating(false);
    view.clearSimulation();
  }

  private void handleEdit(ActionEvent actionEvent) {
    view.setEdit();
  }

  private void handleStep(ActionEvent actionEvent) {
    view.setSimulating(true);
    view.stepSimulation();
    view.draw();
  }
}
