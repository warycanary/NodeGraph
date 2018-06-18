import java.util.*;

/**
 * This class implements a Node which is contained in a tree graph
 * @author warycanary
 */
public class Node<T> {
    
    /**
     * Data contained in the node
     */
    private final T label;
    
    /**
     * A map nodes this node is connected to
     */
    private final Map<Node<T>, Integer> edges;
    
    /**
     * Constructs a node with a label
     * @param label data contained in each node
     */
    Node(T label) {
        this.label = label;
        edges = new LinkedHashMap<>();
    }
    
    /**
     * Adds a new edge between this node and the node
     * @param node the node this node shares an edge with
     * @param weight the weight of the edge
     * @return true if node was successfully added
     */
    boolean addEdge(Node<T> node, int weight) {
        return edges.put(node, weight) == null;
    }
    
    /**
     * Removes the edge between this node and the node
     * @param node the node to remove an edge with
     * @return true if the edge was removed
     */
    boolean removeEdge(Node<T> node) {
        return edges.remove(node) == null;
    }
    
    /**
     * Gets all nodes this node connects to
     * @return Collection of nodes this node connects to
     */
    Collection<Node<T>> getEdges() {
        return edges.keySet();
    }
    
    /**
     * Gets the weight of an edge with a neighbouring node.
     * @param node a node this node shares an edge with
     * @return weight of the edge, or -1 if no edge exists
     */
    int getEdgeWeight(Node<T> node) {
        Integer weight = edges.get(node);
        return weight != null ? weight : -1;
    }
    
    /**
     * @return this node's label
     */
    T getLabel() {
        return this.label;
    }

    /**
     * @return string representation of the data contained in this node
     */
    @Override
    public String toString() {
        return this.label.toString();
    }
    
}
