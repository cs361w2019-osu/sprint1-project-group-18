package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {

	@JsonProperty private List<Square> occupiedSquares;
	@JsonProperty private String type;

	public Ship() {
		occupiedSquares = new ArrayList<>();
		type = "";
	}
	
	public Ship(String kind) {
		type = kind;
		int n;

		if(type == "minesweeper"){
			n = 2;
		}
		else if(type == "destroyer"){
			n = 3;
		}
		else if(type == "battleship"){
			n = 4;
		}
		else {n = 0;}

		occupiedSquares = new ArrayList<Square>(n);

		occupiedSquares.forEach((i) -> i = new Square());
	}

	public String getType(){
		return type;
	}

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}
}
