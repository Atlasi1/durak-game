package com.example.durak_game;

import java.io.Serializable;
import java.util.Random;
import com.example.durak_game.Card.Suit;
import com.example.durak_game.Card.Value;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;

public class Deck implements Serializable {
    private Card[] cards = new Card[36];
    private int sizeOfDeck;

    public Deck() {
        reset();
    }

    public void reset() {
        sizeOfDeck = 0;
        for(Suit s: Suit.values()) {
            for(Value v: Value.values()) {
                cards[sizeOfDeck++] = new Card(s, v);
            }
        }
        System.out.println(sizeOfDeck);
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

//    public Card[] drawCard(int n) throws Exception{
//        if(n < 0 || n > sizeOfDeck.get()) {
//            throw new Exception("Невозможно достать из пустой или маленькой колоды");
//        }
//        Card[] receive = new Card[n];
//        for(int i = 0; i < n; i++) {
//            sizeOfDeck.set(sizeOfDeck.get() - 1);
//            receive[i] = cards[sizeOfDeck.get()];
//        }
//    return receive;
//    }

    public int getSizeOfDeck() {
        return sizeOfDeck;
    }
}
