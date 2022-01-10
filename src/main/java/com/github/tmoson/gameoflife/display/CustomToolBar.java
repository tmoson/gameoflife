package com.github.tmoson.gameoflife.display;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;

public class CustomToolBar extends ToolBar {
  private MainView view;
  private Button step, clear, last, randomize;
  private ToggleButton draw, run, rewind;

  public CustomToolBar(MainView view) {
    this.view = view;
    draw = new ToggleButton("Edit");
    draw.setSelected(false);
    step = new Button("Step");
    clear = new Button("Clear");
    randomize = new Button("Randomize");
    last = new Button("Last");
    run = new ToggleButton("Run");
    run.setSelected(false);
    rewind = new ToggleButton("Rewind");
    rewind.setSelected(false);
    setButtonActions();
    this.getItems().addAll(draw, clear, randomize, step, last, run, rewind);
  }

  private void setButtonActions() {
    draw.setOnAction(this::handleEdit);
    step.setOnAction(this::handleStep);
    clear.setOnAction(this::handleClear);
    randomize.setOnAction(this::handleRandom);
    last.setOnAction(this::handleLast);
    run.setOnAction(this::handleSimulate);
    rewind.setOnAction(this::handleRewind);
  }

  private void handleRandom(ActionEvent actionEvent) {
    view.resetRandom();
  }

  private void handleRewind(ActionEvent actionEvent) {
    Button[] otherButtons = {step, clear, randomize, last};
    ToggleButton[] otherToggles = {draw, run};
    if (view.isRewinding()) {
      view.setRewinding(false);
      enable(otherButtons,otherToggles);
      rewind.setSelected(false);
    } else {
      disable(otherButtons,otherToggles);
      rewind.setSelected(true);
      view.setRewinding(true);
      new Thread(
        () -> {
          while (view.isRewinding() && view.hasLast()) {
            view.lastSimulation();
            try {
              Thread.sleep(250L);
            } catch (InterruptedException ie) {
              System.out.println("Error while pausing for last generation: ");
              ie.printStackTrace();
            }
          }
        }).start();
    }
  }

  private void handleSimulate(ActionEvent actionEvent) {
    Button[] otherButtons = {step, clear, randomize, last};
    ToggleButton[] otherToggles = {draw, rewind};
    if (view.isSimulating()) {
      view.setSimulating(false);
      enable(otherButtons,otherToggles);
      run.setSelected(false);
      // toggle simulation
    } else {
      draw.setSelected(false);
      disable(otherButtons,otherToggles);
      run.setSelected(true);
      view.setEdit(false);
      view.setSimulating(true);
      new Thread(
        () -> {
          while (view.isSimulating()) {
            view.stepSimulation();
            view.draw();
            try {
              Thread.sleep(250L);
            } catch (InterruptedException ie) {
              System.out.println("Error while pausing for next generation: ");
              ie.printStackTrace();
            }
          }
        }).start();
    }
  }

  private void disable(Button[] otherButtons, ToggleButton[] otherToggles){
    for(Button button : otherButtons){
      button.setDisable(true);
    }
    for(ToggleButton toggle : otherToggles){
      toggle.setDisable(true);
    }
  }

  private void enable(Button[] otherButtons, ToggleButton[] otherToggles){
    for(Button button : otherButtons){
      button.setDisable(false);
    }
    for(ToggleButton toggle : otherToggles){
      toggle.setDisable(false);
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
    view.setEdit(draw.isSelected());
  }

  private void handleStep(ActionEvent actionEvent) {
    view.stepSimulation();
    view.draw();
  }
}
