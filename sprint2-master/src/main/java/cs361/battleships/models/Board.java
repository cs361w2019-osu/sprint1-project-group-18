package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

	@JsonProperty private List<Ship> ships;
	@JsonProperty private List<Result> attacks;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		ships = new ArrayList<>();
		attacks = new ArrayList<>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		//changed from 3 to 5 for sub size?
		if (ships.size() >= 5) {
			return false;
		}
		if (ships.stream().anyMatch(s -> s.getKind().equals(ship.getKind()))) {
			return false;
		}
		final var placedShip = new Ship(ship.getKind());
		//if not submarine, don't allow ships to be placed on it.
		if(!ship.getKind().equals("SUBMARINE")) {
			placedShip.place(y, x, isVertical);
			if (ships.stream().anyMatch(s -> s.overlaps(placedShip))) {
				return false;
			}
		}//else if submarine, then it can be placed underneath ships.
		else {
			placedShip.placeSub(y,x,isVertical);
		}

		if (ships.stream().anyMatch(s -> s.overlaps(placedShip))) {
			ships.add(placedShip);
			return true;
		}
		if (placedShip.getOccupiedSquares().stream().anyMatch(s -> s.isOutOfBounds())) {
			return false;
		}
		ships.add(placedShip);
		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		List<Result> attackResult;
		var attackSquare = new Square(x, y);
		if (attacks.stream().anyMatch(r -> (r.getLocation().equals(attackSquare) && r.getLocation().getCaptainsQuarters()))){
			if((attacks.stream().filter(r -> r.getLocation().equals(attackSquare))).count() == 1) {
				attackSquare.setCaptainsQuarters(true);
				attackResult = attack(attackSquare);
			}
			else
				attackResult = attack(attackSquare);
		}
		else {
			attackResult = attack(attackSquare);
		}
		attackResult.forEach((elem) -> attacks.add(elem));
		return attacks.get(attacks.size() - 1);
	}

	private List<Result> attack(Square s) {
		var attackList = new ArrayList<Result>();
		var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList());
		if (shipsAtLocation.size() == 0) {
			var attackResult = new Result(s);
			attackList.add(attackResult);
			return attackList;
		}
		var validShips = shipsAtLocation.stream().filter(ship -> !ship.isUnderWater()).collect(Collectors.toList());
		Ship hitShip;
		if(validShips.size() > 0){
			hitShip = validShips.get(0);
		}
		else{
			var attackResult = new Result(s);
			attackList.add(attackResult);
			return attackList;
		}
		var attackResult = hitShip.attack(s.getRow(), s.getColumn());
		if(hitShip.getOccupiedSquares().get(hitShip.getSize() - 2).equals(attackResult.get(0).getLocation()) && attackResult.size() == 1){
			attackResult.get(0).getLocation().setCaptainsQuarters(true);
		}
		if (attackResult.get(attackResult.size() - 1).getResult() == AtackStatus.SUNK) {
			if (ships.stream().allMatch(ship -> ship.isSunk())) {
				attackResult.get(attackResult.size() - 1).setResult(AtackStatus.SURRENDER);
			}
		}
		return attackResult;
	}

	//For scanning
	public Result scan(int x, char y) {
		Result attackResult = scan(new Square(x, y));
		attacks.add(attackResult);
		return attackResult;
	}

	private Result scan(Square s) {
		var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList());
		if (shipsAtLocation.size() == 0) {
			var attackResult = new Result(s);
			attackResult.setResult(AtackStatus.SCANNED);
			return attackResult;
		}
		else{
			var attackResult = new Result(s);
			attackResult.setResult(AtackStatus.DETECTED);
			return attackResult;
		}
	}

	public void moveFleet(String direction){
		ships.stream().forEach((ship) -> ship.moveShip(direction));
}
  
    public Result attack_under(int x, char y) {
        List<Result> attackResult;
        var attackSquare = new Square(x, y);
        if (attacks.stream().anyMatch(r -> (r.getLocation().equals(attackSquare) && r.getLocation().getCaptainsQuarters()))){
            if((attacks.stream().filter(r -> r.getLocation().equals(attackSquare))).count() == 1) {
                attackSquare.setCaptainsQuarters(true);
                attackResult = attack_under(attackSquare);
            }
            else
                attackResult = attack_under(attackSquare);
        }
        else {
            attackResult = attack_under(attackSquare);
        }
        attackResult.forEach((elem) -> attacks.add(elem));
        return attacks.get(attacks.size() - 1);
    }

    private List<Result> attack_under(Square s) {
        var attackList = new ArrayList<Result>();
        var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList());
        if (shipsAtLocation.size() == 0) {
            var attackResult = new Result(s);
            attackList.add(attackResult);
            return attackList;
        }
        var validShips = shipsAtLocation.stream().filter(ship -> ship.isUnderWater()).collect(Collectors.toList());
        Ship hitShip;
        if(validShips.size() > 0){
            hitShip = validShips.get(0);
        }
        else{
            var attackResult = new Result(s);
            attackList.add(attackResult);
            return attackList;
        }
        var attackResult = hitShip.attack(s.getRow(), s.getColumn());
        if(hitShip.getOccupiedSquares().get(hitShip.getSize() - 2).equals(attackResult.get(0).getLocation()) && attackResult.size() == 1){
            attackResult.get(0).getLocation().setCaptainsQuarters(true);
        }
        if (attackResult.get(attackResult.size() - 1).getResult() == AtackStatus.SUNK) {
            if (ships.stream().allMatch(ship -> ship.isSunk())) {
                attackResult.get(attackResult.size() - 1).setResult(AtackStatus.SURRENDER);
            }
        }
        return attackResult;
    }

	List<Ship> getShips() {
		return ships;
	}
}
