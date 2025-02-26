create table hp_player(
    id int primary key ,
    player_id varchar(255) NOT NULL ,
    player_name varchar(255) NOT NULL ,
    password varchar(255) NOT NULL ,
    score int,
    rank int
);