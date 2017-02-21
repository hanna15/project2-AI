package proj2;

public class Stats {
	private Long startTime;
	
	public Stats() {
        startTime = System.currentTimeMillis();
    }
	
	public void print() {
        System.out.println("------------------------------");
        Long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Time: " + estimatedTime.toString() + " ms");
        System.out.println("----------------------------");
    }	
}

