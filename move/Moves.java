package move;

import java.util.Iterator;
import java.util.LinkedList;

import interfaces.ColorParser;
import interfaces.InputValidator;
import javafx.scene.paint.Color;

/**
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 *
 */
@SuppressWarnings("serial")
public class Moves extends LinkedList<RollMoves> implements InputValidator, ColorParser {
	public Moves() {
		super();
	}
	
	// If there are no moves left, but there are still dice results.
	// In this case, we should recalculate possible moves with the remaining dice result.
	public boolean hasDiceResultsLeft() {
		// if possible moves not empty (expected there is moves left in RollMoves),
		// but there are no moves left in each RollMoves,
		// means there are dice results left.
		boolean noMovesLeft = true;
		for (RollMoves aRollMoves : this) {
			if (!aRollMoves.getMoves().isEmpty()) {
				noMovesLeft = false;
				break;
			}
		}
		return !this.isEmpty() && noMovesLeft;
	}
	
	public Move isValidPipToPip(String fro, String to) {
		Move theValidMove = null;
		
		if (isPip(fro) && isPip(to)) {
			int fromPip = Integer.parseInt(fro);
			int toPip = Integer.parseInt(to);
			
			for (RollMoves rollMoves : this) {
				// check if fromPip is part of possible moves.
				PipToPip move = null;
				for (Move aMove : rollMoves.getMoves()) {
					if (aMove instanceof PipToPip) {
						move = (PipToPip) aMove;
						if (move.getFromPip() == fromPip) {
							// check if toPip is part of fromPip's possible toPips.
							if (move.getToPip() == toPip) {
								theValidMove = (Move) move;
								break;
							}
						}
					}
				}
				if (theValidMove != null) {
					rollMoves.setUsed();
					break;
				}
			}
		}
		return theValidMove;
	}
	
	public Move isValidPipToHome(String fro, String to) {
		Move theValidMove = null;
		
		if (isPip(fro) && isBarOrHome(to)) {
			int fromPip = Integer.parseInt(fro);
			
			for (RollMoves rollMoves : this) {
				// check if fromPip is part of possible moves.
				PipToHome move = null;
				for (Move aMove : rollMoves.getMoves()) {
					if (aMove instanceof PipToHome) {
						move = (PipToHome) aMove;
						if (move.getFromPip() == fromPip) {
							theValidMove = (Move) move;
							break;
						}
					}
				}
				if (theValidMove != null) {
					rollMoves.setUsed();
					break;
				}
			}
		}
		return theValidMove;
	}
	
	public Move isValidBarToPip(String fro, String to) {
		Move theValidMove = null;
		
		if (isBarOrHome(fro) && isPip(to)) {
			Color fromBar = parseColor(fro);
			int toPip = Integer.parseInt(to);
			
			for (RollMoves rollMoves : this) {
				// check if fromBar is part of possible moves.
				BarToPip move = null;
				for (Move aMove : rollMoves.getMoves()) {
					if (aMove instanceof BarToPip) {
						move = (BarToPip) aMove;
						if (move.getFromBar() == fromBar) {
							// check if toPip is part of fromBar's possible toPips.
							if (move.getToPip() == toPip) {
								theValidMove = (Move) move;
								break;
							}
						}
					}
				}
				if (theValidMove != null) {
					rollMoves.setUsed();
					break;
				}
			}
		}
		return theValidMove;
	}
	
