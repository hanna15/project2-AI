package proj2;

public class Position {
    private byte x;
    private byte y;

    public Position(byte x, byte y) {
        this.x = x; this.y = y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    public byte getX() {
    	return x;
    }
    
    public byte getY() {
    	return y;
    }
    
    @Override 
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Position that = (Position) other;
        return this.x == that.x && this.y == that.y;
    }

    @Override
	public int hashCode() {
		int tmp = (y + ((x+1)/2));
        return x + (tmp * tmp);
	}
}