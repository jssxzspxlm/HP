package com.hp.TexasPoker.beans;

import java.util.List;

public class HandStrength implements Comparable<HandStrength> {
    private final int handRank;
    private final List<Integer> kickers;

    public HandStrength(int handRank, List<Integer> kickers) {
        this.handRank = handRank;
        this.kickers = kickers;
    }

    @Override
    public int compareTo(HandStrength other) {
        if (this.handRank != other.handRank) {
            return Integer.compare(this.handRank, other.handRank);
        }

        for (int i = 0; i < this.kickers.size(); i++) {
            int compare = Integer.compare(this.kickers.get(i), other.kickers.get(i));
            if (compare != 0) return compare;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "HandStrength{" +
                "handRank=" + handRank +
                ", kickers=" + kickers +
                '}';
    }
}
