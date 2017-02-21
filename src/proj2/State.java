package proj2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class State {
	HashSet<Position> whites;
	HashSet<Position> blacks;
	boolean isWhite;
	
	State(HashSet<Position> w, HashSet<Position> b, boolean player) {
		whites = w;
		blacks = b;
		isWhite = player;
	}
	
	Set<Move> getAllLegalMoves(int width, int height) {
		Set<Move> legalMoves = new HashSet<Move>();
		if(isWhite) {
			for (Position p : whites) {
				//try go forward
				Position forwPos = new Position(p.getX(), p.getY() + 1);
				if(legalPosition(forwPos, width, height) && !whites.contains(forwPos) && !blacks.contains(forwPos)) {
					legalMoves.add(new Move(p, forwPos, false));
				}
				//try kill left
				Position leftPos = new Position(p.getX() - 1, p.getY() + 1);
				if(legalPosition(leftPos, width, height) && blacks.contains(leftPos)) {
					legalMoves.add(new Move(p, leftPos, true));
				}
				//try kill right
				Position rightPos = new Position(p.getX() + 1, p.getY() + 1);
				if(legalPosition(rightPos, width, height) && blacks.contains(rightPos)) {
					legalMoves.add(new Move(p, rightPos, true));
				}
			}
		}
		else {
			for (Position p : blacks) {
				//try go forward
				Position forwPos = new Position(p.getX(), p.getY() - 1);
				if(legalPosition(forwPos, width, height) && !whites.contains(forwPos) && !blacks.contains(forwPos)) {
					legalMoves.add(new Move(p, forwPos, false));
				}
				//try kill left
				Position leftPos = new Position(p.getX() + 1, p.getY() - 1);
				if(legalPosition(leftPos, width, height) && whites.contains(leftPos)) {
					legalMoves.add(new Move(p, leftPos, true));
				}
				//try kill right
				Position rightPos = new Position(p.getX() - 1, p.getY() - 1);
				if(legalPosition(rightPos, width, height) && whites.contains(rightPos)) {
					legalMoves.add(new Move(p, rightPos, true));
				}
			}
		}
		return legalMoves;
	}
	
	boolean legalPosition(Position p, int width, int height) {
		if (p.getX() < 1 || p.getY() < 1 || p.getX() > width || p.getY() > height) {
            return false;
        }
        return true;
	}
	
	// TODO: replace goal test with ctoff test that decides when to apply EVAL
	
	boolean isGoalState(int width, int height) {
		if(isDraw(width, height)) {
			//System.out.println("Goal state: its a draw");
			return true;
		}
		if(isWhite) {
			for (Position p: whites) {
				if(p.getY() == height) { //if at least one pawn has reached the end height (whites start at 1)
					//System.out.println("Goal state: white");
					return true;
				}
			}
		}
		else {
			for (Position p: blacks) { 
				if(p.getY() == 1) { //if at least one pawn has reached tile height 1 (blacks start at height = height)
					//System.out.println("Goal state: black");
					return true;
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	State successorState(Move move) {
		HashSet<Position> succWhite = new HashSet<Position>();
		HashSet<Position> succBlack = new HashSet<Position>();
		succWhite=(HashSet<Position>) whites.clone();
		succBlack=(HashSet<Position>)blacks.clone();
		if(isWhite) {
			if (move.getKill()) { //if the move is a kill move we have to remove the opponent's pawn
				// find pawn and remove
				succBlack.remove(move.getDest());
			}
			succWhite.remove(move.getCurr()); //always update the curr player's pawns
			succWhite.add(move.getDest());
		}
		else {
			if (move.getKill()) { //if the move is a kill move we have to remove the opponent's pawn
				// find pawn and remove
				succWhite.remove(move.getDest());
			}
			succBlack.remove(move.getCurr()); //always update the curr player's pawns
			succBlack.add(move.getDest());
		}
		State nextState = new State(succWhite, succBlack, !isWhite);
		return nextState;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
    	if (other == null) {
    		return false;
    	}
        if (other.getClass() != this.getClass()) {
        	return false;
        }
        State that = (State)other;
        return (this.whites.equals(that.whites) && this.blacks.equals(that.blacks) && this.isWhite == that.isWhite);
	}
	
	@Override
	public String toString() {
		if (isWhite) {
			return "state is white \n whites: " + whites.toString() + "\n blacks: " + blacks.toString();
		}
		return "state is black \n blacks: " + blacks.toString() + "\n whites: " + whites.toString();
	}
	
	public boolean isDraw(int width, int height) {
		Set<Move> myMoves = getAllLegalMoves(width, height);
		boolean pawnsEmpty = false;
		if (isWhite) {
			pawnsEmpty =  whites.isEmpty();	
		}
		else {
			pawnsEmpty = blacks.isEmpty();
		}
		//System.out.println("pawns empty is " + pawnsEmpty);
		//System.out.println("all legal moves is: " + myMoves.toString());
		//System.out.println("is empty " + myMoves.isEmpty() + "mymoves size " + myMoves.size());
		if (pawnsEmpty || myMoves == null || myMoves.size() == 0) {
			return true;
		}
		
		return false;
	}
}
