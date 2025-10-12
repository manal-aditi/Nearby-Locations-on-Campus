import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class handles the backend logic for working with a graph of locations.
 * It loads graph data, finds shortest paths, and retrieves location info.
 */
public class Backend implements BackendInterface {

    private GraphADT<String, Double> graph;

    // constructor that accepts a graph to work with
    public Backend(GraphADT<String, Double> graph) {
        this.graph = graph;
    }

    @Override
    public void loadGraphData(String filename) throws IOException {
        // clear any existing nodes and edges before loading a new file
        graph.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // skip empty lines, comments, or non-edge lines
                if (line.startsWith("//") || line.isEmpty() || line.contains("digraph") || line.equals("}"))
                    continue;

                // expected format: A -> B [label="5.0"];
                if (line.contains("->") && line.contains("label=")) {
                    String[] parts = line.split("->");
                    String from = parts[0].trim();

                    String[] destAndLabel = parts[1].split("\\[label=");
                    String to = destAndLabel[0].trim();

                    // extract the number from the label, removing extra characters
                    String labelStr = destAndLabel[1].replaceAll("[^0-9.]", "");
                    Double weight = Double.parseDouble(labelStr);

                    // add the nodes and edge to the graph
                    graph.insertNode(from);
                    graph.insertNode(to);
                    graph.insertEdge(from, to, weight);
                }
            }
        }
    }

    @Override
    public List<String> getListOfAllLocations() {
        // return all the nodes (locations) in the graph
        return new ArrayList<>(graph.getAllNodes());
    }

    @Override
    public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {
        // try to get the shortest path as a list of node names
        try {
            return graph.shortestPathData(startLocation, endLocation);
        } catch (Exception e) {
            // if something goes wrong or path doesn't exist, return an empty list
            return new ArrayList<>();
        }
    }

    @Override
    public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {
        // try to get the travel time (weights) between each pair of nodes on the path
        try {
            return graph.shortestPathCostBetweenSteps(startLocation, endLocation);
        } catch (Exception e) {
            // if there's an issue or no path, return an empty list
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getTenClosestDestinations(String startLocation) throws NoSuchElementException {
        // check if the starting location is in the graph
        if (!graph.containsNode(startLocation)) {
            throw new NoSuchElementException("start location not found");
        }

        // get the shortest path cost to every reachable node
        Map<String, Double> shortestPaths = graph.shortestPathCostFrom(startLocation);

        // return the 10 closest (fastest to reach) destinations, excluding the start
        return shortestPaths.entrySet().stream()
                .filter(e -> !e.getKey().equals(startLocation))
                .sorted(Map.Entry.comparingByValue())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
