package com.example.durak_game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Hand2 implements Serializable {
    private ArrayList<Card> cardsInHand;
    private int size;

    public Hand2(ArrayList<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
        this.size = cardsInHand.size();
    }

    public Hand2() {
        this.cardsInHand = new ArrayList<>();
        this.size = 0;
    }

    public Card getCard(Card cd) throws Exception {
        for (Card card : getCardsInHand()) {
            if (Objects.equals(card.toString(), cd.toString())) {
                this.cardsInHand.remove(card);
                size--;
                return cd;
            }
        }
        throw new Exception("Нет такой карты в руке");
    }

    public void addCard(Card cd) throws Exception {
        if (cd != null) {
            cardsInHand.add(cd);
            size++;
        } else {
            throw new Exception("Неверное значение карты");
        }
    }

    public void addCards(Collection<Card> cds) throws Exception {
        if (cds != null) {
            cardsInHand.addAll(cds);
            size += cds.size();
        } else {
            throw new Exception("Неверные значения карт");
        }
    }

    public void reset() {
        cardsInHand.clear();
        size = 0;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    @Override
    public String toString() {
        String data = "#";
        for(Card cd: getCardsInHand()) {
            data += cd.toString() + "#";
        }
        return data;
    }
}
