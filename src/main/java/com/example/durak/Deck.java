package com.example.durak;

import java.util.Random;
import com.example.durak.Card.Suit;
import com.example.durak.Card.Value;

public class Deck {
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
