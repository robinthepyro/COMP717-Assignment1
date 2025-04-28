package atmp2;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class TigerVsDogsMove implements Move<TigerVsDogsMove> {
    public final Coord startNode;
    public final Coord endNode;
    public List<Coord> deadDogs; // List of dead dogs during this move
    private int priority;

    public TigerVsDogsMove(Coord startNode, Coord endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.deadDogs = new ArrayList<>();
        this.priority = 0;
    }

	public void setPriority(int priority) {
		this.priority = priority;
	}

    public Coord getStartNode() {
        return startNode;
    }
    public Coord getEndNode() {
        return endNode;
    }

    public List<Coord> getDeadDogs() {
        return deadDogs;
    }

    public void setDeadDogs(List<Coord> deadDogs) {
        this.deadDogs = deadDogs;
    }

    @Override
    public TigerVsDogsMove clone() {
        // Return a new TigerVsDogsMove with the same startNode, endNode, and deadDogs list
        TigerVsDogsMove clonedMove = new TigerVsDogsMove(this.startNode, this.endNode);
        clonedMove.setDeadDogs(new ArrayList<>(this.deadDogs)); // Create a new list for deadDogs
        return clonedMove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TigerVsDogsMove other = (TigerVsDogsMove) o;
        return startNode == other.startNode && endNode == other.endNode && deadDogs.equals(other.deadDogs);
    }

    @Override
    public Integer sortBy() {
        return priority;

    }

    @Override
    public String toString() {
        return "Move{" +
                "start=" + startNode +
                ", end=" + endNode +
                ", deadDogs=" + deadDogs +
                '}';
    }
}
