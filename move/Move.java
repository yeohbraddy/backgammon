package move;

/**
 * This interface represents the Move (can be PipToPip, PipToHome, BarToPip).
 * 
 * Used to decouple relationship between classes.
 * i.e. (RollMoves with PipToPip, PipToHome, BarToPip).
 * Simplified to (RollMoves with Move), doesn't matter what Move, as long as it is a move.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * @author Braddy Yeoh, 17357376
 *
 */
public interface Move {
	RollMoves getRollMoves();
}
