package atmp2;

public class Coord {
    public int row;
    public int col;

    // Constructor
    public Coord(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Coord other = (Coord) o;
        return row == other.row && col == other.col;
    }

    public String toString() {
        return row + "," + col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}
