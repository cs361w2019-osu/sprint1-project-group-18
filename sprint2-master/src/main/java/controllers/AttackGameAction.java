package controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.Game;

public class AttackGameAction {

    @JsonProperty private Game game;
    @JsonProperty private int x;
    @JsonProperty private char y;
    @JsonProperty private boolean sonarCheck;
    @JsonProperty private int sunk;

    public Game getGame() {
        return game;
    }

    public int getActionRow() {
        return x;
    }

    public char getActionColumn() {
        return y;
    }

    public boolean getSonar() { return sonarCheck; }

    public int getSunk() {return sunk;}
}
