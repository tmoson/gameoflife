package com.github.tmoson.gameoflife.display;

import com.github.tmoson.gameoflife.Simulation;
import com.github.tmoson.gameoflife.SimulationProperties;
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
  private final Canvas canvas;
  private final Affine affine;
  private final int canvasLength, canvasWidth, simLength, simWidth;

  private SimulationProperties props;
  private Simulation simulation;
  private boolean edit;
  private boolean simulating, rewinding;

  private class State {
    Simulation value;
    State previous;

    public State(Simulation current) {
      value = current;
      previous = null;
    }

    public State(Simulation current, State prev) {
      value = current;
      previous = prev;
    }
  }

  State current;

  public MainView() {

    props = new SimulationProperties();
    canvasLength = Integer.parseInt(props.getProperty("canvas.length"));
    canvasWidth = Integer.parseInt(props.getProperty("canvas.width"));
    simLength = Integer.parseInt(props.getProperty("simulation.length"));
    simWidth = Integer.parseInt(props.getProperty("simulation.width"));
    edit = false;
    simulating = false;
    rewinding = false;
    CustomToolBar toolBar = new CustomToolBar(this);
    canvas = new Canvas(canvasWidth, canvasLength);
    this.canvas.setOnMousePressed(this::handleDraw);
    this.canvas.setOnMouseDragged(this::handleDraw);
    this.canvas.setOnMouseMoved(this::handleMoved);

    Pane spacer = new Pane();
    spacer.setMinSize(0, 0);
    spacer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    VBox.setVgrow(spacer, Priority.ALWAYS);

    this.getChildren().addAll(toolBar, canvas, spacer);
    affine = new Affine();
    affine.appendScale(canvasWidth / (float) simWidth, canvasLength / (float) simLength);
    simulation = new Simulation(simWidth, simLength);
    if (Boolean.parseBoolean(props.getProperty("random.start"))){
      initializeSimulation();
    }
    current = new State(Simulation.copy(simulation));
  }

  private Point2D getSimulationCoordinates(MouseEvent event) {
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
  }

  private void handleDraw(MouseEvent event) {
    if (edit) {
      Point2D simCoord = getSimulationCoordinates(event);
      simulation.toggle((int) simCoord.getX(), (int) simCoord.getY());
      this.draw();
      current = new State(Simulation.copy(simulation));
    }
  }

  public void setEdit(boolean state) {
    edit = state;
  }

  public void stepSimulation() {
    simulation.step();
    current = new State(Simulation.copy(simulation),current);
  }

  public void lastSimulation(){
    if(current.previous == null) return;
    current = current.previous;
    simulation = current.value;
    draw();
  }

  public boolean hasLast(){
    return current.previous != null;
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

  // might get rid of this, I don't like the workflow the tutorial set out
  private void drawSimulation(Simulation sim) {
    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setTransform(affine);
    gc.setFill(Color.LIGHTSTEELBLUE);
    for (int y = 0; y < sim.getLength(); ++y) {
      for (int x = 0; x < sim.getWidth(); ++x) {
        if (sim.getState(x, y) == Simulation.ALIVE) {
          gc.fillRect(x, y, 1, 1);
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
    drawSimulation(simulation);
    // draw grid lines
    gc.setFill(Color.BLACK);
    gc.setLineWidth(0.05);
    for (int x = 0; x <= simulation.getWidth(); ++x) {
      gc.strokeLine(x, 0, x, simulation.getLength());
    }
    for (int y = 0; y <= simulation.getLength(); ++y) {
      gc.strokeLine(0, y, simulation.getWidth(), y);
    }
  }

  public void setSimulating(boolean state) {
    simulating = state;
  }

  public void setRewinding(boolean rewind){
    rewinding = rewind;
  }

  public boolean isRewinding(){
    return rewinding;
  }

  public boolean isSimulating(){
    return simulating;
  }

  public void clearSimulation() {
    simulation =
        new Simulation(
            Integer.parseInt(props.getProperty("simulation.width")),
            Integer.parseInt(props.getProperty("simulation.length")));
    current = new State(simulation);
    draw();
  }

  public void resetRandom(){
    simulation =
            new Simulation(
                    Integer.parseInt(props.getProperty("simulation.width")),
                    Integer.parseInt(props.getProperty("simulation.length")));
    initializeSimulation();
    current = new State(simulation);
    draw();
  }
}
