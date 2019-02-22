package cs361.battleships.models;

public enum AtackStatus {

	/**
	 * The result if an attack results in a miss.
	 */
	MISS,

	/**
	 * The result if an attack results in a hit on an enemy ship.
	 */
	HIT,

	/**
	 * THe result if an attack sinks the enemy ship
	 */
	SUNK,

	/**
	 * The results if an attack results in the defeat of the opponent (a
	 * surrender).
	 */
	SURRENDER,

	/**
	 * The results if an attack results in the defeat of the opponent (a
	 * surrender).
	 */
	SCANNED,

	/**
	 * The results if an attack results in the defeat of the opponent (a
	 * surrender).
	 */
	DETECTED,

	/**
	 * The result if the coordinates given are invalid.
	 */
	INVALID,

}
