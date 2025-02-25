package com.hp.TexasPoker.utils;

import com.hp.TexasPoker.beans.*;

import java.util.*;

public class HandEvaluatorUtils {
    public static void main(String[] args) {
        List<Card> hand1 = Arrays.asList(
                new Card(Rank.TEN, Suit.CLUBS),
                new Card(Rank.TEN, Suit.CLUBS),
                new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.TEN, Suit.CLUBS));
        List<Card> hand2 = Arrays.asList(
                new Card(Rank.TEN, Suit.CLUBS),
                new Card(Rank.TEN, Suit.CLUBS),
                new Card(Rank.NINE, Suit.CLUBS),
                new Card(Rank.NINE, Suit.CLUBS),
                new Card(Rank.TEN, Suit.CLUBS));
        System.out.println(compareHands(hand1, hand2));
        List<Card> handAll = Arrays.asList(
                new Card(Rank.TEN, Suit.DIAMONDS),
                new Card(Rank.FIVE, Suit.DIAMONDS),
                new Card(Rank.SIX, Suit.DIAMONDS),
                new Card(Rank.EIGHT, Suit.SPADES),
                new Card(Rank.SEVEN, Suit.CLUBS),
                new Card(Rank.NINE, Suit.DIAMONDS),
                new Card(Rank.NINE, Suit.SPADES));
        System.out.println(getBestHand(handAll));
    }

    // 找到最佳牌型
    public static HandStrength getBestHand(List<Card> sevenCards) {
        return generateCombinations(sevenCards).stream().map(HandEvaluatorUtils::evaluateHand).max(Comparator.naturalOrder()).orElse(null);
    }

    // 生成所有5张组合
    public static List<List<Card>> generateCombinations(List<Card> cards) {
        List<List<Card>> combinations = new ArrayList<>();
        combine(cards, combinations, new ArrayList<>(), 0, 5);
        return combinations;
    }

    private static void combine(List<Card> cards, List<List<Card>> result, List<Card> temp, int start, int k) {
        if (k == 0) {
            result.add(new ArrayList<>(temp));
            return;
        }
        for (int i = start; i <= cards.size() - k; i++) {
            temp.add(cards.get(i));
            combine(cards, result, temp, i + 1, k - 1);
            temp.removeLast();
        }
    }

    // 比较两手牌
    public static int compareHands(List<Card> hand1, List<Card> hand2) {
        HandStrength hs1 = evaluateHand(hand1);
        HandStrength hs2 = evaluateHand(hand2);
        return hs1.compareTo(hs2);
    }

    // 评估五张牌的牌型
    public static HandStrength evaluateHand(List<Card> cards) {
        cards.sort((a, b) -> b.getValue() - a.getValue());
        boolean isFlush = isFlush(cards);
        boolean isStraight = isStraight(cards);
        int straightHigh = getStraightHigh(cards);

        // 同花顺（包括皇家同花顺）
        if (isFlush && isStraight) {
            int royal = (straightHigh == Rank.ACE.getValue()) ? HandType.ROYAL_FLUSH.getValue() : HandType.STRAIGHT_FLUSH.getValue();
            return new HandStrength(royal, Collections.singletonList(straightHigh));
        }

        // 四条
        Map<Integer, Integer> rankCount = getRankCount(cards);
        if (rankCount.containsValue(4)) {
            int quadRank = getKeyByValue(rankCount, 4);
            List<Integer> kickers = new ArrayList<>();
            kickers.add(quadRank);
            kickers.add(getKeyByValue(rankCount, 1));
            return new HandStrength(HandType.FOUR_OF_A_KIND.getValue(), kickers);
        }

        // 葫芦
        if (rankCount.containsValue(3) && rankCount.containsValue(2)) {
            int threeRank = getKeyByValue(rankCount, 3);
            int pairRank = getKeyByValue(rankCount, 2);
            return new HandStrength(HandType.FULL_HOUSE.getValue(), Arrays.asList(threeRank, pairRank));
        }

        // 同花
        if (isFlush) {
            List<Integer> ranks = new ArrayList<>();
            for (Card card : cards) {
                ranks.add(card.getValue());
            }
            return new HandStrength(HandType.FLUSH.getValue(), ranks);
        }

        // 顺子
        if (isStraight) {
            return new HandStrength(HandType.STRAIGHT.getValue(), Collections.singletonList(straightHigh));
        }

        // 三条
        if (rankCount.containsValue(3)) {
            int threeRank = getKeyByValue(rankCount, 3);
            List<Integer> kickers = new ArrayList<>();
            kickers.add(threeRank);
            cards.stream().map(Card::getValue).filter(value -> value != threeRank).sorted(Comparator.reverseOrder()).forEach(kickers::add);
            return new HandStrength(HandType.THREE_OF_A_KIND.getValue(), kickers.subList(0, 3));
        }

        // 两对
        if (rankCount.values().stream().filter(v -> v == 2).count() == 2) {
            List<Integer> pairs = new ArrayList<>();
            List<Integer> singles = new ArrayList<>();
            for (Card card : cards) {
                if (rankCount.get(card.getValue()) == 2 && !pairs.contains(card.getValue())) {
                    pairs.add(card.getValue());
                } else if (rankCount.get(card.getValue()) == 1) {
                    singles.add(card.getValue());
                }
            }
            pairs.sort(Collections.reverseOrder());
            singles.sort(Collections.reverseOrder());
            List<Integer> kickers = new ArrayList<>();
            kickers.addAll(pairs);
            kickers.addAll(singles);
            return new HandStrength(HandType.TWO_PAIRS.getValue(), kickers.subList(0, 3));
        }

        // 一对
        if (rankCount.containsValue(2)) {
            int pairRank = getKeyByValue(rankCount, 2);
            List<Integer> kickers = new ArrayList<>();
            kickers.add(pairRank);
            cards.stream().map(Card::getValue).filter(value -> value != pairRank).sorted(Comparator.reverseOrder()).forEach(kickers::add);
            return new HandStrength(HandType.PAIR.getValue(), kickers.subList(0, 4));
        }

        // 高牌
        List<Integer> ranks = new ArrayList<>();
        for (Card card : cards) ranks.add(card.getValue());
        ranks.sort(Collections.reverseOrder());
        return new HandStrength(HandType.HIGH_CARD.getValue(), ranks.subList(0, 5));
    }

    // 是否是同花
    private static boolean isFlush(List<Card> cards) {
        return cards.stream().map(Card::getSuit).distinct().count() == 1;
    }

    // 是否是顺子
    private static boolean isStraight(List<Card> cards) {
        Set<Integer> ranks = new HashSet<>();
        for (Card card : cards) {
            ranks.add(card.getValue());
        }

        // 普通顺子
        if (ranks.size() == 5) {
            int max = Collections.max(ranks);
            int min = Collections.min(ranks);
            if (max - min == 4) {
                return true;
            }
        }

        // 特殊顺子A-2-3-4-5
        return ranks.containsAll(Arrays.asList(Rank.ACE.getValue(), Rank.TWO.getValue(), Rank.THREE.getValue(), Rank.FOUR.getValue(), Rank.FIVE.getValue()));
    }

    private static int getStraightHigh(List<Card> cards) {
        Set<Integer> ranks = new HashSet<>();
        for (Card card : cards) ranks.add(card.getValue());

        if (ranks.containsAll(Arrays.asList(Rank.ACE.getValue(), Rank.TWO.getValue(), Rank.THREE.getValue(), Rank.FOUR.getValue(), Rank.FIVE.getValue()))) {
            return Rank.FIVE.getValue();
        }
        return Collections.max(ranks);
    }

    private static Map<Integer, Integer> getRankCount(List<Card> cards) {
        Map<Integer, Integer> count = new HashMap<>();
        for (Card card : cards) {
            count.put(card.getValue(), count.getOrDefault(card.getValue(), 0) + 1);
        }
        return count;
    }

    private static int getKeyByValue(Map<Integer, Integer> map, int value) {
        return map.entrySet().stream().filter(entry -> entry.getValue() == value).map(Map.Entry::getKey).findFirst().orElse(-1);
    }
}
