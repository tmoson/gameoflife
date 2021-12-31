package com.github.tmoson.gameoflife.display;

import com.github.tmoson.gameoflife.Simulation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;


/*
 * @author Tyler Moson
 */
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
    canvas = new Canvas(850, 850);
    this.getChildren().addAll(stepButton, canvas);
    affine = new Affine();
    affine.appendScale(850 / 200f, 850 / 200f);
    simulation = new Simulation(200, 200);
    initializeSimulation();
  }

  private void initializeSimulation(){
    for (int x = 0; x < simulation.getWidth(); ++x) {
      for (int y = 0; y < simulation.getLength(); y++) {
        if((Math.random() * 100) > 68) {
          simulation.setAlive(x,y);
        }
      }
    }
  }

  public void draw() {
    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setTransform(affine);
    gc.setFill(Color.LIGHTGRAY);
    gc.fillRect(0, 0, 200, 200);

    gc.setFill(Color.LIGHTSTEELBLUE);
    for (int y = 0; y < simulation.getLength(); ++y) {
      for (int x = 0; x < simulation.getWidth(); ++x) {
        if (this.simulation.getState(x, y) == 1) {
          gc.fillRect(x, y, 1, 1);
        }
      }
    }

    gc.setFill(Color.BLACK);
    gc.setLineWidth(0.05);
    for (int x = 0; x <= simulation.getWidth(); ++x) {
      gc.strokeLine(x, 0, x, simulation.getWidth());
    }
    for (int y = 0; y <= simulation.getLength(); ++y) {
      gc.strokeLine(0, y, simulation.getLength(), y);
    }
  }
}
