package com.example.durak_game;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Objects;

public class Game2Controller {

    @FXML
    private Button btnAction;

    @FXML
    private HBox hand;

    @FXML
    private Label sizeDeck;

    @FXML
    private FlowPane table;

    @FXML
    private ImageView trumpCardImage;

    @FXML
    private Label labelTrumpCard;
    @FXML
    private Label labelMove;
    private final CardComparator<Node> comparator = new CardComparator<>();

    public void setCardsInHand(Hand2 updating) {
        System.out.println("обработка руки игрока...");
        hand.getChildren().clear();
        if(updating != null) {
            for (Card cd : updating.getCardsInHand()) {
                System.out.println("обрабатывается карта - " + cd.toString());
                ImageView iv = new ImageView(cd.getImage());
                iv.setFitHeight(175);
                iv.setFitWidth(100);
                iv.setUserData(cd);
                iv.setOnMouseClicked(this::drop);
                hand.getChildren().add(iv);
            }
            FXCollections.sort(hand.getChildren(), comparator);
        }
    }

    public void setDisableActions(boolean flag) {
        btnAction.setDisable(!flag);
        for(Node nd: hand.getChildren()) {
            nd.setDisable(!flag);
        }
        labelMove.setVisible(flag);
    }

    public void setEndGame(String nick) {
        Helper.sendConf(nick);
    }

    public void setTrumpCardImage(Card cd) {
        trumpCardImage.setImage(cd.getImage());
    }

    public void setTextAction(String str) {
        btnAction.setText(str);
        if(Objects.equals(str, "Бито")) {
            btnAction.setOnAction(this::discard);
        }
        else if(Objects.equals(str, "Взять")) {
            btnAction.setOnAction(this::take);
        }
        else {
            btnAction.setDisable(true);
        }
    }

    public void trumpCardUsed() {
        trumpCardImage.setImage(null);
        labelTrumpCard.setVisible(false);
    }

    public void setSize(int i) {
        sizeDeck.setText(Integer.toString(i));
    }

    public void setTable(ArrayList<Card> t) {
        table.getChildren().clear();
        if(!t.isEmpty()) {
            for(Card cd: t) {
                ImageView iv = new ImageView(cd.getImage());
                iv.setFitHeight(175);
                iv.setFitWidth(100);
                iv.setUserData(cd);
                iv.setDisable(true);
                table.getChildren().add(iv);
            }
        }
    }

    public void drop(MouseEvent event) {
        ImageView iv = (ImageView) event.getSource();
        Card cd = (Card) iv.getUserData();
        Game2Application.client.sendGameMove("drop_card", cd);
                System.out.println("отправил, что я выкинул: " + iv.getUserData().toString());
    }

    public void take(ActionEvent event) {
        Game2Application.client.sendGameMove("take_card", null);
        System.out.println("отправил, что я взял карты");
    }

    public void discard(ActionEvent event) {
        Game2Application.client.sendGameMove("discard", null);
        System.out.println("отправил, что я нажал бито");
    }
}


