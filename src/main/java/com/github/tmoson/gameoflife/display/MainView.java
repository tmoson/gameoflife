package com.github.tmoson.gameoflife.display;

import com.github.tmoson.gameoflife.Simulation;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

/*
 * @author Tyler Moson
 */
public class MainView extends VBox {
  private Canvas canvas;
  private Affine affine;
  private InfoBar infoBar;

  private Simulation simulation;
  private boolean edit;

  public MainView() {
    edit = true;
    CustomToolBar toolBar = new CustomToolBar(this);
    canvas = new Canvas(850, 850);
    this.canvas.setOnMousePressed(this::handleDraw);
    this.canvas.setOnMouseDragged(this::handleDraw);
    this.canvas.setOnMouseMoved(this::handleMoved);

    infoBar = new InfoBar();
    infoBar.setEdit(edit);
    infoBar.setCursorPosition(0, 0);

    Pane spacer = new Pane();
    spacer.setMinSize(0,0);
    spacer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    VBox.setVgrow(spacer, Priority.ALWAYS);

    this.getChildren().addAll(toolBar, canvas, spacer, infoBar);
    affine = new Affine();
    // really need to take an arg or something to set these sizes
    // not sure I want to add spring in here, so I might go the arg route
    affine.appendScale(850 / 100f, 850 / 100f);
    simulation = new Simulation(100, 100);
    initializeSimulation();
  }

  private Point2D getSimulationCoordinates(MouseEvent event){
    double mouseX = event.getX();
    double mouseY = event.getY();
    try {
      return affine.inverseTransform(mouseX, mouseY);
    } catch (NonInvertibleTransformException e) {
      throw new RuntimeException("Could not invert transform, this shouldn't happen.");
    }
  }

  private void handleMoved(MouseEvent event) {
    Point2D simCoord = getSimulationCoordinates(event);
    infoBar.setCursorPosition((int) simCoord.getX(), (int) simCoord.getY());
  }

  private void handleDraw(MouseEvent event) {
    if (edit) {
      Point2D simCoord = getSimulationCoordinates(event);
      simulation.toggle((int) simCoord.getX(), (int) simCoord.getY());
      this.draw();
    }
  }

  public void setEdit() {
    edit = !edit;
    infoBar.setEdit(edit);
  }

  public void stepSimulation() {
    simulation.step();
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
    // color background
    gc.setFill(Color.LIGHTGRAY);
    gc.fillRect(0, 0, 850, 850);

    // fill in live cells
    gc.setFill(Color.LIGHTSTEELBLUE);
    for (int y = 0; y < simulation.getLength(); ++y) {
      for (int x = 0; x < simulation.getWidth(); ++x) {
        if (this.simulation.getState(x, y) == Simulation.ALIVE) {
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
