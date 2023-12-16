package com.example.durak_game;

import javafx.scene.Node;

import java.util.Comparator;

public class CardComparator<T> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        Card c1 = (Card) ((Node) o1).getUserData();
        Card c2 = (Card) ((Node) o2).getUserData();
        if (c1.getSuit().ordinal() < c2.getSuit().ordinal()) {
            return -1;
        } else if (c1.getSuit().ordinal() > c2.getSuit().ordinal()) {
            return 1;
        } else {
            return c1.getValue().getRank() - c2.getValue().getRank();
        }
    }
}
