package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static cs361.battleships.models.AtackStatus.*;

public class Game {

    @JsonProperty private Board playersBoard = new Board();
    @JsonProperty private Board opponentsBoard = new Board();

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
        boolean successful = playersBoard.placeShip(ship, x, y, isVertical);
        if (!successful)
            return false;

        boolean opponentPlacedSuccessfully;
        do {
            // AI places random ships, so it might try and place overlapping ships
            // let it try until it gets it right
            opponentPlacedSuccessfully = opponentsBoard.placeShip(ship, randRow(), randCol(), randVertical());
        } while (!opponentPlacedSuccessfully);

        return true;
    }

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean attack(int x, char y, boolean sonar) {
        Result playerAttack;
        if (sonar) {
            playerAttack = opponentsBoard.scan(x, y);

            Result otherAttack;
            if (y > 'B') {
                otherAttack = opponentsBoard.scan(x, (char) (y - 2));
            }
            if (y > 'A') {
                if (x > 1) {
                    otherAttack = opponentsBoard.scan(x - 1, (char) (y - 1));
                }
                otherAttack = opponentsBoard.scan(x, (char) (y - 1));
                if (x < 10) {
                    otherAttack = opponentsBoard.scan(x + 1, (char) (y - 1));
                }
            }
            if (x > 2) {
                otherAttack = opponentsBoard.scan(x - 2, y);
            }
            if (x > 1) {
                otherAttack = opponentsBoard.scan(x - 1, y);
            }
            if (x < 10) {
                otherAttack = opponentsBoard.scan(x + 1, y);
            }
            if (x < 9) {
                otherAttack = opponentsBoard.scan(x + 2, y);
            }

            if (y < 'J') {
                if (x > 1) {
                    otherAttack = opponentsBoard.scan(x - 1, (char) (y + 1));
                }
                otherAttack = opponentsBoard.scan(x, (char) (y + 1));
                if (x < 10) {
                    otherAttack = opponentsBoard.scan(x + 1, (char) (y + 1));
                }
            }
            if (y < 'I') {
                otherAttack = opponentsBoard.scan(x, (char) (y + 2));
            }
        }
        else{
            playerAttack = opponentsBoard.attack(x, y);
        }
        if (playerAttack.getResult() == INVALID) {
            return false;
        }

        Result opponentAttackResult;
        do {
            // AI does random attacks, so it might attack the same spot twice
            // let it try until it gets it right
            opponentAttackResult = playersBoard.attack(randRow(), randCol());
        } while(opponentAttackResult.getResult() == INVALID);

        return true;
    }

    private char randCol() {
        int random = new Random().nextInt(10);
        return (char) ('A' + random);
    }

    private int randRow() {
        return  new Random().nextInt(10) + 1;
    }

    private boolean randVertical() {
        return new Random().nextBoolean();
    }
}
