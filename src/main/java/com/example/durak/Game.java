package com.example.durak;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    private int currentPlayer;
    private String[] playerIds;
    private Deck deck;
    private ArrayList<ArrayList<Card>> playerHands;
    private ArrayList<Card> stockpile;
    private Card trumpCard;

    private Card.Suit validSuit;
    private Card.Value validValue;

    public Game(String[] playersID) throws Exception {
        deck = new Deck();
        deck.shuffle();
        stockpile = new ArrayList<>();
        playerIds = playersID;
        currentPlayer = 0;
        playerHands = new ArrayList<ArrayList<Card>>();
        for(int i = 0; i < playersID.length; i++) {
            ArrayList<Card> hand = new ArrayList<>(Arrays.asList(deck.drawCard(6)));
            stockpile.addAll(hand);
            playerHands.add(hand);
        }
    }

    public void start(Game game) throws Exception {
        boolean hod = true;
        trumpCard = deck.drawCard();

    }
}
