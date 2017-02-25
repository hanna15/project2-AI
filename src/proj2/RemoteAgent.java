package proj2;


import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class RemoteAgent implements Agent {
	private Random random = new Random();
	private String role; // the name of this agent's role (white or black)
	private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
	private boolean myTurn; // whether it is this agent's turn or not
	private byte width, height; // dimensions of the board
	private State currState;
	private AlphaBeta AB;
	
	/*
		init(String role, int playclock) is called once before you have to select the first action. Use it to initialize the agent. role is either "white" or "black" and playclock is the number of seconds after which nextAction must return.
	*/
    public void init(String role, int width, int height, int playclock) {
    	this.role = role;
		this.playclock = playclock;
		myTurn = !role.equals("white");
		this.width = (byte)width;
		this.height = (byte)height;
		Deque<Position> whites = new LinkedList<Position>();
		Deque<Position> blacks = new LinkedList<Position>();
		// 
		for (byte i = 1; i <= this.width; i++) {
			whites.add(new Position(i, (byte)1));
			whites.add(new Position(i, (byte)2));
			blacks.add(new Position(i, this.height));
			blacks.add(new Position(i, (byte)(this.height-1)));
		}
		this.currState = new State(whites, blacks, true);
    }

	// lastMove is null the first time nextAction gets called (in the initial state)
    // otherwise it contains the coordinates x1,y1,x2,y2 of the move that the last player did
    public String nextAction(int[] lastMove) {
    	Move m = null;
    	if (lastMove != null) {
    		byte 	x1 = (byte)lastMove[0],
    				y1 = (byte)lastMove[1], 
    				x2 = (byte)lastMove[2], 
    				y2 = (byte)lastMove[3];
    		
    		String roleOfLastPlayer;
    		if (myTurn && role.equals("white") || !myTurn && role.equals("black")) {
    			roleOfLastPlayer = "white";
    		} 
    		else {
    			roleOfLastPlayer = "black";
    		}
    		
   			System.out.println(roleOfLastPlayer + " moved from " + x1 + "," + y1 + " to " + x2 + "," + y2);
   			
   			boolean isKill = (x2 != x1); // if moved diagonally, the move must be a kill
   			
   			m = new Move(new Position(x1,y1), new Position(x2,y2), isKill);
   			currState = currState.successorState(m); // update the current state
    	}
		
    	// update turn (above that line it myTurn is still for the previous state)
		myTurn = !myTurn;
		if (myTurn) {
			// check if goalstate
			if (currState.isGoalState(width, height)) {
				return m.toString();
			}
			
			// Create the minimax search
			AB = new AlphaBeta(this.width, this.height, currState, role, playclock);
	        Move nextMove = AB.iterativeDeepening();
	        return nextMove.toString();
		} 
		else {
			return "noop";
		}
	}

	// is called when the game is over or the match is aborted
	@Override
	public void cleanup() {
		// TODO: cleanup so that the agent is ready for the next match
	
	}
}
