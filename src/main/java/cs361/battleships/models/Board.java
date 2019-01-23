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
        ship.getOccupiedSquares().get(i).setRow(x + i);
        ship.getOccupiedSquares().get(i).setColumn(y);
      }
      else{
        ship.getOccupiedSquares().get(i).setRow(x);
        char temp = (char) (y+i);
        ship.getOccupiedSquares().get(i).setColumn(temp);
      }
    }
    
		for(int i = 0; i < ship.getOccupiedSquares().size(); i++){
			if(ship.getOccupiedSquares().get(i).getRow() < 1 || ship.getOccupiedSquares().get(i).getRow() > 10){
				return false;        //Returns false if Row out of bounds
			}
			if(ship.getOccupiedSquares().get(i).getColumn() < 'a' || ship.getOccupiedSquares().get(i).getColumn() > 'j'){
				return false;        //Returns false if Column out of bounds
			}
			if(is_ship_at(ship.getOccupiedSquares().get(i).getRow(), ship.getOccupiedSquares().get(i).getColumn())){
				return false;         //Returns false if conflicting with another ship
			}
		}
   
		placed_ships.add(ship);
		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		if (is_attack_at(x, y) || (x < 0 || x > 10) || (y < 'a' || y > 'j')) {
			Result r = new Result(AtackStatus.INVALID);
			Square loc = new Square(x, y);
			r.setLocation(loc);
			return r;
		} else if (is_ship_at(x, y)) {
			Ship ship = ship_at(x, y);

			if (sunk(ship)) {
				boolean up = false;
				for (int i = 0; i < placed_ships.size(); i++) {
					if (!sunk(placed_ships.get(i))) {
						up = true;
					}
				}
				if (!up) {
					Result r = new Result(AtackStatus.SURRENDER);
					r.setShip(ship);
					Square loc = new Square(x, y);
					r.setLocation(loc);
					return r;
				} else {
					Result r = new Result(AtackStatus.SUNK);
					r.setShip(ship);
					Square loc = new Square(x, y);
					r.setLocation(loc);
					return r;
				}
			} else {
				Result r = new Result(AtackStatus.HIT);
				r.setShip(ship);
				Square loc = new Square(x, y);
				r.setLocation(loc);
				return r;
			}
		} else {
			Result r = new Result(AtackStatus.MISS);
			Square loc = new Square(x, y);
			r.setLocation(loc);
			return r;
		}
	}

	public List<Ship> getShips() {
		return placed_ships;
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
			List<Square> squares = placed_ships.get(i).getOccupiedSquares();
			for(int j = 0; j < squares.size(); j++){
				if(squares.get(j).getRow() == x && squares.get(j).getColumn() == y){
					return true;
				}
			}
		}
		return false;
	}
	public Ship ship_at(int x, char y) {
		for(int i = 0; i < placed_ships.size(); i++){
			List<Square> squares = placed_ships.get(i).getOccupiedSquares();
			for(int j = 0; j < squares.size(); j++){
				if(squares.get(j).getRow() == x && squares.get(j).getColumn() == y){
					return placed_ships.get(i);
				}
			}
		}
		return null;
	}
	public boolean is_attack_at(int x, char y) {
		for(int i = 0; i < results.size(); i++){
			if(results.get(i).getLocation().getRow() == x && results.get(i).getLocation().getColumn() == y){
				return true;
			}
		}
		return false;
	}
	public Result attack_at(int x, char y) {
		for(int i = 0; i < results.size(); i++){
			if(results.get(i).getLocation().getRow() == x && results.get(i).getLocation().getColumn() == y){
				return results.get(i);
			}
		}
		return null;
	}
	public boolean sunk(Ship ship){
		for(int i = 0; i < ship.getOccupiedSquares().size(); i++){
			if(!is_attack_at(ship.getOccupiedSquares().get(i).getRow(), ship.getOccupiedSquares().get(i).getColumn())){
				return false;
			}
		}
		return true;
	}
}
