package move;

import java.util.LinkedList;

/**
 * This class represents a Pip to Pip move.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 *
 */
public class PipToPip extends SumMove {
	private int fromPip;
	private int toPip;
	private boolean isHit;
	
	public PipToPip(int fromPip, int toPip, RollMoves rollMoves, boolean isHit) {
		this(fromPip, toPip, rollMoves, isHit, null);
	}
	
	public PipToPip(int fromPip, int toPip, RollMoves rollMoves, boolean isHit, LinkedList<Move> intermediateMoves) {
		super(intermediateMoves, rollMoves);
		this.fromPip = fromPip;
		this.toPip = toPip;
		this.isHit = isHit;
	}

	// Copy Constructor.
	public PipToPip(PipToPip move) {
		this(move.getFromPip(), move.getToPip(), move.getRollMoves(), move.isHit(), move.getIntermediateMoves());
	}
	
	public int getFromPip() {
		return fromPip;
	}
	
	public int getToPip() {
		return toPip;
	}
	
	public boolean isHit() {
		return isHit;
	}
	
	// not used atm.
	public String toString() {
		String s = "fromPip: " + (fromPip+1) + ", toPip: " + (toPip+1);
		if (this.hasIntermediateMoves()) {
			s += "\n ~ with intermediate move:\n ~ " + this.getIntermediateMoves();
		}
		return s;
	}
	
	public int getFro() {
		return getFromPip();
	}
	
	public int getTo() {
		return getToPip();
	}
}
