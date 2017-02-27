package proj2;

public class Stats {
	private Long startTime;
	private int expansions;
	
	public Stats() {
        startTime = System.currentTimeMillis();
        expansions = 0;
    }
	
	public void increaseExpansions() {
		expansions++;
	}
	public void print() {
        System.out.println("------------------------------");
        Long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Expansions: " + expansions);
        System.out.println("Time: " + estimatedTime.toString() + " ms");
        System.out.println("----------------------------");
    }	
}

