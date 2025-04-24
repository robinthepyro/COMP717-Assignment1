
// package atmp2;

// import java.util.List;

// public class TigerVsDogsMove implements Move<TigerVsDogsMove> {
//     public final Integer startNode;
//     public final Integer endNode;
//     // the node that a dog died on this turn
//     // TODO add functionality to TigerVsDogs.getValidMoves() to add deaths to move.
//     public List<Integer> deadDogs;

//     public TigerVsDogsMove(Integer startNode, Integer endNode){
//         this.startNode = startNode;
//         this.endNode = endNode;
//         this.deadDogs = null;
//     }

// 	public TigerVsDogsMove(Integer startNode, Integer endNode, List<Integer> deadDogs){
//         this.startNode = startNode;
//         this.endNode = endNode;
//         this.deadDogs = deadDogs;
//     }

// 	public List<Integer> getDeadDogs() {
// 		return deadDogs;
// 	}

//     public void setDeadDogs(List<Integer> deadDogs) {
// 		this.deadDogs = deadDogs;
// 	}

//     public TigerVsDogsMove clone(){
//         return new TigerVsDogsMove(startNode, endNode);
//     }

//     @Override
//     public boolean equals(Object o) {
//         if (this == o) return true;
//         if (o == null || getClass() != o.getClass()) return false;
//         TigerVsDogsMove other = (TigerVsDogsMove) o;
//         return startNode == other.startNode && endNode == other.endNode;
//     }

//     @Override
//     public String toString(){
//         return "Start: "+startNode+" End: " +endNode; 

//     }

//     public Integer sortBy(){
//         return endNode;
//     }
// }

package atmp2;

import java.util.ArrayList;
import java.util.List;

public class TigerVsDogsMove implements Move<TigerVsDogsMove> {
    public final Integer startNode;
    public final Integer endNode;

    private List<Integer> deadDogs;

    public TigerVsDogsMove(Integer startNode, Integer endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.deadDogs = null; // No dogs killed by default
    }

    public List<Integer> getDeadDogs() {
        return deadDogs;
    }

    public void setDeadDogs(List<Integer> deadDogs) {
        this.deadDogs = deadDogs;
    }

    @Override
    public TigerVsDogsMove clone() {
        TigerVsDogsMove clonedMove = new TigerVsDogsMove(startNode, endNode);
        if (this.deadDogs != null) {
            clonedMove.setDeadDogs(new ArrayList<>(this.deadDogs)); // Deep clone of the list
        }
        return clonedMove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TigerVsDogsMove other = (TigerVsDogsMove) o;
        return startNode.equals(other.startNode) && endNode.equals(other.endNode)
                && (deadDogs != null ? deadDogs.equals(other.deadDogs) : other.deadDogs == null);
    }

    @Override
    public String toString() {
        String deadDogsString = (deadDogs != null && !deadDogs.isEmpty()) ? " Dead Dogs: " + deadDogs : "";
        return "Start: " + startNode + " End: " + endNode + deadDogsString;
    }

    public Integer sortBy() {
        return endNode;
    }
}
