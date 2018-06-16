public class GraphTester {
    
    public static void main(String[] args) {
        NodeGraph<String> graph = new NodeGraph();
        createGraph(graph);
        System.out.println(graph.shortestPath("1", "4"));
    }
    
    public static void createGraph(NodeGraph<String> graph) {
        String n1 = "1";
        String n2 = "2";
        String n3 = "3";
        String n4 = "4";
        String n5 = "5";
        String n6 = "6";
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addNode(n4);
        graph.addNode(n5);
        graph.addNode(n6);
        graph.addUndirectedEdge(n1, n2, 7);
        graph.addUndirectedEdge(n1, n3, 9);
        graph.addUndirectedEdge(n1, n6, 14);
        graph.addUndirectedEdge(n2, n3, 10);
        graph.addUndirectedEdge(n2, n4, 15);
        graph.addUndirectedEdge(n3, n6, 2);
        graph.addUndirectedEdge(n3, n4, 11);
        graph.addUndirectedEdge(n4, n5, 6);
        graph.addUndirectedEdge(n5, n6, 9);
    }
    
}
