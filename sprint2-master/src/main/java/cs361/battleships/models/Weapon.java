package cs361.battleships.models;


import java.util.List;

public class Weapon {
    static class Laser {
        public static Result Attack(Board opponentsBoard, int x, char y){
            Result SurfaceAttack = opponentsBoard.attack(x, y);
            Result UnderAttack = opponentsBoard.attack_under(x, y);
            if(SurfaceAttack.getResult() != AtackStatus.INVALID){
                return SurfaceAttack;
            }else{
                return UnderAttack;
            }
        }
    }
    static class Sonar {
        public static Result Attack(Board opponentsBoard, int x, char y){
            Result playerAttack;
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
            return playerAttack;
        }
    }
}