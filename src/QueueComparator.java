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
     * @param node1 node 1 to compare
     * @param node2 node 2 to compare
     * @return -1 if n1 < n2, or 1 if n1 >= n2
     */
    @Override
    public int compare(Node<T> node1, Node<T> node2) {
        int path1 = paths[graph.getIndex(node1.getLabel())];
        int path2 = paths[graph.getIndex(node2.getLabel())];
        
        /* If no paths do whatever */
        if (path1 == NOT_FOUND && path2 == NOT_FOUND) {
            return 0;
        /* If node 1 has no path, node 2 has priority */
        } else if (path1 == NOT_FOUND) {
            return 1;
        /* If node 2 has no path, or if node 1's path is less,
         * node 1 has priority */
        } else if (path2 == NOT_FOUND || path1 < path2) {
            return -1;
        /* Node 2 has priority */
        } else {
            return 1;
        }
    }
}
