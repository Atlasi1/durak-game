package com.example.durak_game;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class Helper {
    public static String getImageString(Card cd) {
        String filename = "images/";
        if (cd.getValue().getRank() >= 6 && cd.getValue().getRank() <= 10) {
            filename += cd.getValue().getRank();
        } else {
            filename += cd.getValue().name().toLowerCase();
        }
        return filename.concat("_of_" + cd.getSuit().name().toLowerCase() + ".png");
    }

    public static void sendError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка подключения");
            alert.setContentText(message);
            alert.showAndWait();
            Platform.exit();
        });
    }

    public static void sendConf(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Player " + message + " has won the game.");
            alert.showAndWait();
            Platform.exit();
        });
    }

    public static Card.Suit getSuit(String str) {
        if (str.equalsIgnoreCase("diamonds")) {
            return Card.Suit.Diamonds;
        }
        if (str.equalsIgnoreCase("spades")) {
            return Card.Suit.Spades;
        }
        if (str.equalsIgnoreCase("hearts")) {
            return Card.Suit.Hearts;
        } else {
            return Card.Suit.Clubs;
        }
    }

    public static Card.Value getValue(String value) {
        value = value.toLowerCase();
        switch (value) {
            case "six": {
                return Card.Value.Six;
            }
            case "seven": {
                return Card.Value.Seven;
            }
            case "eight": {
                return Card.Value.Eight;
            }
            case "nine": {
                return Card.Value.Nine;
            }
            case "ten": {
                return Card.Value.Ten;
            }
            case "jack": {
                return Card.Value.Jack;
            }
            case "queen": {
                return Card.Value.Queen;
            }
            case "king": {
                return Card.Value.King;
            }
            case "ace": {
                return Card.Value.Ace;
            }
        }
        return null;
    }

}
