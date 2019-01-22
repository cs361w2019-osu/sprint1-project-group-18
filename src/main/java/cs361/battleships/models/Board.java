package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Board {
	private List<Ship> placed_ships;
	private List<Result> results;
	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		this.placed_ships = new ArrayList<Ship>();
		this.results = new ArrayList<Result>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
    for(int i = 0; i < ship.getOccupiedSquares().size(); i++){
      if(isVertical){
        ship.getOccupiedSquares()[i].setRow(x + i);
        ship.getOccupiedSquares()[i].setColumn(y);
      }
      else{
        ship.getOccupiedSquares()[i].setRow(x);
        ship.getOccupiedSquares()[i].setColumn(y + i);
      }
    }
    
		for(int i = 0; i < ship.getOccupiedSquares().size(); i++){
			if(ship.getOccupiedSquares()[i].getRow() < 1 || ship.getOccupiedSquares()[i].getRow() > 10){
				return false;        //Returns false if Row out of bounds
			}
			if(ship.getOccupiedSquares()[i].getColumn() < 'a' || ship.getOccupiedSquares()[i].getColumn() > 'j'){
				return false;        //Returns false if Column out of bounds
			}
			if(is_ship_at(ship.getOccupiedSquares()[i].getRow(), ship.getOccupiedSquares()[i].getColumn())){
				return false         //Returns false if conflicting with another ship
			}
		}
   
		placed_ships.append(ship);
		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		if(is_attack_at(x, y) || (x < 0 || x > 10) || (y < 'a' || y > 'j')){
			return INVALID;
		}
		else if(is_ship_at(x, y)){
			Ship ship = ship_at(x, y);

			if(sunk(ship)){
				up = false;
				for(int i = 0; i < placed_ships.size(); i++){
					if(!sunk(placed_ships[i]){
						up = true;
					}
				}
				if(!up){
					return SURRENDER;
				}
				else{
					return SUNK;
				}
			}
			else{
				return Hit;
			}
		}
		else{
			return MISS;
		}
	}

	public List<Ship> getShips() {
		return ships;
	}

	public void setShips(List<Ship> ships) {
		placed_ships = ships;
	}

	public List<Result> getAttacks() {
		return results;
	}

	public void setAttacks(List<Result> attacks) {
		results = attacks;
	}

	public boolean is_ship_at(int x, char y) {
		for(int i = 0; i < placed_ships.size(); i++){
			squares = ships[i].getOccupiedSquares();
			for(int j = 0; j < squares.size(); j++){
				if(squares[j].getRow() == x && squares[j].getColumn() == y){
					return true;
				}
			}
		}
		return false;
	}
	public Ship ship_at(int x, char y) {
		for(int i = 0; i < placed_ships.size(); i++){
			squares = ships[i].getOccupiedSquares();
			for(int j = 0; j < squares.size(); j++){
				if(squares[j].getRow() == x && squares[j].getColumn() == y){
					return ships[i];
				}
			}
		}
		return null;
	}
	public boolean is_attack_at(int x, char y) {
		for(int i = 0; i < results.size(); i++){
			if(results[i].getLocation().getRow() == x && results[i].getLocation().getColumn() == y){
				return true;
			}
		}
		return false;
	}
	public Result attack_at(int x, char y) {
		for(int i = 0; i < results.size(); i++){
			if(results[i].getLocation().getRow() == x && results[i].getLocation().getColumn() == y){
				return results[i];
			}
		}
		return null;
	}
	public boolean sunk(Ship ship){
		for(int i = 0; i < ship.getOccupiedSquares(); i++){
			if(!is_attack_at(ship.getOccupiedSquares()[i].getRow(), ship.getOccupiedSquares()[i].getColumn())){
				return false;
			}
		}
		return true;
	}
}
