import java.util.*;

public class NodeGraph<T> {
    
    private final List<Node<T>> nodes = new ArrayList<>();
    private final Map<T, Integer> indexes = new LinkedHashMap<>();
    private final int NOT_FOUND = -1;
    
    public boolean addNode(T label) {
        final int index = getIndex(label);
        if (index == NOT_FOUND) {
            indexes.put(label, nodes.size());
            nodes.add(new Node<>(label));
            return true;
        }
        return false;
    }
    
    public boolean removeNode(T label) {
        final int index = getIndex(label);
        if (index != NOT_FOUND) {
            nodes.remove(index);
            indexes.remove(label);
            rehash();
            return true;
        }
        return false;
    }
    
    public boolean addDirectedEdge(T srcLabel, T destLabel, int weight) {
        final int srcIndex = getIndex(srcLabel);
        final int destIndex = getIndex(destLabel);
        return (srcIndex != NOT_FOUND && destIndex != NOT_FOUND)
                && nodes.get(srcIndex).addEdge(nodes.get(destIndex), weight);
    }
    
    public boolean addDirectedEdge(T srcLabel, T destLabel) {
        return addDirectedEdge(srcLabel, destLabel, 1);
    }
    
    public boolean addUndirectedEdge(T srcLabel, T destLabel, int weight) {
        return addDirectedEdge(srcLabel, destLabel, weight) && addDirectedEdge(destLabel, srcLabel, weight);
    }
    
    public boolean addUndirectedEdge(T srcLabel, T destLabel) {
        return addUndirectedEdge(srcLabel, destLabel, 1);
    }
    
    public boolean removeDirectedEdge(T srcLabel, T destLabel) {
        final int srcIndex = getIndex(srcLabel);
        final int destIndex = getIndex(destLabel);
        return (srcIndex != NOT_FOUND && destIndex != NOT_FOUND)
                && nodes.get(srcIndex).removeEdge(nodes.get(destIndex));
    }
    
    public boolean removeUndirectedEdge(T srcLabel, T destLabel) {
        return removeDirectedEdge(srcLabel, destLabel) && removeDirectedEdge(destLabel, srcLabel);
    }
    
    public Collection<Node<T>> getEdges(T label) {
        final int index = getIndex(label);
        if (index != NOT_FOUND) {
            return nodes.get(index).getEdges();
        }
        return null;
    }
    
    public int shortestPath(T srcLabel, T destLabel) {
        final int srcIndex = getIndex(srcLabel);
        final int destIndex = getIndex(destLabel);
        
        if (srcIndex != NOT_FOUND && destIndex != NOT_FOUND) {
            Queue<Node<T>> queue = new LinkedList<>();
            queue.add(nodes.get(srcIndex));
            
            /* Create ArrayLists */
            boolean [] visited = new boolean[nodes.size()];
            int [] paths = new int[nodes.size()];
            Arrays.fill(paths, NOT_FOUND);
            paths[srcIndex] = 0;
            
            while (queue.size() > 0) {
                Node<T> current = queue.poll();
                assert current != null;
                updateQueue(current, queue, visited, paths);
                if (current == nodes.get(destIndex)) {
                    return paths[destIndex];
                }
            }
        }
        return NOT_FOUND;
    }
    
    private void updateQueue(Node<T> current, Queue<Node<T>> queue, boolean [] visited, int [] paths) {
        final int currIndex = getIndex(current.getLabel());
        
        for (Node<T> neighbour : current.getEdges()) {
            final int neighIndex = getIndex(neighbour);
            if (!visited[neighIndex]) {
                queue.add(neighbour);
                visited[currIndex] = true;
                
                final int edgeWeight = current.getEdgeWeight(neighbour);
                final int currPath = paths[currIndex] + edgeWeight;
                if (paths[neighIndex] == NOT_FOUND) {
                    paths[neighIndex] = currPath;
                } else if (currPath < paths[neighIndex]) {
                    paths[neighIndex] = currPath;
                }
            }
        }
    }
    
    private void rehash() {
        for (int i = 0; i < nodes.size(); i++) {
            indexes.put(nodes.get(i).getLabel(), i);
        }
    }
    
    private int getIndex(T label) {
        Integer index = indexes.get(label);
        if (index != null) {
            return index;
        }
        return NOT_FOUND;
    }
    
    private int getIndex(Node<T> node) {
        return this.getIndex(node.getLabel());
    }
    
}
