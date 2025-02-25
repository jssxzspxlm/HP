package com.hp.TexasPoker.beans;

import lombok.Getter;

@Getter
public enum HandType {
    // 皇家同花顺
    ROYAL_FLUSH(10),
    // 同花顺
    STRAIGHT_FLUSH(9),
    // 四条
    FOUR_OF_A_KIND(8),
    // 葫芦
    FULL_HOUSE(7),
    // 同花
    FLUSH(6),
    // 顺子
    STRAIGHT(5),
    // 三条
    THREE_OF_A_KIND(4),
    // 两对
    TWO_PAIRS(3),
    // 对子
    PAIR(2),
    // 高牌
    HIGH_CARD(1);

    final int value;
    HandType(int value) { this.value = value; }
}
