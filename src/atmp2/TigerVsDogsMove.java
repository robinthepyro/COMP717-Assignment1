package atmp2;

import java.util.List;

import atmp2.Coordinate;

import java.util.ArrayList;

public class TigerVsDogsMove implements Move<TigerVsDogsMove> {
    public final Coord startNode;
    public final Coord endNode;
    public List<Coord> deadDogs; // List of dead dogs during this move

    public TigerVsDogsMove(Coord startNode, Coord endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.deadDogs = new ArrayList<>();
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
    public Integer sortBy() {
        return deadDogs.size(); // Sort by the number of dead dogs (more captured dogs is a better move)
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
