import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;

public class FrontendTests {
  /**
   * tests generateShortestPathPromptHTML() and generateTenClosestDestinationsPromptHTML()
   */
  @Test
  public void teamTest1(){
    // create a frontend instance
    Frontend tester = new Frontend(new Backend(new Graph_Placeholder()));
    // create a string to hold the output of generateShortestPathPromptHTML()
    String output = tester.generateShortestPathPromptHTML();
    // ensure the output contains all the necessary components
    assertTrue(output.contains("<input"));
    assertTrue(output.contains("type=\"text\""));
    assertTrue(output.contains("id=\"start\""));
    assertTrue(output.contains("id=\"end\""));
    assertTrue(output.contains("<button onclick=\"findShortestPath()\">Find Shortest Path</button>"));
    assertTrue(output.contains("<label for=\"start\">"));
    assertTrue(output.contains("<label for=\"end\">"));
    assertTrue(output.contains("</label>"));
    // reassign the string to the output of generateTenClosestDestinationsPromptHTML()
    output = tester.generateTenClosestDestinationsPromptHTML();
    // ensure the output contains all the necessary components
    assertTrue(output.contains("<input type=\"text\" id=\"from\""));
    assertTrue(output.contains("<button onclick=\"findTenClosestDestinations()\">Ten Closest Destinations</button>"));
    assertTrue(output.contains("<label for=\"from\">"));
    assertTrue(output.contains("</label>"));
  }

  /**
   * tests generateShortestPathResponseHTML()
   */
  @Test
  public void teamTest2(){
    // create a frontend instance
    Frontend tester = new Frontend(new Backend(new Graph_Placeholder()));
    // create a string to hold the output of generateShortestPathResponseHTML() with
    // arguments that have a path between them
    String output = tester.generateShortestPathResponseHTML("Union South", "Atmospheric, Oceanic and Space Sciences");
    // ensure the output contains all the necessary components
    assertTrue(output.contains("<p>"));
    assertTrue(output.contains("</p>"));
    assertTrue(output.contains("<ol>"));
    assertTrue(output.contains("</ol>"));
    assertTrue(output.contains("<li>Union South</li>"));
    assertTrue(output.contains("<li>Computer Sciences and Statistics</li>"));
    assertTrue(output.contains("<li>Atmospheric, Oceanic and Space Sciences</li>"));
    System.out.println(output);
    //assertTrue(output.contains("6.0"));
    // reassign the output string to hold the output of generateShortestPathResponseHTML()
    // with arguments that do not have a path between them
    output = tester.generateShortestPathResponseHTML("Memorial Union", "Nicholas Recreation Center");
    // ensure the output contains all the necessary components
    assertTrue(output.contains("<p>"));
    assertTrue(output.contains("</p>"));
  }

  /**
   * tests generateTenClosestDestinationsResponseHTML()
   */
  @Test
  public void teamTest3(){
    // create a frontend instance
    Frontend tester = new Frontend(new Backend(new Graph_Placeholder()));
    // create a string to hold the output of generateTenClosestDestinationsResponseHTML() with
    // an argument that has outgoing edges
    String output = tester.generateTenClosestDestinationsResponseHTML("Union South");
    // ensure the output contains all the necessary components
    assertTrue(output.contains("<p>"));
    assertTrue(output.contains("</p>"));
    assertTrue(output.contains("<ul>"));
    assertTrue(output.contains("</ul>"));
    assertTrue(output.contains("<li>Union South</li>"));
    assertTrue(output.contains("<li>Computer Sciences and Statistics</li>"));
    assertTrue(output.contains("<li>Atmospheric, Oceanic and Space Sciences</li>"));
    // reassign the output string to hold the output of generateTenClosestDestinationsResponseHTML()
    // with an argument that does not have outgoing edges
    output = tester.generateTenClosestDestinationsResponseHTML("Memorial Union");
    // ensure the output contains all the necessary components
    assertTrue(output.contains("<p>"));
    assertTrue(output.contains("</p>"));
  }


