package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import com.mchange.v1.util.CollectionUtils;

import java.util.*;

public class Ship {

	@JsonProperty private String kind;
	@JsonProperty private List<Square> occupiedSquares;
	@JsonProperty private int size;
	@JsonProperty private int CQindex;
	@JsonProperty private boolean isArmored;
	@JsonProperty private boolean sunk;
	@JsonProperty private boolean isUnderWater;

	public Ship() {
		occupiedSquares = new ArrayList<>();
	}
	
	public Ship(String kind) {
		this();
		this.kind = kind;
		sunk = false;
		switch(kind) {
			case "MINESWEEPER":
				size = 2;
				CQindex = 0;
				isArmored = false;
				isUnderWater = false;
				break;
			case "DESTROYER":
				size = 3;
				CQindex = 1;
				isArmored = true;
				isUnderWater = false;
				break;
			case "BATTLESHIP":
				size = 4;
				CQindex = 2;
				isArmored = true;
				isUnderWater = false;
				break;
			case "SUBMARINE":
				size = 5;
				CQindex = 4;
				isArmored = true;
				isUnderWater = true;
				break;
		}
	}

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}

	public int getSize(){
		return size;
	}

	public void place(char col, int row, boolean isVertical) {
		for (int i=0; i<size; i++) {
			if (isVertical) {
				occupiedSquares.add(new Square(row + i, col));
			} else {
				occupiedSquares.add(new Square(row, (char) (col + i)));
			}
			if (i == CQindex) {
				occupiedSquares.get(i).setCaptainsQuarters(true);
			}
		}
	}

	public void placeSub(char col, int row, boolean isVertical) {
		for (int i = 0; i < size - 1; i++)
		{
			if (isVertical) {
				occupiedSquares.add(new Square(row + i, col));
			} else {
				occupiedSquares.add(new Square(row, (char) (col + i)));
			}
			if (i == CQindex) {
				occupiedSquares.get(i).setCaptainsQuarters(true);
			}
		}
		if(isVertical) {
			occupiedSquares.add(new Square(row + 2, (char) (col + 1)));
		}
		else {
			occupiedSquares.add(new Square(row - 1, (char)(col + 2)));
		}
	}

	public boolean overlaps(Ship other) {
		Set<Square> thisSquares = Set.copyOf(getOccupiedSquares());
		Set<Square> otherSquares = Set.copyOf(other.getOccupiedSquares());
		Sets.SetView<Square> intersection = Sets.intersection(thisSquares, otherSquares);
		return intersection.size() != 0;
	}

	public boolean isAtLocation(Square location) {
		return getOccupiedSquares().stream().anyMatch(s -> s.equals(location));
	}

	public String getKind() {
		return kind;
	}

	public List<Result> attack(int x, char y) {
		var resultList = new ArrayList<Result>();
		var attackedLocation = new Square(x, y);
		var square = getOccupiedSquares().stream().filter(s -> s.equals(attackedLocation)).findFirst();
		if (!square.isPresent()) {
			resultList.add(new Result(attackedLocation));
			return resultList;
		}
		var attackedSquare = square.get();
		var result = new Result(attackedLocation);
		if (getOccupiedSquares().get(CQindex).equals(attackedSquare))
		{
			if (!this.isArmored && !this.isSunk()) {
				for(var i = 0; i < size; i++) {
					if(i != CQindex) {
						resultList.add(attack(getOccupiedSquares().get(i).getRow(), getOccupiedSquares().get(i).getColumn()).get(0));
					}
				}
				attackedSquare.hit();
				result.setShip(this);
				result.setResult(AtackStatus.SUNK);
				resultList.add(result);
				return resultList;
			}
			else if (this.isArmored && !this.isSunk())
			{
				this.isArmored = false;
				attackedSquare.hit();
				result.setShip(this);
				result.setResult(AtackStatus.HIT);
				resultList.add(result);
				return resultList;
			}
			else if (this.isSunk())
			{
				result.setResult(AtackStatus.INVALID);
				resultList.add(result);
				return resultList;
			}
		}
		if (attackedSquare.isHit() && !getOccupiedSquares().get(CQindex).equals(attackedSquare)) {
			result.setResult(AtackStatus.INVALID);
			resultList.add(result);
			return resultList;
		}
		attackedSquare.hit();
		result.setShip(this);
		if (isSunk()) {
			result.setResult(AtackStatus.SUNK);
		} else {
			result.setResult(AtackStatus.HIT);
		}
		resultList.add(result);
		return resultList;
	}

	public void moveShip(String direction){
			if (direction.equals("North")) {
				occupiedSquares.stream().forEach((square) -> square.moveUp());
			} else if (direction.equals("South")) {
				occupiedSquares.stream().forEach((square) -> square.moveDown());
			} else if (direction.equals("East")) {
				occupiedSquares.stream().forEach((square) -> square.moveRight());
			} else if (direction.equals("West")) {
                occupiedSquares.stream().forEach((square) -> square.moveLeft());
            }
	}

	@JsonIgnore
	public boolean isSunk() {
		if(getOccupiedSquares().stream().allMatch(s -> s.isHit())){
			sunk = true;
		}
		return sunk;
	}

	@JsonIgnore
	public boolean isUnderWater(){return isUnderWater;}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Ship)) {
			return false;
		}
		var otherShip = (Ship) other;

		return this.kind.equals(otherShip.kind)
				&& this.size == otherShip.size
				&& this.occupiedSquares.equals(otherShip.occupiedSquares);
	}

	@Override
	public int hashCode() {
		return 33 * kind.hashCode() + 23 * size + 17 * occupiedSquares.hashCode();
	}

	@Override
	public String toString() {
		return kind + occupiedSquares.toString();
	}
}
