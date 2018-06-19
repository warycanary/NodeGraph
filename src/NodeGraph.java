import java.util.*;

/**
 * Directed, weighted graph structure containing nodes and edges
 * @param <T> Generic. Specify data type contained in the graph on creation
 * @author warycanary
 */
public class NodeGraph<T> {
    
    /**
     * List of nodes in the graph
     */
    private final List<Node<T>> nodes = new ArrayList<>();
    
    /**
     * Map of node positions in the nodes List
     */
    private final Map<T, Integer> indexes = new LinkedHashMap<>();
    
    /**
     * Returned when a node or edge is not found
     */
    private final int NOT_FOUND = -1;
    
    /**
     * Default weight of an edge
     */
    private final int DEFAULT_WEIGHT = 1;
    
    /**
     * Add a new node to the graph
     * @param label data to be contained in the node
     * @return true if the node was added
     */
    public boolean addNode(T label) {
        final int index = getIndex(label);
        if (index == NOT_FOUND) {
            indexes.put(label, nodes.size());
            nodes.add(new Node<>(label));
            return true;
        }
        return false;
    }
    
    /**
     * Remove the specified node from the graph
     * @param label node containing this data will be removed
     * @return true if the node was removed
     */
    public boolean removeNode(T label) {
        final int index = getIndex(label);
        if (index != NOT_FOUND) {
            nodes.remove(index);
            indexes.remove(label);
            /* Update the indexes of the nodes in the Map */
            rehash();
            return true;
        }
        return false;
    }
    
    /**
     * Deletes all nodes and edges from the graph
     */
    public void clearGraph() {
        this.nodes.clear();
    }
    
    /**
     * Add an edge with a direction to the graph, use default weight
     * @param srcLabel the source node label
     * @param destLabel the destination node label
     * @return true if the edge was added
     */
    public boolean addDirectedEdge(T srcLabel, T destLabel) {
        return addDirectedEdge(srcLabel, destLabel, DEFAULT_WEIGHT);
    }
    
    /**
     * Add an edge with a direction to the graph
     * @param srcLabel the source node label
     * @param destLabel the destination node label
     * @param weight the weight of the edge
     * @return true if the edge was added
     */
    public boolean addDirectedEdge(T srcLabel, T destLabel, int weight) {
        final int srcIndex = getIndex(srcLabel);
        final int destIndex = getIndex(destLabel);
        return (srcIndex != NOT_FOUND && destIndex != NOT_FOUND)
                && nodes.get(srcIndex).addEdge(nodes.get(destIndex), weight);
    }
    
    /**
     * Add an edge in both directions to the graph, use default weight
     * @param srcLabel the source node label
     * @param destLabel the destination node label
     * @return true if the edge was added
     */
    public boolean addUndirectedEdge(T srcLabel, T destLabel) {
        return addUndirectedEdge(srcLabel, destLabel, DEFAULT_WEIGHT);
    }
    
    /**
     * Add an edge in both directions to the graph
     * @param srcLabel the source node label
     * @param destLabel the destination node label
     * @param weight the weight of the edge
     * @return true if the edge was removed
     */
    public boolean addUndirectedEdge(T srcLabel, T destLabel, int weight) {
        return addDirectedEdge(srcLabel, destLabel, weight) && addDirectedEdge(destLabel, srcLabel, weight);
    }
    
    /**
     * Remove an edge in both directions from the graph
     * @param srcLabel the source node label
     * @param destLabel the destination node label
     * @return true if the edge was removed
     */
    public boolean removeUndirectedEdge(T srcLabel, T destLabel) {
        return removeDirectedEdge(srcLabel, destLabel) && removeDirectedEdge(destLabel, srcLabel);
    }
    
    /**
     * Remove an edge in a single direction from the graph
     * @param srcLabel the source node label
     * @param destLabel the destination node label
     * @return true if the edge was removed
     */
    public boolean removeDirectedEdge(T srcLabel, T destLabel) {
        final int srcIndex = getIndex(srcLabel);
        final int destIndex = getIndex(destLabel);
        return (srcIndex != NOT_FOUND && destIndex != NOT_FOUND)
                && nodes.get(srcIndex).removeEdge(nodes.get(destIndex));
    }
    
    /**
     * Delete all edges in the graph
     */
    public void removeAllEdges() {
        for (Node<T> node : nodes) {
            node.getEdges().clear();
        }
    }
    
    /**
     * Returns all nodes in the graph
     * @return all nodes stored in the graph
     */
    public Collection<Node<T>> getNodes() {
        return this.nodes;
    }
    
    /**
     * Get a collection of neighbours of a node
     * @param label the requested node
     * @return collection of neighbouring nodes, or an empty list
     */
    public Collection<Node<T>> getNeighbours(T label) {
        final int index = getIndex(label);
        if (index != NOT_FOUND) {
            return nodes.get(index).getEdges();
        }
        /* If no neighbours, return an empty list */
        return new ArrayList<>();
    }
    
    /**
     * Get an order list of nodes of the shortest path between two nodes
     * @param srcLabel the source node label
     * @param destLabel the destination node label
     * @return list of nodes - the shortest path from source to destination
     */
    public List<Node<T>> shortestPath(T srcLabel, T destLabel) {
        List<Node<T>> parents = new ArrayList<>(Collections.nCopies(nodes.size(), null));
        int[] paths = new int[nodes.size()];
        return dijkstra(srcLabel, destLabel, parents, paths);
    }
    
