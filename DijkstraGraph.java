// === CS400 File Header Information ===
// Name: Manal Aditi
// Email: aditi@wisc.edu
// Group and Team: <P2.2713 : two letters, and team color <code>
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;


/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
    extends BaseGraph<NodeType, EdgeType>
    implements GraphADT<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode
   * contains data about one specific path between the start node and another
   * node in the graph. The final node in this path is stored in its node
   * field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referened by the predecessor
   * field (this field is null within the SearchNode containing the starting
   * node in its node field).
   *
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost
   * SearchNode has the highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node;
    public double cost;
    public SearchNode predecessor;

    public SearchNode(Node node, double cost, SearchNode predecessor) {
      this.node = node;
      this.cost = cost;
      this.predecessor = predecessor;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost)
        return +1;
      if (cost < other.cost)
        return -1;
      return 0;
    }
  }

  /**
   * Constructor that sets the map that the graph uses.
   */
  public DijkstraGraph() {
    super(new HashtableMap<>());
  }

  /**
   * This helper method creates a network of SearchNodes while computing the
   * shortest path between the provided start and end locations. The
   * SearchNode that is returned by this method is represents the end of the
   * shortest path that is found: it's cost is the cost of that shortest path,
   * and the nodes linked together through predecessor references represent
   * all of the nodes along that shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found
   *                                or when either start or end data do not
   *                                correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end) {
    //check to see if the graph has both the starting node and the end node
    if (!nodes.containsKey(start) || !nodes.containsKey(end)) {
      throw new NoSuchElementException("The nodes don't exist in the graph.");
    }
    //make a priority queue and list
    PriorityQueue<SearchNode> priorityQueue = new PriorityQueue<>();
    List<Node> visitedNodes = new LinkedList<>();

    //get the start node and end node values and set them
    Node starterNode = nodes.get(start);
    Node finalNode = nodes.get(end);

    //add a new node to the priority queue
    priorityQueue.add(new SearchNode(starterNode, 0.0, null));

    //while the priority queue isn't empty, check the nodes
    while (!priorityQueue.isEmpty()) {
      //sets the head of the queue to current
      SearchNode current = priorityQueue.poll();
      //when the current node is equal to the final node, it's returned
      if (current.node == finalNode) {
        return current;
      }
      //if the visited nodes have the current node, then the loop of searching should continue
      if (visitedNodes.contains(current.node)) {
        continue;
      }
      //now it's visited, it's added to the visited list
      visitedNodes.add(current.node);
      //go throughout the edge and update the costs
      for (Edge edges : current.node.edgesLeaving) {
        Node nextNode = edges.successor;
        double newCost = current.cost + edges.data.doubleValue();
        //if the visited nodes list doesn't have the next node, add to priority queue
        if (!visitedNodes.contains(nextNode)) {
          priorityQueue.add(new SearchNode(nextNode, newCost, current));
        }
      }
    }
    //if there's no potential path, returns error
    throw new NoSuchElementException("No path here exists");
  }

  /**
   * Returns the list of data values from nodes along the shortest path
   * from the node with the provided start value through the node with the
   * provided end value. This list of data values starts with the start
   * value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shorteset path. This
   * method uses Dijkstra's shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end) {
    // implement in step 5.4
    LinkedList<NodeType> path = new LinkedList<>();
    //finds the shortest path using computerShortestPath
    SearchNode finalNode = computeShortestPath(start, end);
    //using this data, it goes through the path of the nodes and adds them to the linked list
    for (SearchNode node = finalNode; node != null; node = node.predecessor) {
      path.addFirst(node.node.data);
    }
    //returns list of nodes in shortest path
    return path;
  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest
   * path freom the node containing the start data to the node containing the
   * end data. This method uses Dijkstra's shortest path algorithm to find
   * this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   */
  public double shortestPathCost(NodeType start, NodeType end) {
    //use the method computeShortestPath and use the inbuilt cost function
    double cost = computeShortestPath(start, end).cost;
    return cost;
  }

  // TODO: implement 3+ tests in step 4.1

  @Test
  public void testOne() {
    //Find shortest path from node D to node I
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    //insert the nodes like the class example
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("M");
    graph.insertNode("I");
    graph.insertNode("E");
    graph.insertNode("D");
    graph.insertNode("F");
    graph.insertNode("G");
    graph.insertNode("H");
    graph.insertNode("L");

    // make the edge weights the same as the lecture example
    graph.insertEdge("A", "B", 1.0);
    graph.insertEdge("A", "M", 5.0);
    graph.insertEdge("A", "H", 7.0);

    graph.insertEdge("B", "M", 3.0);

    graph.insertEdge("M", "I", 4.0);
    graph.insertEdge("M", "E", 3.0);
    graph.insertEdge("M", "F", 4.0);

    graph.insertEdge("I", "H", 2.0);
    graph.insertEdge("I", "D", 1.0);

    graph.insertEdge("D", "F", 4.0);
    graph.insertEdge("D", "G", 2.0);
    graph.insertEdge("D", "A", 7.0);

    graph.insertEdge("F", "G", 9.0);

    graph.insertEdge("G", "L", 7.0);
    graph.insertEdge("G", "H", 9.0);
    graph.insertEdge("G", "A", 4.0);

    graph.insertEdge("H", "L", 2.0);
    graph.insertEdge("H", "B", 6.0);
    graph.insertEdge("H", "I", 2.0);

    List<String> path = graph.shortestPathData("D", "I");

    //find the correct pathway from D to I
    assertEquals(List.of("D", "G", "H", "I"), path, "Shortest path from D to I should be D, G, H, I");

    //ensure the cost is correct
    double cost = graph.shortestPathCost("D", "I");
    assertEquals(13.0, cost, "Shortest path cost from D to I should be 13");

  }

  @Test
  public void testTwo() {
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    //insert the nodes like the class example
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("M");
    graph.insertNode("I");
    graph.insertNode("E");
    graph.insertNode("D");
    graph.insertNode("F");
    graph.insertNode("G");
    graph.insertNode("H");
    graph.insertNode("L");

    // make the edge weights the same as the lecture example
    graph.insertEdge("A", "B", 1.0);
    graph.insertEdge("A", "M", 5.0);
    graph.insertEdge("A", "H", 7.0);

    graph.insertEdge("B", "M", 3.0);

    graph.insertEdge("M", "I", 4.0);
    graph.insertEdge("M", "E", 3.0);
    graph.insertEdge("M", "F", 4.0);

    graph.insertEdge("I", "H", 2.0);
    graph.insertEdge("I", "D", 1.0);

    graph.insertEdge("D", "F", 4.0);
    graph.insertEdge("D", "G", 2.0);
    graph.insertEdge("D", "A", 7.0);

    graph.insertEdge("F", "G", 9.0);

    graph.insertEdge("G", "L", 7.0);
    graph.insertEdge("G", "H", 9.0);
    graph.insertEdge("G", "A", 4.0);

    graph.insertEdge("H", "L", 2.0);
    graph.insertEdge("H", "B", 6.0);
    graph.insertEdge("H", "I", 2.0);

    List<String> path = graph.shortestPathData("D", "G");

    //pathway from D to G should just be D and G
    assertEquals(List.of("D", "G"), path, "Shortest path from D to G should be D, G");

    //check to see if the expected cost is correct
    double cost = graph.shortestPathCost("D", "G");
    assertEquals(2.0, cost,"Shortest path cost from D to G should be 2");
  }

  @Test
  public void testThree() {
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    //insert the nodes like the class example
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("M");
    graph.insertNode("I");
    graph.insertNode("E");
    graph.insertNode("D");
    graph.insertNode("F");
    graph.insertNode("G");
    graph.insertNode("H");
    graph.insertNode("L");

    // make the edge weights the same as the lecture example
    graph.insertEdge("A", "B", 1.0);
    graph.insertEdge("A", "M", 5.0);
    graph.insertEdge("A", "H", 7.0);

    graph.insertEdge("B", "M", 3.0);

    graph.insertEdge("M", "I", 4.0);
    graph.insertEdge("M", "E", 3.0);
    graph.insertEdge("M", "F", 4.0);

    graph.insertEdge("I", "H", 2.0);
    graph.insertEdge("I", "D", 1.0);

    graph.insertEdge("D", "F", 4.0);
    graph.insertEdge("D", "G", 2.0);
    graph.insertEdge("D", "A", 7.0);

    graph.insertEdge("F", "G", 9.0);

    graph.insertEdge("G", "L", 7.0);
    graph.insertEdge("G", "H", 9.0);
    graph.insertEdge("G", "A", 4.0);

    graph.insertEdge("H", "L", 2.0);
    graph.insertEdge("H", "B", 6.0);
    graph.insertEdge("H", "I", 2.0);


    //throw an exception because Z doesn't exist
    assertThrows(NoSuchElementException.class, () -> {
      graph.shortestPathData("D", "Z");
    }, "No path so should throw NoSuchElementException");
  }

  @Test
  public void testFour() {
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    //insert the nodes like the class example
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("M");
    graph.insertNode("I");
    graph.insertNode("E");
    graph.insertNode("D");
    graph.insertNode("F");
    graph.insertNode("G");
    graph.insertNode("H");
    graph.insertNode("L");

    // make the edge weights the same as the lecture example
    graph.insertEdge("A", "B", 1.0);
    graph.insertEdge("A", "M", 5.0);
    graph.insertEdge("A", "H", 7.0);

    graph.insertEdge("B", "M", 3.0);

    graph.insertEdge("M", "I", 4.0);
    graph.insertEdge("M", "E", 3.0);
    graph.insertEdge("M", "F", 4.0);

    graph.insertEdge("I", "H", 2.0);
    graph.insertEdge("I", "D", 1.0);

    graph.insertEdge("D", "F", 4.0);
    graph.insertEdge("D", "G", 2.0);
    graph.insertEdge("D", "A", 7.0);

    graph.insertEdge("F", "G", 9.0);

    graph.insertEdge("G", "L", 7.0);
    graph.insertEdge("G", "H", 9.0);
    graph.insertEdge("G", "A", 4.0);

    graph.insertEdge("H", "L", 2.0);
    graph.insertEdge("H", "B", 6.0);
    graph.insertEdge("H", "I", 2.0);

    List<String> path = graph.shortestPathData("F", "I");

    //find the path for F to I, which should be F, G, H, I
    assertEquals(List.of("F", "G", "H", "I"), path, "Shortest path from F to I should be F, G, H, I");

    // Check the cost of this path
    double cost = graph.shortestPathCost("F", "I");
    assertEquals(20.0, cost, "Shortest path cost from F to I should be 20");
  }

}
