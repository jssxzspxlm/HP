package com.hp.TexasPoker.beans;

import lombok.Getter;

public class Card {
    private final Rank rank;
    @Getter
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public int getValue() { return rank.value; }

}
