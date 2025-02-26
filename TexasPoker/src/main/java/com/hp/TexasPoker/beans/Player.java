package com.hp.TexasPoker.beans;

import lombok.Data;

@Data
public class Player {

    private String playerId;

    private String playerName;

    private String password;

    private int score;

    private int rank;

}
