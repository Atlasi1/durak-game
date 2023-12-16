package com.example.durak_game;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameController {

    @FXML
    public Button btnQuit;
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

    @FXML
    private Label playerId;

    private Deck deck = new Deck();
    private Player playerOne, playerTwo;
    private int move; // 1 - first, 2 - second
    private Card trumpCard;
    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty game = new SimpleBooleanProperty(false);
    private CardComparator comparator = new CardComparator();

    @FXML
    public void initialize() throws Exception {
        playerOne = new Player("player1", 1);
        playerTwo = new Player("player2", 2);
        btnSurr.visibleProperty().bind(game);
        btnQuit.visibleProperty().bind(game.not());
        btnAction.disableProperty().bind(playable.not());
        btnAction.setOnAction((event) -> {
            try {
                if (btnAction.getText() == "Взять") {
                    for (Node nd : table.getChildren()) {
                        Card cd = (Card) nd.getUserData();
                        getPlayerByMoveId().getHand().addCard(cd);
                    }
                }
                table.getChildren().clear();
                playable.set(false);
                playerOne.setAttacker(false);
                playerTwo.setAttacker(false);
                cardDrow();
                next();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        btnQuit.setOnMouseClicked((event) -> Platform.exit());
        btnSurr.setOnMouseClicked((event) -> {
            try {
                Player player = playerOne == getPlayerByMoveId() ? playerTwo : playerOne;
                endGame(player);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        labelEnd.setVisible(false);

        startGame();

    }

    private void startGame() throws Exception {
        game.set(true);
        deck.reset();
        deck.shuffle();
        for (int i = 0; i < 6; i++) {
            for (Player p : Arrays.asList(playerOne, playerTwo)) {
                p.getHand().addCard(deck.drawCard());
            }
        }
        trumpCard = deck.drawCard();
        trumpCardImage.setImage(trumpCard.getImage());
        if (!isFirstMoveOffPlayerOne()) {
            move = 2;
        } else {
            move = 1;
        }
        setHand(getPlayerByMoveId());
    }

    private void cardDrow() throws Exception {
        if (playerOne.getHand().sizeProperty().get() == 6 && playerTwo.getHand().sizeProperty().get() == 6) {
            return;
        }
        Player attacker = playerOne.isAttacker() ? playerOne : playerTwo;
        Player defending = playerOne.isAttacker() ? playerTwo : playerOne;
        if (deck.isEmpty()) {
            if (trumpCardImage.getImage() != null) {
                if (attacker.getHand().sizeProperty().get() >= 6) {
                    defending.getHand().addCard(trumpCard);
                } else {
                    attacker.getHand().addCard(trumpCard);
                }
                trumpCardImage.setImage(null);
            }
        } else {
            while (attacker.getHand().sizeProperty().get() < 6 && !deck.isEmpty()) {
                attacker.getHand().addCard(deck.drawCard());
            }
            while (defending.getHand().sizeProperty().get() < 6 && !deck.isEmpty()) {
                defending.getHand().addCard(deck.drawCard());
            }
        }
    }

    private void updateLabels() {
        sizeDeck.setText(Integer.toString(deck.getSizeOfDeck()));
        playerId.setText(getPlayerByMoveId().getName());
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
                if (canPutInTable((Card) iv.getUserData())) {
                    if (tableEmpty()) {
                        p.setAttacker(true);
                        playable.set(true);
                    }
                    try {
                        p.getHand().getCard((Card) iv.getUserData());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    ImageView iv_table = iv;
                    iv_table.setDisable(true);
                    table.getChildren().add(iv_table);
                    try {
                        next();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            hand.getChildren().add(iv);
            updateLabels();
        }
        FXCollections.sort(hand.getChildren(), comparator);
    }

    private boolean canPutInTable(Card pcard) {
        if (tableEmpty()) {
            return true;
        } else {
            if(getPlayerByMoveId().isAttacker()) {
                ArrayList<Card> cards = new ArrayList<>();
                for(Node nd: table.getChildren()) {
                    cards.add((Card)nd.getUserData());
                }
                for(Card card: cards) {
                    if(pcard.getValue().getRank() == card.getValue().getRank()) {
                        return true;
                    }
                }
                return false;
            }
            else {
                Card card = (Card) table.getChildren().get(table.getChildren().size() - 1).getUserData();
                if (card.getSuit() == trumpCard.getSuit()) {
                    if (pcard.getSuit() == card.getSuit()) {
                        return pcard.getValue().getRank() >= card.getValue().getRank();
                    }
                } else if (pcard.getSuit() == trumpCard.getSuit() && card.getSuit() != trumpCard.getSuit()) {
                    return true;
                } else return card.getSuit() == pcard.getSuit() && card.getValue().getRank() <= pcard.getValue().getRank();
                return false;
            }
        }
    }

    private void next() throws IOException {
        if (isEndGame()) {
            endGame(null);
        }
        hand.getChildren().clear();
        switchMove();
        if (getPlayerByMoveId().isAttacker()) {
            btnAction.setText("Бито");
        } else {
            btnAction.setText("Взять");
        }
        try {
            setHand(getPlayerByMoveId());
        } catch (Exception e) {
            System.out.println("Не получилось поменять руку");
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


    private void endGame(Player p) throws IOException {
        Player winner;
        if(p != null) {
            winner = p;
        }
        else if (playerOne.getHand().sizeProperty().get() == 0) {
            winner = playerTwo;
        }
        else {
            winner = playerOne;
        }
        labelEnd.setVisible(true);
        labelEnd.setText(labelEnd.getText() + " " + winner.getName());
        game.set(false);
    }

    private boolean isEndGame() {
        if (playerOne.getHand().sizeProperty().get() == 0) {
            return true;
        } else if (playerTwo.getHand().sizeProperty().get() == 0) {
            return true;
        }
        return false;
    }


}