  @Test
  public void roleTest1(){
    Frontend tester = new Frontend(new Backend(new DijkstraGraph<>()));
      //Create a new string that holds the value of frontend generating a prompt for generateShortestPathPromptHTML
      String testShortestPath = tester.generateShortestPathPromptHTML();
      //Check to see that key features like an ask to start, end, and "Find Shortest Path" are written
      assertTrue(testShortestPath.contains("id=\"start\""));
      assertTrue(testShortestPath.contains("id=\"end\""));
      assertTrue(testShortestPath.contains("Find Shortest Path"));

  }

  @Test
  public void roleTest2(){
    //First, setup the graph, backend, and frontend instances to be tested
    Graph_Placeholder testGraph = new Graph_Placeholder();
    Backend backend = new Backend(testGraph);
    Frontend testFrontend = new Frontend(backend);
    //Create a new string that holds the value of frontend generating a response for generateShortestPathResponseHTML
    String responseHTML = testFrontend.generateShortestPathResponseHTML("LocationA", "LocationC");
    //Check to see that a path can't be found due to the constraints and an error message pops
    assertTrue(responseHTML.contains("No path found between LocationA and LocationC"));
  }

  @Test
  public void roleTest3(){
    //First, setup the graph, backend, and frontend instances to be tested
    Graph_Placeholder testGraph = new Graph_Placeholder();
    Backend backend = new Backend(testGraph);
    Frontend testFrontend = new Frontend(backend);
    //Create a new string that holds the value of frontend generating a prompt for generateTenClosestDestinationsPromptHTML
    String promptHTML = testFrontend.generateTenClosestDestinationsPromptHTML();
    //Check to see if the prompt asks from and Ten Closest Destinations, both necessary for the test
    assertTrue(promptHTML.contains("id=\"from\""));
    assertTrue(promptHTML.contains("Ten Closest Destinations"));
    //Create a new string that holds the value of frontend generating a response for generateTenClosestDestinationsResponseHTML
    String responseHTML = testFrontend.generateTenClosestDestinationsResponseHTML("LocationA");

  }

  /**
   * Integration Test 1: Verifies that the frontend can generate a correct HTML response for the shortest path
   * using real backend data.
   */
  @Test
  public void testGenerateShortestPathIntegration() {

    // Create backend and frontend instances
    Backend backend = new Backend(new DijkstraGraph<>());
    Frontend frontend = new Frontend(backend);

    // Generate the shortest path response HTML for two valid locations
    List<String> backendLocations = backend.findLocationsOnShortestPath("Union South", "Memorial Union");

    // Assert that the HTML contains the correct elements
    System.out.println(backendLocations);
    List<String> frontendLocations = backend.findLocationsOnShortestPath("Union South", "Memorial Union");
    System.out.println(frontendLocations);
    assertTrue(backendLocations.contains("<p>Shortest path from Union South to Atmospheric, Oceanic and Space Sciences:</p>"));
    assertTrue(backendLocations.contains("<ol>"));
    assertTrue(backendLocations.contains("</ol>"));
    assertTrue(backendLocations.contains("<li>Union South</li>"));
    assertTrue(backendLocations.contains("<li>Computer Sciences and Statistics</li>"));
    assertTrue(backendLocations.contains("<li>Atmospheric, Oceanic and Space Sciences</li>"));
    assertTrue(backendLocations.contains("<p>Total travel time: "), "Expected total travel time missing.");
  }
  /**
   * Integration Test 4: Checks to see how the findTenClosestDestinations method works.
   */
  @Test
  public void testGenerateTenClosestDestinationsIntegration() {
    // First, creates backend and frontend instances
    Backend backend = new Backend(new Graph_Placeholder());
    Frontend frontend = new Frontend(backend);
    //ensures that the prompt is correctly generated
    String html = frontend.generateTenClosestDestinationsPromptHTML();
    //checks for prompt correctness
    assertTrue(html.contains("findTenClosestDestinations()"));
    // Generate the ten closest destinations response HTML
    List<String> locations = backend.findLocationsOnShortestPath("Union South", "Memorial Union");
    // Assert the List has some of the right elements
    assertTrue(locations.contains("Computer Sciences and Statistics"));
    assertTrue(locations.contains("Atmospheric, Oceanic and Space Sciences"));
  }
  /**
   * Integration Test 4: Find if the integration tests can find the close locations in the path.
   */
  @Test
  public void integrationFindCloseLocations() {
    // First, creates backend and frontend instances
    Backend backend = new Backend(new Graph_Placeholder());
    Frontend frontend = new Frontend(backend);
    //ensures that the prompt is correctly generated
    String html = frontend.generateShortestPathResponseHTML("Computer Sciences and Statistics","Union South");
    //checks for prompt correctness
    assertTrue(html.contains("Shortest path from Computer Sciences and Statistics to Union South:"));
    // Generate the locations in the shortest path response HTML
    List<String> locations = backend.findLocationsOnShortestPath("Computer Sciences and Statistics","Union South");
    // Assert the List has some of the right elements
    assertTrue(locations.contains("Computer Sciences and Statistics"));
    assertTrue(locations.contains("Atmospheric, Oceanic and Space Sciences"));
  }

