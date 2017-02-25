package proj2;

public class Move {
	private Position currPos;
	private Position destPos;
	private boolean isKill;
	
	Move(Position p, Position newPos, boolean isKill) {
		this.currPos = p;
		this.destPos = newPos;
		this.isKill = isKill;
	}
	
	Position getCurr() {
		return currPos;
	}
	
	Position getDest() {
		return destPos;
	}
	
	boolean getKill() {
		return isKill;
	}
	
	int getCurrY() {
		return currPos.getY();
	}
	int getCurrX() {
		return currPos.getX();
	}
	
	int getNewY() {
		return destPos.getY();
	}
	
	int getNewX() {
		return destPos.getX();
	}
	
	@Override
	public String toString() {
		return "(move " + currPos.getX() + " " + currPos.getY() + " " + destPos.getX() + " " + destPos.getY() + ")";
	}
}
	