	// Removes all the moves (PipToPip, PipToHome) that has the fromPip, 'fromPip'.
	// used by removeMovesOfEmptyCheckersStorer().
	public void removeMovesOfFromPip(int fromPip) {
		// we use this way of iterating and use iter.remove() to remove,
		// if not while removing will raised ConcurrentModificationException.
		for (Iterator<RollMoves> iterRollMoves = this.iterator(); iterRollMoves.hasNext();) {
			RollMoves aRollMoves = iterRollMoves.next();
			for (Iterator<Move> iterMove = aRollMoves.getMoves().iterator(); iterMove.hasNext();) {
				Move aMove = iterMove.next();
				if (aMove instanceof PipToPip) {
					if (((PipToPip) aMove).getFromPip() == fromPip) iterMove.remove();
				} else if (aMove instanceof PipToHome) {
					if (((PipToHome) aMove).getFromPip() == fromPip) iterMove.remove();
				}
			}
			
			// removes the entire rollMoves if it has no moves left and is used.
			if (aRollMoves.getMoves().isEmpty() && aRollMoves.isUsed()) iterRollMoves.remove();
		}
	}
	// Removes all the moves (PipToPip, PipToHome) that has the fromPip, 'fromPip'.
	// used by removeMovesOfEmptyCheckersStorer().
	public void removeMovesOfFromBar(Color fromBar) {
		// we use this way of iterating and use iter.remove() to remove,
		// if not while removing will raised ConcurrentModificationException.
		for (Iterator<RollMoves> iterRollMoves = this.iterator(); iterRollMoves.hasNext();) {
			RollMoves aRollMoves = iterRollMoves.next();
			for (Iterator<Move> iterMove = aRollMoves.getMoves().iterator(); iterMove.hasNext();) {
				Move aMove = iterMove.next();
				if (aMove instanceof BarToPip) {
					if (((BarToPip) aMove).getFromBar() == fromBar) iterMove.remove();
				}
			}
			
			// removes the entire rollMoves if it has no moves left and is used.
			if (aRollMoves.getMoves().isEmpty() && aRollMoves.isUsed()) {
				iterRollMoves.remove();
			}
		}
	}
	
	/**
	 * Remove the rollMoves from moves.
	 * i.e. remove everything related to the dice roll result completely from moves.
	 * @param rollMoves rollMoves to remove.
	 */
	public void removeRollMoves(RollMoves theRollMoves) {
		this.remove(theRollMoves);		// remove used rollMoves.
		removeOtherRollMoves(theRollMoves);
	}
	
	/**
	 * Removes other rollMoves from moves based on argument 'rollMoves'.
	 * Rules state,
	 * - if sum result is moved, then two other result is forfeited.
	 * - if either one result moved, sum result is forfeited.
	 * @param rollMoves rollMoves that was removed.
	 */
	private void removeOtherRollMoves(RollMoves theRollMoves) {
		// if NormalRollMoves, we remove SumRollMoves relied on it.
		if (theRollMoves.isNormalRollMoves()) {
			for (Iterator<RollMoves> iter = this.iterator(); iter.hasNext();) {
				RollMoves aRollMoves = iter.next();
				if (aRollMoves.isSumRollMoves()) {
					if (aRollMoves.getDependedRollMoves().contains(theRollMoves)) {
						iter.remove();
					}
				}
			}
		// if SumRollMoves,
		// 1. we remove SumRollMove's NormalRollMoves.
		// 2. we remove other SumRollMoves that rely on the removed SumRollMoves's NormalRollMoves.
		} else if (theRollMoves.isSumRollMoves()) {
			// remove the relied NormalRollMoves.
			for (RollMoves dependedRollMoves : theRollMoves.getDependedRollMoves()) {
				this.remove(dependedRollMoves);

				// remove the other SumRollMoves that rely on the removed SumRollMove's NormalRollMoves.
				for (Iterator<RollMoves> iter = this.iterator(); iter.hasNext();) {
					RollMoves aRollMoves = iter.next();
					if (aRollMoves.isSumRollMoves()) {
						if (aRollMoves.getDependedRollMoves().contains(dependedRollMoves)) {
							iter.remove();
						}
					}
				}
			}
		}
	}
	
	/**
	 * Checks if fromPip is part of possible moves.
	 * Only used for mouse clicks of fro.
	 * Atm, considers pips, no bars.
	 * @param fromPip
	 * @return boolean value indicating if fromPip is part of possible moves.
	 */
	public boolean isValidFro(int fromPip) {
		boolean isValidFro = false;
		for (RollMoves rollMoves : this) {
			for (Move aMove : rollMoves.getMoves()) {
				if (aMove instanceof PipToPip) {
					if (((PipToPip) aMove).getFromPip() == fromPip) {
						isValidFro = true;
						break;
					}
				} else if (aMove instanceof PipToHome) {
					if (((PipToHome) aMove).getFromPip() == fromPip) {
						isValidFro = true;
						break;
					}
				}
			}
		}
		return isValidFro;
	}
	public boolean isValidFro(String fromBar) {
		boolean isValidFro = false;
		for (RollMoves rollMoves : this) {
			for (Move aMove : rollMoves.getMoves()) {
				if (aMove instanceof BarToPip) {
					if (((BarToPip) aMove).getFromBar() == parseColor(fromBar)) {
						isValidFro = true;
						break;
					}
				}
			}
		}
		return isValidFro;
	}
}