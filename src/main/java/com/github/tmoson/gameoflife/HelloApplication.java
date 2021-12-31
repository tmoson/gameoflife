package com.github.tmoson.gameoflife;

import com.github.tmoson.gameoflife.display.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
  @Override
  public void start(Stage stage) {
    MainView mainView = new MainView();
    Scene scene = new Scene(mainView, 1000, 1000);
    stage.setScene(scene);
    stage.show();
    mainView.draw();
  }

  public static void main(String[] args) {
    launch();
  }
}
