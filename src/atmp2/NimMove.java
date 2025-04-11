package atmp2;

public class NimMove implements Move<NimMove> {
    public final int pileIndex;
    public final int pips;

    public NimMove(int pileIndex, int pipsRemoved) {
        this.pileIndex = pileIndex;
        this.pips = pipsRemoved;
    }

    @Override
    public NimMove clone() {
        return new NimMove(pileIndex, pips);
    }

    @Override
    public String toString() {
        return "Remove " + pips + " from pile " + pileIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NimMove other = (NimMove) o;
        return pileIndex == other.pileIndex && pips == other.pips;
    }

    @Override
    public int hashCode() {
        return 31 * pileIndex + pips;
    }

    @Override
	public Integer sortBy() {
		return pips;
	}
}
