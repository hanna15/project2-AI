package proj2;
public interface Agent
{
    public void init(String role, int width, int height, int playclock);
    public String nextAction(int[] lastmove);
    public void cleanup();
}
