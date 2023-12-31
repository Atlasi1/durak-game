package com.example.durak_game;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {
    private String name;
    private String id;
    private Hand hand;
    private boolean attacker;

    public Player(String name, String id) {
        this.name = name;
        this.id = id;
        hand = new Hand();
    }

    public Player(String name) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
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

    public String getId() {
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
