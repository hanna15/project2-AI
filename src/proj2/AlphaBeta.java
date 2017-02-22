package proj2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class AlphaBeta {
	static final int MAX = 100;
	static final int MIN = 0;
	private Stats stats = null;
	
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
		stats = new Stats(); // Set the starting time

		Set<Move> moves = currState.getAllLegalMoves(envWidth, envHeight);
		System.out.println("-----------size of legal moves: " + moves.size());
		// moves should not be empty
		int bestVal = maxValue(currState, MIN, MAX);
		int counter = 0;
		if (moves.isEmpty()) {
			System.out.println("this should never happen");
			System.out.println("moves is empty, state is: " + currState.toString());
		}
		int val = 0;
		for (Move m : moves) {
			counter += 1;
			val = minValue(currState.successorState(m), MIN, MAX); //synist ekki skipta mali hvort byrjum a min eda max
			if (val == bestVal) {
				//System.out.println(" counter: " + counter + " moves size " + moves.size());
				stats.print();
				return m;
			}
		}
		//System.out.println("best val " + bestVal + " val: " + val);
		//System.out.println("hvad ertu ad gera her???? ");
		return null;
		//stats.print();
		//return max_move;
		//System.out.println("---max move is--- : " + max_move);
	}

	private int minValue(State s, int alpha_min, int beta_max) {
		int value = MAX;
		
		if(s.isGoalState(envWidth, envHeight)) { //veit hann potto "who's turn it is ?"
			//System.out.println("state: " + s.toString()); 
			if (s.isWhite && myRole.equals("white") || !s.isWhite && myRole.equals("black")) { //if we are the winners
				 return 100; 
			 }
			 else if(s.isWhite && myRole.equals("black") || !s.isWhite && myRole.equals("white")) { //if we are the loosers
				 return 0;
			 }
			 else {
				 return 50; // draw 
			 }
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
		if(s.isGoalState(envWidth, envHeight)) { //veit hann potto "who's turn it is ?"
			//System.out.println("state: " + s.toString()); 
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