    /**
     * Get the length of the shortest path between two nodes
     * @param srcLabel the source node label
     * @param destLabel the destination node label
     * @return length of the shortest path, or -1 if no path found
     */
    public int shortestPathLength(T srcLabel, T destLabel) {
        List<Node<T>> parents = new ArrayList<>(Collections.nCopies(nodes.size(), null));
        int[] paths = new int[nodes.size()];
        /* If the list is not empty (ie. no path) */
        if (dijkstra(srcLabel, destLabel, parents, paths).size() > 0) {
            return paths[getIndex(destLabel)];
        }
        /* If no path, return an empty list */
        return NOT_FOUND;
    }
    
    /**
     * Implementation of Dijkstra's Algorithm to calculate the shortest path
     * @param srcLabel the source node label
     * @param destLabel the destination node label
     * @param parents list of the parents nodes of each node visited
     * @param paths int array of the shortest paths to each node from the source
     * @return list of nodes - the shortest path from source to destination
     */
    private List<Node<T>> dijkstra(T srcLabel, T destLabel, List<Node<T>> parents, int[] paths) {
        final int srcIndex = getIndex(srcLabel);
        final int destIndex = getIndex(destLabel);
        
        if (srcIndex != NOT_FOUND && destIndex != NOT_FOUND) {
            final Node<T> srcNode = nodes.get(srcIndex);
            final Node<T> destNode = nodes.get(destIndex);
            /* Nodes are marked visited when shortest path is found */
            boolean[] visited = new boolean[nodes.size()];
            /* Priority queue. Shortest paths are visited first */
            Queue<Node<T>> queue = new PriorityQueue<>(new QueueComparator<>(this, paths, NOT_FOUND));
            queue.add(srcNode);
            
            /* Set all path values to -1 */
            Arrays.fill(paths, NOT_FOUND);
            /* Set starting path to 0 */
            paths[srcIndex] = 0;
            
            /* If queue is empty, source and destination are disconnected */
            while (queue.size() > 0) {
                Node<T> currNode = queue.poll();
                assert currNode != null;
                /* Visit the current node */
                updateQueue(currNode, queue, parents, visited, paths);
                /* End when destination is found */
                if (currNode == destNode) {
                    return buildPathList(destNode, srcNode, parents);
                }
            }
        }
        /* Return an empty list */
        return new ArrayList<>();
    }
    
    /**
     * Visits the current node and finds neighbours. Updates path to neighbouring nodes if
     * a shorter path is found. Shortest path to current node is found when a node is visited
     * @param currNode the current node being visited
     * @param queue queue of nodes to be visited
     * @param parents list of parent nodes of each node visited
     * @param visited boolean array of all nodes whose shortest path has been found
     * @param paths int array of the shortest paths to each node from the source
     */
    private void updateQueue(Node<T> currNode, Queue<Node<T>> queue, List<Node<T>> parents, boolean[] visited, int[] paths) {
        final int currIndex = getIndex(currNode.getLabel());
        visited[currIndex] = true;
        /* For all neighbours of the current node */
        for (Node<T> neighbour : currNode.getEdges()) {
            int neighIndex = getIndex(neighbour);
            /* Shortest paths of visited neighbours have already been determined
             * Note: Will not visit self, already marked as visited */
            if (!visited[neighIndex]) {
                /* Path to each neighbour is the path to the current node + neighbour edge weight */
                final int path = paths[currIndex] + currNode.getEdgeWeight(neighbour);
                if (paths[neighIndex] == NOT_FOUND || path < paths[neighIndex]) {
                    parents.set(neighIndex, currNode);
                    paths[neighIndex] = path;
                }
                /* Do not add duplicates to the queue */
                if (!queue.contains(neighbour)) {
                    queue.add(neighbour);
                }
            }
        }
    }
    
    /**
     * Backtracks parents array and builds a List of nodes of the shortest path
     * @param currNode node for backtracking parents, initially the destination node
     * @param srcNode the source node being backtracked to
     * @param parents list of parents of all visited nodes
     * @return list of nodes - the shortest path from source to destination
     */
    private List<Node<T>> buildPathList(Node<T> currNode, Node<T> srcNode, List<Node<T>> parents) {
        List<Node<T>> path = new ArrayList<>();
        /* Add nodes to the list from the destination to source */
        while (currNode != srcNode) {
            final int currIndex = getIndex(currNode.getLabel());
            path.add(currNode);
            currNode = parents.get(currIndex);
        }
        /* Reverse the collection so source is first */
        Collections.reverse(path);
        return path;
    }
    
    /**
     * Updates the indexes of nodes in the Map (ie. when a node is removed)
     */
    private void rehash() {
        for (int i = 0; i < nodes.size(); i++) {
            indexes.put(nodes.get(i).getLabel(), i);
        }
    }
    
    /**
     * Gets the index of a given node in the nodes List
     * @param node the requested node
     * @return the index of the node, or -1 if does not exist
     */
    int getIndex(Node<T> node) {
        return this.getIndex(node.getLabel());
    }
    
    /**
     * Gets the index of a given node label in the nodes List
     * @param label the requested node's label
     * @return the index of the node, or -1 if does not exist
     */
    int getIndex(T label) {
        Integer index = indexes.get(label);
        if (index != null) {
            return index;
        }
        return NOT_FOUND;
    }
    
}
