import java.util.Comparator;

/**
 * Compares nodes in priority queue and puts shortest path node first
 * @author warycanary
 */
public class QueueComparator<T> implements Comparator<Node<T>> {
    
    private final NodeGraph<T> graph;
    private final int [] paths;
    private final int NOT_FOUND;
    
    /**
     * Constructs a comparator for a priority queue
     * @param graph reference to the graph for accessing methods
     * @param paths array of shortest paths from a specific node
     * @param NOT_FOUND constant indicating no path
     */
    QueueComparator(NodeGraph<T> graph, int[] paths, int NOT_FOUND) {
        this.graph = graph;
        this.paths = paths;
        this.NOT_FOUND = NOT_FOUND;
    }
    
    /**
     * Compares two nodes based on their shortest path
     * @param n1 node 1 to compare
     * @param n2 node 2 to compare
     * @return -1 if n1 < n2, or 1 if n1 >= 2
     */
    @Override
    public int compare(Node<T> n1, Node<T> n2) {
        int path1 = paths[graph.getIndex(n1)];
        int path2 = paths[graph.getIndex(n2)];
        
        /* If no paths do whatever */
        if (path1 == NOT_FOUND && path2 == NOT_FOUND) {
            return 0;
        /* If node 1 has a path, it has priority */
        } else if (path1 == NOT_FOUND) {
            return 1;
        /* If node 2 has a path, it has priority */
        } else if (path2 == NOT_FOUND) {
            return -1;
        /* If node 1's path is less, it has priority */
        } else if (path1 < path2) {
            return -1;
        /* Node 2 has priority */
        } else {
            return 1;
        }
    }
}
