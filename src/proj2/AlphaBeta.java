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
	private int playClock;
	private long startClock;
	private long breakClock;
	AlphaBeta(int w, int h, State curr, String role, int playclock) {
		currState = curr;
		envWidth = w;
		envHeight = h;
		myRole = role;
		playClock = playclock;
		breakClock = (playClock*1000) - 300;
		//best = new best(0, null);
	}
	
	public class TimeExpired extends Throwable {
		
	}
	
	Move iterativeDeapening() {
		startClock = System.currentTimeMillis();
		System.out.println("StartClock: " + startClock);
		System.out.println("BreakClock: " + breakClock);
		Move result = null;
		try{
			for (int d = 1; d <= 15; d++) {
				result = bestMove(d);
			}
		}catch (TimeExpired e){
			System.out.println("Time out!!!!!");
			return result;
		}
		return result;
	}
	
	Move bestMove(int depth_limit) throws TimeExpired { 
		stats = new Stats(); // Set the starting time
		ArrayList<Move> moves = currState.getAllLegalMoves(envWidth, envHeight);
		System.out.println("-----------size of legal moves: " + moves.size());
		// moves should not be empty
		Move bestMove = maxMove(currState, MIN, MAX, depth_limit);
		stats.print();
		if(bestMove == null) {
			System.out.println("------------ATH! Best move is null -----------------");
		}
		return bestMove;
	}

	private int minValue(State s, int alpha_min, int beta_max, int depth_limit) throws TimeExpired {
		int bestVal = MAX;
		if(s.isGoalState(envWidth, envHeight)) { //veit hann potto "who's turn it is ?"
			//System.out.println("goal state from min : " + s.toString());
			bestVal = utility(s);
			return bestVal; 
		}
		if(depth_limit == 0) {
			return heuristic(s);
		}
		if(System.currentTimeMillis() - startClock >= breakClock) {
			throw new TimeExpired();
		}
		for (Move m : s.getAllLegalMoves(envWidth, envHeight)) {
			bestVal = Math.min(bestVal, maxValue(s.successorState(m), alpha_min, beta_max, depth_limit -  1));
			if(bestVal <= alpha_min) {
				return bestVal;
			}
			beta_max = Math.min(beta_max, bestVal);
		}
		return bestVal;
	}
	
	private Move maxMove(State s, int alpha_min, int beta_max, int depth_limit) throws TimeExpired {
		//best best = new best(MIN, null);
		Move bestMove = null;
		int bestVal = MIN;
		if(System.currentTimeMillis() - startClock >= breakClock ) {
			throw new TimeExpired();
		}
		for (Move m : s.getAllLegalMoves(envWidth, envHeight)) {
			int valFromMin = minValue(s.successorState(m), alpha_min, beta_max, depth_limit - 1);
			if ( valFromMin > bestVal) {
				//best.bestVal = valFromMin;
			    bestMove = m;
			}
			//value = Math.max(value, minValue(s.successorState(m), alpha_min, beta_max, m).bestVal);
			if(bestVal >= beta_max) {
				return bestMove;
			}
			alpha_min = Math.max(alpha_min, bestVal);
		}
		return bestMove;
	}
	
    private int maxValue(State s, int alpha_min, int beta_max, int depth_limit) throws TimeExpired {
		int bestVal = MIN;
		
		if(s.isGoalState(envWidth, envHeight)) { //veit hann potto "who's turn it is ?"
			//System.out.println("goal state from max : " + s.toString());
			bestVal = utility(s);
			return bestVal; 
		}
		if(depth_limit == 0) {
			return heuristic(s);
		}
		if(System.currentTimeMillis() - startClock >= breakClock ) {
			throw new TimeExpired();
		}
		
		for (Move m : s.getAllLegalMoves(envWidth, envHeight)) {
			int valFromMin = minValue(s.successorState(m), alpha_min, beta_max, depth_limit -1);
			if ( valFromMin > bestVal) {
				bestVal = valFromMin;
			}
			//value = Math.max(value, minValue(s.successorState(m), alpha_min, beta_max, m).bestVal);
			if(bestVal >= beta_max) {
				return bestVal;
			}
			alpha_min = Math.max(alpha_min, bestVal);
		}
		return bestVal;
	}
	
	private int heuristic(State s) {
		System.out.println("in heuristic");
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
