import java.util.*;

public class Node<T> {
    
    private final T label;
    private final Map<Node<T>, Integer> edges = new LinkedHashMap<>();
    
    Node(T label) {
        this.label = label;
    }
    
    boolean addEdge(Node<T> node, int weight) {
        return edges.put(node, weight) == null;
    }
    
    boolean removeEdge(Node<T> node) {
        return edges.remove(node) == null;
    }
    
    public Collection<Node<T>> getEdges() {
        return edges.keySet();
    }
    
    public int getEdgeWeight(Node<T> node) {
        return edges.get(node);
    }
    
    T getLabel() {
        return this.label;
    }
    
    @Override
    public String toString() {
        return this.label.toString();
    }
    
}