  /**
   * Integration Test 2: Tests the backend and frontend integration to ensure that the closest ten
   * destinations are found.
   */
  @Test
  public void testGetClosestDestinations() {
    // Create backend and frontend instances
    Backend backend = new Backend(new Graph_Placeholder());
    Frontend frontend = new Frontend(backend);
    // Get the list of all locations through the backend
    List<String> locations = backend.getTenClosestDestinations("Union South");
    //get it through frontend as well
    String html = frontend.generateTenClosestDestinationsResponseHTML("Union South");
    assertTrue(html.contains("<p>The ten closest destinations are from Union South:</p>"));
    // Assert that the backend provides one of the correct locations in the list
    assertTrue(locations.contains("Computer Sciences and Statistics"));
  }

  /**
   * Integration Test 1: Sees what happens when there is no path between two places.
   */
  @Test
  public void integrationTestNoPathFound() {
    //First, create frontend and backend instances
    Backend backend = new Backend(new DijkstraGraph<>());
    Frontend frontend = new Frontend(backend);

    //Then get the HTML for the location between the instances
    String html = frontend.generateShortestPathResponseHTML("San Francisco", "Green Bay");
    //Get the list of values from backend
    List<String> shortestPath = backend.findLocationsOnShortestPath("San Francisco", "Green Bay");
    System.out.println(shortestPath);
    //There should be no values within the code
    assertTrue(shortestPath.isEmpty());
    // Assert that the HTML prints the right error message.
    assertTrue(html.contains("No path found between San Francisco and Green Bay."),
        "There should be no path between these locations.");
  }

  /**
   * Tests the generateShortestPathResultHTML method to ensure it returns
   * and HTML response with a valid path and total travel time.
   * This checks for correct formatting and calculations in the response.
   */
  @Test
  public void testGenerateShortestPathResponseHTML(){
    //create a backend implementation
    Backend backend = new Backend(new Graph_Placeholder());
    //create a frontend implementation
    Frontend frontend = new Frontend(backend);

    //generate the shortest path response HTML
    String html = frontend.generateShortestPathResponseHTML("Union South", "Atmospheric, Oceanic and Space Sciences");

    //check that the HTML contains the expected elements
    assertTrue(html.contains("<p>Shortest path from Union South to Atmospheric, Oceanic and Space Sciences:</p>"),
        "Expected path description missing.");
    //checking that it contains an ordered list
    assertTrue(html.contains("<ol>"), "Expected ordered list missing.");
    //checking that it contains the total travel time
    assertTrue(html.contains("<p>Total travel time: "), "Expected total travel time missing.");
  }

}
