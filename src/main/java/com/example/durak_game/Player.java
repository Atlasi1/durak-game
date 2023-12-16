package com.example.durak_game;

import java.util.UUID;

public class Player {
    private String name;
    private int id;
    private Hand hand;
    private boolean attacker;

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        hand = new Hand();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public int getId() {
        return id;
    }
    public void setAttacker(boolean b) {
        attacker = b;
    }

    public boolean isAttacker() {
        return attacker;
    }

    @Override
    public String toString() {
        return this.name + "#" + this.id;
    }
}
