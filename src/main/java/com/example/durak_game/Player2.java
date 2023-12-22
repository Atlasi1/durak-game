package com.example.durak_game;

import java.io.Serializable;
import java.util.UUID;

public class Player2 implements Serializable {
    private String name;
    private String id;
    private Hand2 hand;
    private boolean attacker;

    public Player2(String name, String id) {
        this.name = name;
        this.id = id;
        hand = new Hand2();
    }

    public Player2(String name) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
        hand = new Hand2();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hand2 getHand() {
        return hand;
    }

    public void setHand(Hand2 hand) {
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
        return this.name;
    }
}

