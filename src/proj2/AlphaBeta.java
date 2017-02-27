package proj2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;



public class AlphaBeta {
	static final int MAX = 101;
	static final int MIN = -1;
	private Stats stats = null;
	static final long cutOff = 1000;
	private byte envWidth;
	private byte envHeight;
	private State currState;
	private String myRole;
	private int playClock;
	private int DEPTH = 100;
	private long startClock;
	private long breakClock;
	public int[][] board_values;
	
	public class TimeExpired extends Exception{};
	
	
	public class Pair {
		int bestValue = MIN;
		Move bestMove = null;
	}
	
	AlphaBeta(byte w, byte h, State curr, String role, int playclock) {
//		board_values = new int[][]{
//			  { 5, 15, 15, 5, 5, 15, 15, 5 },
//			  { 2, 3, 3, 3, 3, 3, 3, 2 },
//			  { 4, 6, 6, 6, 6, 6, 6, 4 },
//			  { 7, 10, 10, 10, 10, 10, 10, 7 },
//			  { 11, 15, 15, 15, 15, 15, 15, 11 },
//			  { 16, 21, 21, 21, 21, 21, 21, 16 },
//			  { 20, 28, 28, 28, 28, 28, 28, 20 },
//			  { 36, 36, 36, 36, 36, 36, 36, 36 }
//		};
		board_values = new int[][]{
				  { 10, 15, 10, 15, 10 },
				  { 4, 6, 6, 6, 4 },
				  { 16, 21, 21, 21, 16 },
				  { 20, 28, 28, 28, 20 },
				  { 30, 30, 30, 30, 30 }
			};
//		
//		board_values = new int[][]{
//				  { 10, 15, 10 },
//				  { 4, 6, 4 },
//				  { 16, 21, 16 },
//				  { 20, 28, 20 },
//				  { 30, 30, 30 }
//			};
		currState = curr;
		envWidth = w;
		envHeight = h;
		myRole = role;
		playClock = playclock;
		breakClock = (playClock*1000) - 300;
	}
	
	public Move iterativeDeepening() {
		stats = new Stats(); 
		Move bestMove = null;
		startClock = System.currentTimeMillis();
		
		for (int d = 1; d < DEPTH; d++) {
			try {
				Pair p = maxMove(currState, MIN, MAX, d);
				bestMove = p.bestMove;
				
				if (p.bestValue == 0 || p.bestValue == 100) {
					stats.print();
					System.out.println(">>>>>>>>>> GOING ALL THE WAY!!!");
					return p.bestMove;
				}
			} catch(TimeExpired e) {
				System.out.println(">>>>>>>>>>> Timed out.");
				return bestMove;
			}
		}	
		
		stats.print();
		return bestMove;
	}
	
//	Move bestMove() { 
//		stats = new Stats(); 
//		// ArrayList<Move> moves = currState.getAllLegalMoves(envWidth, envHeight); 		// get all legal moves for the current state
//		// System.out.println("-----------size of legal moves: " + moves.size());
//		// moves should not be empty
//		Move bestMove = maxMove(currState, MIN, MAX);
//		//System.out.println(bestMove.toString());
//		stats.print();
//		if(bestMove == null) {
//			System.out.println("------------ATH! Best move is null -----------------");
//		}
//		return bestMove;
//	}

	
	private Pair maxMove(State s, int alpha_min, int beta_max, int depth_limit) throws TimeExpired {
		if (System.currentTimeMillis() - startClock >= breakClock) {
			throw new TimeExpired();
		}
		Pair best = new Pair();
		Move bestMove = null;
		int bestVal = MIN;
		
		// sort the moves based on which pawn can kill right away
		ArrayList<Move> orderedMoves = sortArrayList(s.getAllLegalMoves(envWidth, envHeight), s.isWhite);
		System.out.println("Sorted legalMoves: " + orderedMoves + " depth: " + depth_limit);

		for (Move m : orderedMoves) {
			//System.out.println("maxMove(): " + orderedMoves + " bestVal: " + bestVal +", alpha: " + alpha_min + ", beta:" + beta_max);
			int valFromMin = minValue(s.successorState(m), alpha_min, beta_max, depth_limit - 1);
			if ( valFromMin > bestVal) {
				//System.out.println("maxMove( if valfromMin > bestVal" + valFromMin);
				bestVal = valFromMin;
			    bestMove = m;
			}
			if(bestVal >= beta_max) {
				best.bestMove = bestMove;
				best.bestValue = bestVal;
				return best;
			}
			
			//System.out.println("before, alpha: " + alpha_min);
			alpha_min = Math.max(alpha_min, bestVal);
			//System.out.println("WE'RE CHANING!!!!!!! alpha: " + alpha_min  + " best: " + bestVal);
		}
		best.bestMove = bestMove;
		best.bestValue = bestVal;
		return best;
	}
	
