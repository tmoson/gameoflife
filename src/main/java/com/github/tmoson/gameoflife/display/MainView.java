package com.github.tmoson.gameoflife.display;

import com.github.tmoson.gameoflife.Simulation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class MainView extends VBox {
  private Button stepButton;
  private Canvas canvas;
  private Affine affine;

  private Simulation simulation;

  public MainView() {
    stepButton = new Button("step");
    stepButton.setOnAction(clickEvent -> {
      simulation.step();
      draw();
    });
    canvas = new Canvas(400, 400);
    this.getChildren().addAll(stepButton, canvas);
    affine = new Affine();
    affine.appendScale(400 / 10f, 400 / 10f);
    simulation = new Simulation(10, 10);

    simulation.setAlive(2,2);
    simulation.setAlive(3,2);
    simulation.setAlive(4,2);

    simulation.setAlive(4,5);

    simulation.setAlive(5,5);
    simulation.setAlive(5,6);
    simulation.setAlive(6,5);
    simulation.setAlive(6,6);
  }

  public void draw() {
    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setTransform(affine);
    gc.setFill(Color.LIGHTGRAY);
    gc.fillRect(0, 0, 450, 450);

    gc.setFill(Color.LIGHTSTEELBLUE);
    for (int y = 0; y < 10; ++y) {
      for (int x = 0; x < 10; ++x) {
        if (this.simulation.getState(x, y) == 1) {
          gc.fillRect(x, y, 1, 1);
        }
      }
    }

    gc.setFill(Color.BLACK);
    gc.setLineWidth(0.025);
    for (int x = 0; x <= 10; ++x) {
      gc.strokeLine(x, 0, x, 10);
    }
    for (int y = 0; y <= 10; ++y) {
      gc.strokeLine(0, y, 10, y);
    }
  }
}
