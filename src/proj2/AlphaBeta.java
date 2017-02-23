package proj2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class AlphaBeta {
	static final int MAX = 100;
	static final int MIN = 0;
	private Stats stats = null;
	static final long cutOff = 1000;
	private int envWidth;
	private int envHeight;
	private State currState;
	private String myRole;
	private best best;
	
	public class best {
		int bestVal;
		Move bestMove;
		
		best(int value, Move move) {
			bestVal = value;
			bestMove = move;
		}
	}
	
	AlphaBeta(int w, int h, State curr, String role) {
		currState = curr;
		envWidth = w;
		envHeight = h;
		myRole = role;
		best = new best(0, null);
	}
	
	Move bestMove() { 
		stats = new Stats(); // Set the starting time
		ArrayList<Move> moves = currState.getAllLegalMoves(envWidth, envHeight);
		System.out.println("-----------size of legal moves: " + moves.size());
		// moves should not be empty
		Move bestMove = maxValue(currState, MIN, MAX, null).bestMove;
		stats.print();
		if(bestMove == null) {
			System.out.println("------------ATH! Best move is null -----------------");
		}
		return bestMove;
	}

	private best minValue(State s, int alpha_min, int beta_max, Move bestMove) {
		int value = MAX;
		if(s.isGoalState(envWidth, envHeight)) { //veit hann potto "who's turn it is ?"
			//System.out.println("goal state from min : " + s.toString());
			best.bestVal = utility(s);
			best.bestMove = bestMove;
			return best; 
		}
		for (Move m : s.getAllLegalMoves(envWidth, envHeight)) {
			value = Math.min(value, maxValue(s.successorState(m), alpha_min, beta_max, m).bestVal);
			if(value <= alpha_min) {
				best.bestVal = value;
				best.bestMove = m;
				return best;
			}
			beta_max = Math.min(beta_max, value);
		}
		best.bestVal = value;
		best.bestMove = bestMove;
		return best;
	}
	
	private best maxValue(State s, int alpha_min, int beta_max, Move bestMove) {
		int value = MIN;
		if(s.isGoalState(envWidth, envHeight)) { //veit hann potto "who's turn it is ?"
			//System.out.println("goal state from max : " + s.toString());
			best.bestVal = utility(s);
			best.bestMove = bestMove;
			return best; 
		}
		for (Move m : s.getAllLegalMoves(envWidth, envHeight)) {
			value = Math.max(value, minValue(s.successorState(m), alpha_min, beta_max, m).bestVal);
			if(value >= beta_max) {
				best.bestVal = value;
				best.bestMove = m;
				return best;
			}
			alpha_min = Math.max(alpha_min, value);
		}
		best.bestVal = value;
		best.bestMove = bestMove;
		return best;
	}
	
	private int heuristic(State s) {
		int whiteDist = envHeight;
		int blackDist = 1;
		for (Position p : s.whites) {
			if(p.getY() < whiteDist) {
				whiteDist = envHeight - p.getY();
			}
		}
		for (Position p : s.blacks) {
			if(p.getY() > blackDist) {
				blackDist = p.getY();
			}
		}
		if (s.isWhite) {
			return 50 - whiteDist + blackDist;
		}
		else {
			return 50 + whiteDist - blackDist;
		}
	}
	
	private int utility(State s) {
		if (s.isWhite && myRole.equals("white") || !s.isWhite && myRole.equals("black")) { //if we are the winners
			 return 100; 
		 }
		 else if(s.isWhite && myRole.equals("black") || !s.isWhite && myRole.equals("white")) { //if we are the loosers
			 return 0;
		 }
		 else {
			 return 50; //draw
		 }
	}
}
