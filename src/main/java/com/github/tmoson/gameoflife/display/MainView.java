package com.github.tmoson.gameoflife.display;

import com.github.tmoson.gameoflife.Simulation;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

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
    stepButton.setOnAction(
        clickEvent -> {
          simulation.step();
          draw();
        });
    canvas = new Canvas(850, 850);
    this.canvas.setOnMousePressed(this::handleDraw);
    this.canvas.setOnMouseDragged(this::handleDraw);
    this.getChildren().addAll(stepButton, canvas);
    affine = new Affine();
    // really need to take an arg or something to set these sizes
    // not sure I want to add spring in here, so I might go the arg route
    affine.appendScale(850 / 100f, 850 / 100f);
    simulation = new Simulation(100, 100);
    initializeSimulation();
  }

  private void handleDraw(MouseEvent event) {
    double mouseX = event.getX();
    double mouseY = event.getY();
    Point2D simCoord = null;
    try {
      simCoord = affine.inverseTransform(mouseX,mouseY);
    } catch (NonInvertibleTransformException e) {
      System.out.println("Could not invert transform, this shouldn't happen.");
    }
    simulation.toggle((int) simCoord.getX(), (int) simCoord.getY());
    this.draw();
  }

  private void initializeSimulation() {
    for (int x = 0; x < simulation.getWidth(); ++x) {
      for (int y = 0; y < simulation.getLength(); y++) {
        if (Math.random() > .68) {
          simulation.setAlive(x, y);
        }
      }
    }
  }

  public void draw() {
    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setTransform(affine);
    //color background
    gc.setFill(Color.LIGHTGRAY);
    gc.fillRect(0, 0, 850, 850);

    // fill in live cells
    gc.setFill(Color.LIGHTSTEELBLUE);
    for (int y = 0; y < simulation.getLength(); ++y) {
      for (int x = 0; x < simulation.getWidth(); ++x) {
        if (this.simulation.getState(x, y) == 1) {
          gc.fillRect(x, y, 1, 1);
        }
      }
    }

    // draw grid lines
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
