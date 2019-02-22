package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class Square {

	@JsonProperty private int row;
	@JsonProperty private char column;
	@JsonProperty private boolean hit;
	@JsonProperty private boolean isCaptainsQuarters;

	public Square() {
		this.isCaptainsQuarters = false;
		this.hit = false;
	}

	public Square(int row, char column) {
		this.row = row;
		this.column = column;
		this.isCaptainsQuarters = false;
		this.hit = false;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	public boolean getCaptainsQuarters() { return isCaptainsQuarters; }

	public void setCaptainsQuarters(boolean choice) { isCaptainsQuarters = choice; }


	@Override
	public boolean equals(Object other) {
		if (other instanceof Square) {
			return ((Square) other).row == this.row && ((Square) other).column == this.column;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return 31 * row + column;
	}

	@JsonIgnore
	public boolean isOutOfBounds() {
		return row > 10 || row < 1 || column > 'J' || column < 'A';
	}

	public boolean isHit() {
		return hit;
	}

	public void hit() {
		hit = true;
	}

	@Override
	public String toString() {
		return "(" + row + ", " + column + ')';
	}
}
