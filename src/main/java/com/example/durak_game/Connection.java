package com.example.durak_game;

import javafx.scene.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Connection implements Runnable {
    private boolean gameStarted = false;
    private boolean initializeGame = false;
    private boolean changeMove = false;
    private boolean end = false;
    private int currentCount = 0;
    private int currentActivePlayer;
    private ArrayList<Card> table;
    private Deck deck;
    private Card trumpCard;
    private boolean usedTrumpCard;
    private ArrayList<PrintWriter> outputs = new ArrayList<>();
    private ArrayList<BufferedReader> inputs = new ArrayList<>();
    private ArrayList<Player2> players = new ArrayList<>();

    public Connection(BufferedReader in, PrintWriter out, Player2 h) {
        this.inputs.add(in);
        this.outputs.add(out);
        this.players.add(h);
        this.currentCount += 1;
        this.sendDataToAll("waiting");
    }

    public void addStreams(BufferedReader in, PrintWriter out, Player2 p) {
        if (this.currentCount < 2) {
            this.inputs.add(in);
            this.outputs.add(out);
            sendData("join_session_success", out);
            this.players.add(p);
            this.currentCount += 1;
            sendDataToAll("started");
            gameStarted = true;
        } else {
            sendData("join_session_failed", out);
        }
    }

    @Override
    public void run() {
        while(true) {
            while(gameStarted){
                if(!initializeGame) {
                    startGame();
                    initializeGame = true;
                }
                String data = readData(currentActivePlayer);
                System.out.println(data);
                parseData(data);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void parseData(String data) {
        // take_card | discard | drop_card#card
        String[] params = data.split("#");
        String operation = params[0];
        switch (operation) {
            case "take_card": {
                try {
                    System.out.println("взятие карт");
                    players.get(this.currentActivePlayer).getHand().addCards(table);
                    table.clear();
                    cardDrow();
                    for (Player2 p : players) {
                        p.setAttacker(false);
                    }
                    changeMove = true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "discard": {
                try {
                    System.out.println("отбой карт");
                    table.clear();
                    cardDrow();
                    for (Player2 p : players) {
                        p.setAttacker(false);
                    }
                    changeMove = true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "drop_card": {
                System.out.println("операция дроп карты");
                Card dropped = new Card(params[1]);
                System.out.println("карта: " + dropped.toString());
                try {
                    if (canPutInTable(dropped)) {
                        System.out.println("можно положить карту");
                        if (table.isEmpty()) {
                            System.out.println("назначение атакующего");
                            players.get(this.currentActivePlayer).setAttacker(true);
                        }
                        Player2 p = players.get(this.currentActivePlayer);
                        Hand2 h = p.getHand();
                        System.out.println("рука игрока до - " + h.toString());
                        table.add(h.getCard(dropped));
                        System.out.println("рука игрока после - " + h.toString());
                        System.out.println("карта была положена на стол");
                        changeMove = true;
                        System.out.println("ход поменялся");
                    } else {
                        System.out.println("нельзя положить карту");
                        changeMove = false;
                        System.out.println("ход не поменялся");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        sendGameStateAndPermission();
    }

    public void sendData(String data, PrintWriter out) {
        out.println(data);
    }

    public void sendDataToAll(String data) {
        for (PrintWriter out : outputs) {
            out.println(data);
        }
    }

    private String readData(int currentActivePlayer) { // operation, card
        String data = "";
        try {
            data += this.inputs.get(currentActivePlayer).readLine();
        } catch (IOException ex) {
            System.out.println("Error Occurred in readObject in ServerSession: " + ex.toString());
        }
        return data;
    }

    private void startGame() {
        deck = new Deck();
        deck.shuffle();
        table = new ArrayList<>();
        usedTrumpCard = false;
        try {
            for (int i = 0; i < 6; i++) {
                for (Player2 p : players) {
                    p.getHand().addCard(deck.drawCard());
                }
            }
            trumpCard = deck.drawCard();
            currentActivePlayer = whoMakeFirstMove();
            for(int i = 0; i < currentCount; i++) {
                String data = "start_game#";
                data += getPlayerCard(i)  + "#" + trumpCard.toString() + "#" + getPermission(i);
                sendData(data, outputs.get(i));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getPlayerCard(int indexPlayer) {
        if(getPlayerByIndex(indexPlayer).getHand().getCardsInHand().isEmpty()) {
            return "null";
        }
        return getPlayerByIndex(indexPlayer).getHand().getCardsInHand().stream().map(Object::toString).collect(Collectors.joining(","));
    }

    private String getPermission(int indexPlayer) {
        if(indexPlayer == currentActivePlayer) {
            return "true";
        }
        return "false";
    }

    private Player2 getPlayerByIndex(int i) {
        return players.get(i);
    }

    private String getEndGame() {
        String data = "";
        boolean result = checkWinningState();
        System.out.println("конец игры - " + result);
        String winner_state = result ? "true" : "false";
        System.out.println("отправление данных о конце игры");
        data += result + "," + winner_state;
        return data;
    }

    private String getTextAction(int indexPlayer) {
        if (!table.isEmpty()) {
            if (getPlayerByIndex(indexPlayer).isAttacker()) {
                return "Бито";
            } else {
                return "Взять";
            }
        }
        return "null";
    }

    private void changeMove() {
        if (changeMove) {
            if (this.currentActivePlayer == 1) {
                this.currentActivePlayer = 0;
            } else {
                this.currentActivePlayer += 1;
            }
            System.out.println("ход был изменен");
        }
    }

    private void sendGameStateAndPermission() {
        //read_game#card1, card2, card3 ...#usedTrump#sizeDeck#endGame,nickname#permission#hand#textAction
        changeMove();
        for(int i = 0; i < currentCount; i++) {
            String data = "read_game#";
            if(table.isEmpty()) {
                data += "null#";
            }
            else {
                data += table.stream().map(Object::toString)
                        .collect(Collectors.joining(",")) + "#";
            }
            data += usedTrumpCard + "#";
            data += deck.getSizeOfDeck() + "#";
            data += getEndGame() + "#";
            data += getPlayerCard(i) + "#";
            data += getTextAction(i) + "#";
            data += getPermission(i);
            sendData(data, outputs.get(i));
        }
    }

    private void closeStreams() {
        for (int i = 0; i < outputs.size(); i++) {
            this.outputs.get(i).close();
        }
    }

    private boolean checkWinningState() {
        if (!deck.isEmpty() || !usedTrumpCard) {
            return false;
        }
        for (Player2 p : players) {
            if (p.getHand().getCardsInHand().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void cardDrow() throws Exception {
        if (players.get(0).getHand().getCardsInHand().size() >= 6 && players.get(1).getHand().getCardsInHand().size() >= 6) {
            return;
        }
        Player2 attacker = players.get(0).isAttacker() ? players.get(0) : players.get(1);
        Player2 defending = players.get(0).isAttacker() ? players.get(1) : players.get(0);
        if (deck.isEmpty()) {
            if (!usedTrumpCard) {
                if (attacker.getHand().getCardsInHand().size() >= 6) {
                    defending.getHand().addCard(trumpCard);
                } else {
                    attacker.getHand().addCard(trumpCard);
                }
                usedTrumpCard = true;
            }
        } else {
            while (attacker.getHand().getCardsInHand().size() < 6 && !deck.isEmpty()) {
                attacker.getHand().addCard(deck.drawCard());
            }
            while (defending.getHand().getCardsInHand().size() < 6 && !deck.isEmpty()) {
                defending.getHand().addCard(deck.drawCard());
            }
        }
    }

    private boolean canPutInTable(Card pcard) {
        if (table.isEmpty()) {
            return true;
        } else {
            if (players.get(currentActivePlayer).isAttacker()) {
                for (Card card : table) {
                    if (pcard.getValue().getRank() == card.getValue().getRank()) {
                        return true;
                    }
                }
            } else {
                Card card = table.get(table.size() - 1);
                if (card.getSuit() == trumpCard.getSuit()) {
                    if (pcard.getSuit() == card.getSuit()) {
                        return pcard.getValue().getRank() >= card.getValue().getRank();
                    }
                } else if (pcard.getSuit() == trumpCard.getSuit() && card.getSuit() != trumpCard.getSuit()) {
                    return true;
                } else
                    return card.getSuit() == pcard.getSuit() && card.getValue().getRank() <= pcard.getValue().getRank();
            }
            return false;
        }
    }


    private int whoMakeFirstMove() {
        int min_value = 15;
        for (Card c : players.get(0).getHand().getCardsInHand()) {
            if (c.getSuit() == trumpCard.getSuit()) {
                int rank = c.getValue().getRank();
                if (min_value > rank) {
                    min_value = rank;
                }
            }
        }
        for (Card c : players.get(1).getHand().getCardsInHand()) {
            if (c.getSuit() == trumpCard.getSuit()) {
                int rank = c.getValue().getRank();
                if (min_value > rank) {
                    return 1;
                }
            }
        }
        return 0;
    }
}
