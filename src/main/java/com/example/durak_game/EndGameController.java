package com.example.durak_game;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EndGameController {
    @FXML private Label playerId;
    private String winner;

    public EndGameController(String playerID) {

        winner = playerID;
    }

    @FXML
    public void initialize() {
        playerId.setText(winner);
        playerId.setVisible(true);
    }
}
