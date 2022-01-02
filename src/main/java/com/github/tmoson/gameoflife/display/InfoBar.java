package com.github.tmoson.gameoflife.display;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class InfoBar extends HBox {

  private static String cursorPosFormat = "Cursor: (%d, %d)";

  private Label cursor;
  private Label editingTool;

  public InfoBar() {
    editingTool = new Label();
    cursor = new Label();

    Pane spacer = new Pane();
    spacer.setMinSize(0,0);
    spacer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    HBox.setHgrow(spacer, Priority.ALWAYS);
    this.getChildren().addAll(this.cursor, spacer, this.editingTool);
  }

  public void setEdit(boolean edit){
    if(edit){
      editingTool.setText("Editing");
    } else {
      editingTool.setText("Viewing");
    }
  }

  public void setCursorPosition(int x, int y){
    cursor.setText(String.format(cursorPosFormat, x, y));
  }

}
