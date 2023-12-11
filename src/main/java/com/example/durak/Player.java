package com.example.durak;

import java.util.UUID;

public class Player {
    private String name;
    private Hand hand;
    private UUID id;

    public Player(String name) {
        this.name = name;
        id = UUID.randomUUID();
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

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.name + "#" + this.id;
    }
}
