public class GraphTester {
    
    public static void main(String[] args) {
        NodeGraph<String> graph = new NodeGraph<>();
        createGraph(graph);
        System.out.println(graph.shortestPathLength("A", "C"));
    }
    
    public static void createGraph(NodeGraph<String> graph) {
        String n1 = "A";
        String n2 = "B";
        String n3 = "C";
        String n4 = "D";
        String n5 = "E";
        String n6 = "F";
        String n7 = "G";
        
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addNode(n4);
        graph.addNode(n5);
        graph.addNode(n6);
        graph.addNode(n7);
        
        graph.addUndirectedEdge(n1, n2, 1);
        graph.addUndirectedEdge(n1, n3, 20);
        graph.addUndirectedEdge(n2, n3, 15);
        graph.addUndirectedEdge(n3, n4, 7);
        graph.addUndirectedEdge(n2, n5, 2);
        graph.addUndirectedEdge(n5, n6, 1);
        graph.addUndirectedEdge(n6, n7, 1);
        graph.addUndirectedEdge(n7, n3, 1);
    }
    
}
