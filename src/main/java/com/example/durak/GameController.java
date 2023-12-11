package com.example.durak;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class GameController {


    @FXML
    private Button btnAction;

    @FXML
    private Button btnSurr;

    @FXML
    private HBox hand;

    @FXML
    private Label labelEnd;

    @FXML
    private Label sizeDeck;

    @FXML
    private Label sizeDiscard;

    @FXML
    private FlowPane table;

    @FXML
    private ImageView trumpCardImage;

    private Deck deck = new Deck();
    private ArrayList<Card> discard = new ArrayList<>();
    private Player playerOne, playerTwo;
    private int move; // 1 - first, 2 - second
    private Card trumpCard;
    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);
    private SimpleStringProperty actionOnBtn = new SimpleStringProperty(); // взять / бито

    @FXML
    public void initialize() {
        playerOne = new Player("player1");
        playerTwo = new Player("player2");

    }

    private void startGame() throws Exception {
        playable.set(true);
        deck.reset();
        deck.shuffle();
        for (int i = 0; i < 6; i++) {
            for (Player p : Arrays.asList(playerOne, playerTwo)) {
                p.getHand().addCard(deck.drawCard());
            }
        }
        trumpCard = deck.drawCard();
        if (!isFirstMoveOffPlayerOne()) {
            move = 2;
        } else {
            move = 1;
        }
    }

    private boolean isFirstMoveOffPlayerOne() {
        int min_value = 15;
        for (Card c : playerOne.getHand().getCardsInHand()) {
            if (c.getSuit() == trumpCard.getSuit()) {
                int rank = c.getValue().getRank();
                if (min_value > rank) {
                    min_value = rank;
                }
            }
        }
        for (Card c : playerTwo.getHand().getCardsInHand()) {
            if (c.getSuit() == trumpCard.getSuit()) {
                int rank = c.getValue().getRank();
                if (min_value > rank) {
                    return false;
                }
            }
        }
        return true;
    }


    @FXML
    void handleBtnSurr(ActionEvent e) {
        // TODO: обработка проигрыша и показ лейбла и отлючение всех кнопок и добавление 3 секунд до закрытия
    }

    private void endGame() {
        //
    }



}

