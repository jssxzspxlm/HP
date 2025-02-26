package com.hp.TexasPoker.mapper;

import com.hp.TexasPoker.beans.Player;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlayerMapper {

    int createNewPlayer(Player player);
}
