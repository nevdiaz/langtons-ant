package edu.cnm.deepdive.langton.controller;

import edu.cnm.deepdive.langton.model.Terrain;
import edu.cnm.deepdive.langton.view.TerrainView;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;

public class MainController {

  @FXML private Button resetter;
  @FXML private TerrainView terrainView;
  @FXML private ToggleButton runToggle;
  @FXML private Slider populationSize;
  private boolean running;
  private Terrain terrain;
  private AnimationTimer timer;

  @FXML
  private void initialize() {
    timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        terrainView.draw(terrain.getPatches());
      }
    };
    reset();
  }

  @FXML
  private void toggleRun(ActionEvent actionEvent) {
    if (runToggle.isSelected()) {
      start();
    } else {
      stop();
    }
  }

  private void start() {
    running = true;
    resetter.setDisable(true);
    timer.start();
    new Runner().start();
  }

  public void stop() {
    runToggle.setDisable(true);
    running = false;
    timer.stop();
  }

  @FXML
  private void reset() {
    terrain = new Terrain((int) populationSize.getValue(), new Random());
    terrain.tick();
    terrainView.draw(terrain.getPatches());
  }

  private class Runner extends Thread {

    @Override
    public void run() {
      while (running) {
        for (int i = 0; i < 10; i++) {
          terrain.tick();
        }
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          // DO NOTHING! GET ON WITH YOUR LIFE!
        }
      }
      Platform.runLater(() -> {
        runToggle.setDisable(false);
        resetter.setDisable(false);
      });
    }

  }

}
