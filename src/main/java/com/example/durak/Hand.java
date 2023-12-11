package com.example.durak;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Collection;

public class Hand {
    private ArrayList<Card> cardsInHand;
    private SimpleIntegerProperty size;

    public Hand(ArrayList<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
        this.size = new SimpleIntegerProperty(cardsInHand.size());
    }

    public Card getCard(Card cd) throws Exception{
        if (cardsInHand.contains(cd)) {
            int index = cardsInHand.indexOf(cd);
            Card returning = cardsInHand.get(index);
            cardsInHand.remove(index);
            size.set(size.get() - 1);
            return returning;
        }
        else {
            throw new Exception("Нет такой карты в руке");
        }
    }

    public void addCard(Card cd) throws Exception {
        if(cd != null) {
            cardsInHand.add(cd);
            size.set(size.get() + 1);
        }
        else {
            throw new Exception("Неверное значение карты");
        }
    }

    public void addCards(Collection<Card> cds) throws Exception {
        if(cds != null) {
            cardsInHand.addAll(cds);
            size.set(size.get() + cds.size());
        }
        else {
            throw new Exception("Неверные значения карт");
        }
    }

    public void reset() {
        cardsInHand.clear();
        size.set(0);
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public SimpleIntegerProperty sizeProperty() {
        return size;
    }
}
