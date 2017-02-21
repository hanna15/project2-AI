package proj2;

import java.util.ArrayList;

public class AlphaBeta {
	static final int MAX = 100;
	static final int MIN = 0;
	
	//svo depth og playclock, startclock etc
	//private int value;
	private int envWidth;
	private int envHeight;
	private State currState;
	private String myRole;
	
	AlphaBeta(int w, int h, State curr, String role) {
		currState = curr;
		envWidth = w;
		envHeight = h;
		myRole = role;
		//value = 0;
		//value = maxValue(currState, MIN, MAX);
		//return the action in ACTIONS(state) with value v
		//bestMove();
	}
	
	Move bestMove() { 
		ArrayList<Move> moves = currState.getAllLegalMoves(envWidth, envHeight);
		System.out.println("-----------size of legal moves: " + moves.size());
		int max_val = MIN;
		Move max_move = null;
		for (Move m : currState.getAllLegalMoves(envWidth, envHeight)) {
			int val = minValue(currState.successorState(m), MIN, MAX); //synist ekki skipta mali hvort byrjum a min eda max
			//System.out.println("-----------val is : " + val);
			if (val >= max_val) {
				//System.out.println("------------in if----------------");
				max_val = val;
				max_move = m;
			}
		}
		return max_move;
		//System.out.println("---max move is--- : " + max_move);
	}

	private int minValue(State s, int alpha_min, int beta_max) {
		int value = MAX;
		if(s.isGoalState(envHeight)) { //veit hann potto "who's turn it is ?"
			 if (s.isWhite && myRole.equals("white") || !s.isWhite && myRole.equals("black")) { //if we are the winners
				 return 100; 
			 }
			 else if(s.isWhite && myRole.equals("black") || !s.isWhite && myRole.equals("white")) { //if we are the loosers
				 return -100;
			 }
			 return 0; //??? when would we really return 0 ?
		} 
		//value = MAX;
		for (Move m : s.getAllLegalMoves(envWidth, envHeight)) {
			value = Math.min(value, maxValue(s.successorState(m), alpha_min, beta_max));
			if(value <= alpha_min) {
				return value;
			}
			beta_max = Math.min(beta_max, value);
		}
		return value;
	}
	
	private int maxValue(State s, int alpha_min, int beta_max) {
		int value = MIN;
		if(s.isGoalState(envHeight)) { //veit hann potto "who's turn it is ?"
			 if (s.isWhite && myRole.equals("white") || !s.isWhite && myRole.equals("black")) { //if we are the winners
				 return 100; 
			 }
			 else if(s.isWhite && myRole.equals("black") || !s.isWhite && myRole.equals("white")) { //if we are the loosers
				 return -100;
			 }
			 return 0; //??? when would we really return 0 ?
		} 
		//value = MIN;
		for (Move m : s.getAllLegalMoves(envWidth, envHeight)) {
			value = Math.max(value, minValue(s.successorState(m), alpha_min, beta_max));
			if(value >= beta_max) {
				return value;
			}
			alpha_min = Math.max(alpha_min, value);
		}
		return value;
	}
}
