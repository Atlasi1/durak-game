package com.example.durak;

public class Card {
    enum Suit {
        Clubs, Diamonds, Hearts, Spades; // Трефы — clubs Бубны — diamonds Червы — hearts Пики — spades
        private static final Suit[] suits = Suit.values();
        public static Suit getSuit(int i) {
            return Suit.suits[i];
        }
    }

    enum Value{
        Six(6), Seven(7), Eight(8), Nine(9), Ten(10), Jack(11), Queen(12), King(13), Ace(14);
        final int value;
        private Value(int value) {
            this.value = value;
        }
        private static final Value[] values = Value.values();
        public int getRank() { return value; }
    }

    private final Suit suit;
    private final Value value;
    private final int val;

    public Card(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
        this.val = value.value;
    }

    public Suit getSuit() {
        return suit;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return suit + "_" + value;
    }
}

