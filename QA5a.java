// Implement ant colony algorithm solving travelling a salesman problem 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class AntColony {

    private double[][] pheromones;
    private int[][] distances;
    private int numAnts;
    private double decay;
    private double alpha;
    private double beta;

    public AntColony(int[][] distances, int numAnts, double decay, double alpha, double beta) {
        this.distances = distances;
        this.pheromones = new double[distances.length][distances.length];
        this.numAnts = numAnts;
        this.decay = decay;
        this.alpha = alpha;
        this.beta = beta;

        // Initialize pheromones
        for (int i = 0; i < pheromones.length; i++) {
            Arrays.fill(pheromones[i], 1.0);
        }
    }

    public List<Integer> findPath() {
        List<Integer> bestPath = null;
        double bestPathLength = Double.POSITIVE_INFINITY;

        for (int iteration = 0; iteration < 100; iteration++) {
            List<List<Integer>> antPaths = new ArrayList<>();
            double[] antPathLengths = new double[numAnts];

            // Generate paths for each ant
            for (int ant = 0; ant < numAnts; ant++) {
                List<Integer> path = generatePath();
                antPaths.add(path);
                antPathLengths[ant] = calculatePathLength(path);
            }

            // Update pheromones
            updatePheromones(antPaths, antPathLengths);

            // Find the best path
            for (int ant = 0; ant < numAnts; ant++) {
                if (antPathLengths[ant] < bestPathLength) {
                    bestPathLength = antPathLengths[ant];
                    bestPath = antPaths.get(ant);
                }
            }

            // Decay pheromones
            for (int i = 0; i < pheromones.length; i++) {
                for (int j = 0; j < pheromones[i].length; j++) {
                    pheromones[i][j] *= decay;
                }
            }
        }

        return bestPath;
    }

    private List<Integer> generatePath() {
        List<Integer> path = new ArrayList<>();
        boolean[] visited = new boolean[distances.length];
        Random random = new Random();

        // Start from a random city
        int currentCity = random.nextInt(distances.length);
        path.add(currentCity);
        visited[currentCity] = true;

        // Move to the next city based on pheromone levels and distances
        for (int step = 1; step < distances.length; step++) {
            int nextCity = selectNextCity(currentCity, visited);
            path.add(nextCity);
            visited[nextCity] = true;
            currentCity = nextCity;
        }

        return path;
    }

    private int selectNextCity(int currentCity, boolean[] visited) {
        double[] probabilities = new double[distances.length];
        double totalProbability = 0.0;

        for (int i = 0; i < distances.length; i++) {
            if (!visited[i]) {
                double pheromone = Math.pow(pheromones[currentCity][i], alpha);
                double distance = Math.pow(1.0 / distances[currentCity][i], beta);
                probabilities[i] = pheromone * distance;
                totalProbability += probabilities[i];
            }
        }

        // Select the next city based on probabilities
        Random random = new Random();
        double randomValue = random.nextDouble() * totalProbability;

        for (int i = 0; i < probabilities.length; i++) {
            if (!visited[i]) {
                randomValue -= probabilities[i];
                if (randomValue <= 0.0) {
                    return i;
                }
            }
        }

        // Should not reach here
        return -1;
    }

    private double calculatePathLength(List<Integer> path) {
        double length = 0.0;

        for (int i = 0; i < path.size() - 1; i++) {
            length += distances[path.get(i)][path.get(i + 1)];
        }

        length += distances[path.get(path.size() - 1)][path.get(0)]; // Return to the starting city

        return length;
    }

    private void updatePheromones(List<List<Integer>> antPaths, double[] antPathLengths) {
        for (int i = 0; i < antPaths.size(); i++) {
            List<Integer> path = antPaths.get(i);

            for (int j = 0; j < path.size() - 1; j++) {
                int city1 = path.get(j);
                int city2 = path.get(j + 1);

                // Update pheromone level based on the length of the path
                pheromones[city1][city2] += 1.0 / antPathLengths[i];
                pheromones[city2][city1] += 1.0 / antPathLengths[i];
            }

            // Update pheromone for the last edge connecting the last and first cities
            int lastCity = path.get(path.size() - 1);
            int firstCity = path.get(0);
            pheromones[lastCity][firstCity] += 1.0 / antPathLengths[i];
            pheromones[firstCity][lastCity] += 1.0 / antPathLengths[i];
        }
    }

    public static void main(String[] args) {
        int[][] distances = {
                {0, 2, 9, 10},
                {1, 0, 6, 4},
                {15, 7, 0, 8},
                {6, 3, 12, 0}
        };

        int numAnts = 5;
        double decay = 0.95;
        double alpha = 1.0;
        double beta = 2.0;

        AntColony antColony = new AntColony(distances, numAnts, decay, alpha, beta);
        List<Integer> bestPath = antColony.findPath();

        System.out.println("Best Path: " + bestPath);
        System.out.println("Best Path Length: " + antColony.calculatePathLength(bestPath));
    }
}
