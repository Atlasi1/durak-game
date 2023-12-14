package com.example.durak_game;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    private FlowPane table;

    @FXML
    private ImageView trumpCardImage;

    private Deck deck = new Deck();
    private Player playerOne, playerTwo;
    private int move; // 1 - first, 2 - second
    private Player attacker;
    private Card trumpCard;
    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() throws Exception {
        playerOne = new Player("player1", 1);
        playerTwo = new Player("player2", 2);
        btnAction.disableProperty().bind(playable);
        btnAction.setOnAction((event) -> {
            Player receiving;
            if (playerOne.getId() == attacker.getId()) {
                receiving = playerTwo;
            } else {
                receiving = playerOne;
            }
            for (Node nd : table.getChildren()) {
                Card cd = (Card) nd.getUserData();
                try {
                    receiving.getHand().addCard(cd);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            table.getChildren().clear();
        });

        labelEnd.setVisible(false);
        startGame();
    }

    private void startGame() throws Exception {
        deck.reset();
        deck.shuffle();
        for (int i = 0; i < 6; i++) {
            for (Player p : Arrays.asList(playerOne, playerTwo)) {
                p.getHand().addCard(deck.drawCard());
            }
        }
        trumpCard = deck.drawCard();
        trumpCardImage.setImage(trumpCard.getImage());
        update();
    }

    private void update() throws Exception {
        if (!isFirstMoveOffPlayerOne()) {
            move = 2;
        } else {
            move = 1;
        }
        setHand(getPlayerByMoveId());
    }

    private void cardDrow(Player firstTaking, Player secondTaking) throws Exception {
        if(deck.getSizeOfDeck() == 0) {
            if(trumpCardImage != null) {
//                if(firstTaking.getHand().sizeProperty().get() == 6) {
//
//                }
                //TODO: доделать раздачу карт на столе
                firstTaking.getHand().addCard((Card) trumpCardImage.getUserData());
                trumpCardImage.setImage(null);
            }
        }
        else {

        }
    }

    private void updateLabels() {
        sizeDeck.setText(Integer.toString(deck.getSizeOfDeck()));
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

    private Player getPlayerByMoveId() {
        if (move == 1) {
            return playerOne;
        }
        return playerTwo;
    }

    private void setHand(Player p) {
        for (Card cd : p.getHand().getCardsInHand()) {
            ImageView iv = new ImageView(cd.getImage());
            iv.setFitHeight(175);
            iv.setFitWidth(100);
            iv.setUserData(cd);
            iv.setOnMouseClicked(mouseEvent -> {
                if(!tableEmpty()) {
                    playable.set(true);
                }
                if (canPutInTable((Card) iv.getUserData())) {
                    if (tableEmpty()) {
                        attacker = p;
                    }
                    try {
                        p.getHand().getCard((Card) iv.getUserData());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    ImageView iv_table = iv;
                    iv_table.setDisable(true);
                    table.getChildren().add(iv_table);
                    hand.getChildren().clear();
                    switchMove();
                    try {
                        setHand(getPlayerByMoveId());
                    } catch (Exception e) {
                        System.out.println("Не получилось поменять руку");
                    }
                }
            });
            hand.getChildren().add(iv);
            if (p == attacker) {
                btnAction.setText("Бито");
            } else {
                btnAction.setText("Взять");
            }
            updateLabels();
        }
    }

    private boolean canPutInTable(Card pcard) {
        if (tableEmpty()) {
            return true;
        } else {
            Card card = (Card) table.getChildren().get(table.getChildren().size() - 1).getUserData();
            if (pcard.getValue().getRank() == card.getValue().getRank()) { // TODO: если подкидной, то это, если нет, то нет
                return true;
            } else if (card.getSuit() == trumpCard.getSuit()) {
                if (pcard.getSuit() == card.getSuit()) {
                    return pcard.getValue().getRank() >= card.getValue().getRank();
                }
            } else if (pcard.getSuit() == trumpCard.getSuit() && card.getSuit() != trumpCard.getSuit()) {
                return true;
            } else return card.getSuit() == pcard.getSuit() && card.getValue().getRank() <= pcard.getValue().getRank();
            return false;
        }
    }

    private void switchMove() {
        if (move == 1) {
            move = 2;
        } else {
            move = 1;
        }
    }

    private boolean tableEmpty() {
        return table.getChildren().isEmpty();
    }


    @FXML
    void handleBtnSurr(ActionEvent e) {
        // TODO: обработка проигрыша и показ лейбла и отлючение всех кнопок и добавление 3 секунд до закрытия
    }


    private void endGame() throws InterruptedException {
        Player winner;
        if (playerOne.getHand().sizeProperty().get() == 0) {
            winner = playerTwo;
        } else {
            winner = playerOne;
        }
        labelEnd.setVisible(true);
        labelEnd.setText(labelEnd.getText() + " " + winner.getName());
        Thread.sleep(1000);
        Platform.exit();
    }

    private boolean isEndGame() {
        return false;
    }


}

