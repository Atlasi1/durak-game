package com.example.durak;

public class Card {
    enum Suit {
        Clubs, Diamonds, Hearts, Spades; // Трефы — clubs Бубны — diamonds Червы — hearts Пики — spades
        private static final Suit[] suits = Suit.values();
        public static Suit getSuit(int i) {
            return Suit.suits[i];
        }
    }

    enum Value {
        Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Ace;
        private static final Value[] values = Value.values();
        public static Value getValue(int i) {
            return Value.values[i];
        }
    }

    private final Suit suit;
    private final Value value;

    public Card(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
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

