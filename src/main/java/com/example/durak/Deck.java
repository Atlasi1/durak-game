package com.example.durak;

import java.util.Random;
import javax.swing.ImageIcon;

public class Deck {
    private final int CAPACITY = 36;
    private Card[] cards;
    private int sizeOfDeck;

    public Deck() {
        cards = new Card[CAPACITY];
    }

    public void reset() {
        Card.Suit[] suits = Card.Suit.values();
        Card.Value[] values = Card.Value.values();
        sizeOfDeck = 0;
        for(int i = 0; i < CAPACITY / 4; i++) {
            for(int j = 0; j < 4; j++) {
                cards[sizeOfDeck++] = new Card(suits[j], values[i]);
            }
        }
    }

    public boolean isEmpty() {
        return sizeOfDeck == 0;
    }

    public void shuffle() {
        int n = sizeOfDeck;
        Random r = new Random();

        for(int i = 0; i < sizeOfDeck; i++) {
            int randint = i + r.nextInt(n - i);
            Card randCard = cards[randint];
            cards[randint] = cards[i];
            cards[i] = randCard;
        }
    }

    public Card drawCard() throws Exception {
        if(isEmpty()) {
            throw new Exception("Невозможно нарисовать карту в пустой колоде");
        }
        return cards[--sizeOfDeck];
    }

    public ImageIcon drawCardImage() throws Exception {
        if(isEmpty()) {
            throw new Exception("Невозможно нарисовать карту в пустой колоде");
        }
        return new ImageIcon(cards[--sizeOfDeck].toString() + ".png");
    }

    public Card[] drawCard(int n) throws Exception{
        if(n < 0 || n > sizeOfDeck) {
            throw new Exception("Невозможно достать из пустой или маленькой колоды");
        }
        Card[] receive = new Card[n];
        for(int i = 0; i < n; i++) {
            receive[i] = cards[--sizeOfDeck];
        }
    return receive;
    }
}
