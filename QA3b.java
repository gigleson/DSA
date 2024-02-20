//  Implement Kruskal algorithm and priority queue using minimum heap 
import java.util.*;

public class QA3b {

    // Class representing an edge with source, destination, and weight
    public static class Edge implements Comparable<Edge> {
        int source, destination, weight;

        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    // Class implementing the disjoint-set data structure
    public static class DisjointSet {
        int[] parent, rank;

        public DisjointSet(int vertices) {
            parent = new int[vertices];
            rank = new int[vertices];
            for (int i = 0; i < vertices; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        int find(int vertex) {
            if (parent[vertex] != vertex) {
                parent[vertex] = find(parent[vertex]);
            }
            return parent[vertex];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootX] = rootY;
                rank[rootY]++;
            }
        }
    }

    // Class implementing Kruskal's Algorithm to find Minimum Spanning Tree
    public static class KruskalsAlgorithm {
        public static List<Edge> kruskalMST(List<Edge> edges, int vertices) {
            // Sort edges based on weight
            Collections.sort(edges);
            List<Edge> minimumSpanningTree = new ArrayList<>();
            DisjointSet disjointSet = new DisjointSet(vertices);

            // Iterate through sorted edges
            for (Edge edge : edges) {
                int rootSource = disjointSet.find(edge.source);
                int rootDestination = disjointSet.find(edge.destination);

                // If adding the current edge doesn't form a cycle, add it to the MST
                if (rootSource != rootDestination) {
                    minimumSpanningTree.add(edge);
                    disjointSet.union(rootSource, rootDestination);
                }
            }

            return minimumSpanningTree;
        }
    }

    public static void main(String[] args) {
        int vertices = 4;
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 10));
        edges.add(new Edge(0, 2, 6));
        edges.add(new Edge(0, 3, 5));
        edges.add(new Edge(1, 3, 15));
        edges.add(new Edge(2, 3, 4));

        // Apply Kruskal's Algorithm to find Minimum Spanning Tree
        List<Edge> minimumSpanningTree = KruskalsAlgorithm.kruskalMST(edges, vertices);

        // Print Minimum Spanning Tree edges
        System.out.println("Minimum Spanning Tree edges:");
        for (Edge edge : minimumSpanningTree) {
            System.out.println(edge.source + " - " + edge.destination + " : " + edge.weight);
        }
    }
}
