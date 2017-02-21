package proj2;


import java.util.HashSet;
import java.util.Random;

public class RemoteAgent implements Agent {
	private Random random = new Random();
	private String role; // the name of this agent's role (white or black)
	private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
	private boolean myTurn; // whether it is this agent's turn or not
	private int width, height; // dimensions of the board
	private State currState;
	
	/*
		init(String role, int playclock) is called once before you have to select the first action. Use it to initialize the agent. role is either "white" or "black" and playclock is the number of seconds after which nextAction must return.
	*/
    public void init(String role, int width, int height, int playclock) {
    	this.role = role;
		this.playclock = playclock;
		myTurn = !role.equals("white");
		this.width = width;
		this.height = height;
		HashSet<Position> whites = new HashSet<Position>();
		HashSet<Position> blacks = new HashSet<Position>();
		for (int i = 1; i < this.width; i++) {
			Position posW1 = new Position(i,1);
			Position posW2 = new Position(i,2);
			whites.add(posW1);
			whites.add(posW2);
			Position posB1 = new Position(i, this.height);
			Position posB2 = new Position(i, this.height - 1);
			blacks.add(posB1);
			blacks.add(posB2);
		}
		this.currState = new State(whites, blacks, true);
    }

	// lastMove is null the first time nextAction gets called (in the initial state)
    // otherwise it contains the coordinates x1,y1,x2,y2 of the move that the last player did
    public String nextAction(int[] lastMove) {
    	Move m = null;
    	if (lastMove != null) {
    		int x1 = lastMove[0], y1 = lastMove[1], x2 = lastMove[2], y2 = lastMove[3];
    		String roleOfLastPlayer;
    		if (myTurn && role.equals("white") || !myTurn && role.equals("black")) {
    			roleOfLastPlayer = "white";
    		} else {
    			roleOfLastPlayer = "black";
    		}
   			System.out.println(roleOfLastPlayer + " moved from " + x1 + "," + y1 + " to " + x2 + "," + y2);
    		// TODO: 1. update your internal world model according to the action that was just executed
   			boolean isKill = x2 != x1;
   			m = new Move(new Position(x1,y1), new Position(x2,y2), isKill);
   			currState = currState.successorState(m);
   			
    	}
		
    	// update turn (above that line it myTurn is still for the previous state)
		myTurn = !myTurn;
		if (myTurn ) {
			// ATH!!
			if (currState.isGoalState(width, height)) {
				return m.toString();
			}
			// TODO: 2. run alpha-beta search to determine the best move
			// Here we just construct a random move (that will most likely not even be possible),
			// this needs to be replaced with the actual best move.
			
			//ArrayList<Move> legalMoves = currState.getAllLegalMoves(width, height);
			//Move m = legalMoves.get(random.nextInt(legalMoves.size()));
			//return m.toString();
			
			// Create the minimax search
			AlphaBeta ABsearch = new AlphaBeta(this.width, this.height, currState, role);
	        Move nextMove = ABsearch.bestMove();
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
//		this.role = " ";
//		this.playclock = playclock;
//		myTurn = !role.equals("white");
//		this.width = width;
//		this.height = height;
//		HashSet<Position> whites = new HashSet<Position>();
//		HashSet<Position> blacks = new HashSet<Position>();
//		for (int i = 1; i < this.width; i++) {
//			Position posW1 = new Position(i,1);
//			Position posW2 = new Position(i,2);
//			whites.add(posW1);
//			whites.add(posW2);
//			Position posB1 = new Position(i, this.height);
//			Position posB2 = new Position(i, this.height - 1);
//			blacks.add(posB1);
//			blacks.add(posB2);
//		}
//		this.currState = new State(whites, blacks, true);
//		
	}
}