	private int minValue(State s, int alpha_min, int beta_max, int depth_limit) throws TimeExpired {
		if (System.currentTimeMillis() - startClock >= breakClock) {
			throw new TimeExpired();
		}
		int bestVal = MAX;
		if(s.isGoalState(envWidth, envHeight)) { //veit hann potto "who's turn it is ?"
			//System.out.println("goal state from min : " + s.toString());
			bestVal = utility(s);
			return bestVal; 
		}
		
		if (depth_limit == 0) {
			return heuristic(s);
		}
		
		ArrayList<Move> orderedMoves = sortArrayList(s.getAllLegalMoves(envWidth, envHeight), s.isWhite);
		
		for (Move m : orderedMoves) {
			//System.out.println("minValue(): " + orderedMoves + " bestVal: " + bestVal +", alpha: " + alpha_min + ", beta:" + beta_max);
			int valFromMax = maxValue(s.successorState(m), alpha_min, beta_max, depth_limit - 1);
			if (valFromMax < bestVal) {
				bestVal = valFromMax;
			}
			if(bestVal <= alpha_min) {
				return bestVal;
			}
			//System.out.println("before, beta: " + beta_max);
			beta_max = Math.min(beta_max, bestVal);
			//System.out.println("WE'RE CHANING!!!!!!! beta: " + beta_max + " best: " + bestVal);
		}
		return bestVal;
	}
	
    private int maxValue(State s, int alpha_min, int beta_max, int depth_limit) throws TimeExpired {
		if (System.currentTimeMillis() - startClock >= breakClock) {
			throw new TimeExpired();
		}
		int bestVal = MIN;
		
		if(s.isGoalState(envWidth, envHeight)) { //veit hann potto "who's turn it is ?"
			//System.out.println("goal state from max : " + s.toString());
			bestVal = utility(s);
			return bestVal; 
		}
		
		if (depth_limit == 0) {
			return heuristic(s);
		}
		
		ArrayList<Move> orderedMoves = sortArrayList(s.getAllLegalMoves(envWidth, envHeight), s.isWhite);
		
		// Sort the legal moves according to the heuristic
		
		for (Move m : orderedMoves) {
			//System.out.println("maxValue(): " + orderedMoves + " bestVal: " + bestVal +", alpha: " + alpha_min + ", beta:" + beta_max);
			int valFromMin = minValue(s.successorState(m), alpha_min, beta_max, depth_limit - 1);
			
			if (valFromMin > bestVal) {
				bestVal = valFromMin;
			}
			
			if(bestVal >= beta_max) {
				return bestVal;
			}
			
			//System.out.println("before, alpha: " + alpha_min);
			alpha_min = Math.max(alpha_min, bestVal);
			//System.out.println("WE'RE CHANING!!!!!!! alpha:" + alpha_min  + " best: " + bestVal);
		}
		return bestVal;
	}
	
    public ArrayList<Move> sortArrayList(ArrayList<Move> orderedMoves, boolean isWhite) {
		Collections.sort(orderedMoves, new Comparator<Move>() {
			@Override
			public int compare(Move m1, Move m2) {
		        if (m1.getKill() && !m2.getKill()) {
		        	return -1;
		        }
		        else if ((m1.getKill() && m2.getKill()) || !(m1.getKill() && m2.getKill())) {
//					if (isWhite) {
//						if (m1.getNewY() > m2.getNewY()) {
//							return -1;
//						}
//					}
//					else {
//						if (m1.getNewY() < m2.getNewY()) {
//							return -1;
//						}
//					}
//					return 0;
		        	if (isWhite) {
		        		
		        		//System.out.println("Y " + (m1.getNewY()-1) + " X: " + (m1.getNewX()-1));
		        		if (board_values[m1.getNewY()-1][m1.getNewX()-1] < board_values[m2.getNewY()-1][m2.getNewX()-1]) {
		        			return 1;
		        		}
		        		else if (board_values[m1.getNewY()-1][m1.getNewX()-1] > board_values[m2.getNewY()-1][m2.getNewX()-1]) {
		        			return -1;
		        		}
		        		else return 0;
		        	}
		        	else {
		        		if (board_values[m1.getNewY()-1][m1.getNewX()-1] < board_values[m2.getNewY()-1][m2.getNewX()-1]) {
		        			return -1;
		        		}
		        		else if (board_values[m1.getNewY()-1][m1.getNewX()-1] > board_values[m2.getNewY()-1][m2.getNewX()-1]) {
		        			return 1;
		        		}
		        		else return 0;
		        	}
		        }
		        else {
		        	return 1;
		        }
			}
		});
		
		return orderedMoves;
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
	
    
	private int heuristic1(State s) {
		int whiteDistToGoal = envHeight;
		int blackDistToGoal = 1;
		int whiteSize = s.whites.size();
		int blackSize = s.blacks.size();
		
		
		for (Position p : s.whites) {
			if((envHeight-p.getY()) < whiteDistToGoal) {
				whiteDistToGoal = envHeight - p.getY();
			}
		}
		
		for (Position p : s.blacks) {
			if((envHeight-p.getY()) > blackDistToGoal) {
				blackDistToGoal = p.getY();
			}
		}
		
		if (s.isWhite) {
			return (50 - whiteDistToGoal + blackDistToGoal) + (whiteSize + (whiteSize - blackSize));
		}
		
		else {
			return (50 + whiteDistToGoal - blackDistToGoal) - (blackSize + (blackSize - whiteSize));
		}
	}
	
	
	private int utility(State s) {
		if (s.isWhite && myRole.equals("white") || !s.isWhite && myRole.equals("black")) { //if we are the winners
			 return 100; 
		 }
		 else if(s.isWhite && myRole.equals("black") || !s.isWhite && myRole.equals("white")) { //if we are the losers
			 return 0;
		 }
		 else {
			 return 50; //draw
		 }
	}
}
