import org.junit.jupiter.api.Test;
import org.junit.jupiter.engine.config.DefaultJupiterConfiguration;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TeamTests {
  /**
   * This test method will test the loadGraphData() method as well as if getListOfAllLocations()
   * returns the necessary locations.
   */
  @Test
  public void testLoadGraphData() {
    try {
      //creates the graph placeholder and test backend
      Graph_Placeholder testGraph = new Graph_Placeholder();
      Backend backend = new Backend(testGraph);
      // takes in the data from the file
      backend.loadGraphData("src/campus.dot");
      //tests the getListOfAllLocations method
      List<String> locations = backend.getListOfAllLocations();
      System.out.println(locations);
      //checks to make sure all four of the locations are returned by the method
      assertTrue(locations.contains("Union South"));
      assertTrue(locations.contains("Computer Sciences and Statistics"));
      assertTrue(locations.contains("Atmospheric, Oceanic and Space Sciences"));
      assertTrue(locations.contains("Memorial Union"));
      assertEquals(4, locations.size());
    } catch (IOException e) {
      //should be able to load in the file
      fail("FAIL: " + e.getMessage());
    }
  }
  /**
   * This test method will test the findLocationsOnShortestPath() method as well as findTimesOnShortestPath().
   * The locations should be returned as well as the double values for the walk times.
   */
  @Test
  public void testFindLocationsOnShortestPathAndTimes() {
    //creates the graph placeholder and test backend
    Graph_Placeholder testGraph = new Graph_Placeholder();
    Backend backend = new Backend(testGraph);

    // write in the start and end locations
    String startLocation = "Union South";
    String endLocation = "Atmospheric, Oceanic and Space Sciences";

    // get the values of the shortest path
    List<String> shortestPath = backend.findLocationsOnShortestPath(startLocation, endLocation);

    //ensure that the shortest path is printed with the given info
    assertEquals(3, shortestPath.size());
    assertEquals("Union South", shortestPath.get(0));
    assertEquals("Computer Sciences and Statistics", shortestPath.get(1));
    assertEquals("Atmospheric, Oceanic and Space Sciences", shortestPath.get(2));
    //see if the findTimesOnShortestPath returns the correct walking times
    List<Double> testTime = backend.findTimesOnShortestPath(startLocation, endLocation);
    assertEquals(1.0, testTime.get(0));
    assertEquals(2.0, testTime.get(1));
  }

  /**
   * This test method will test getTenClosestDestinations() to make sure it returns the 10 closest
   * locations.
   */
@Test
  public void testGetTenClosestDestinations() {
    //creates the graph placeholder and test backend
    Graph_Placeholder testGraph = new Graph_Placeholder();
    Backend backend = new Backend(testGraph);

    //create the starting location
    String startLocation = "Computer Sciences and Statistics";

    //find the ten closest locations
    List<String> closestLocations = backend.getTenClosestDestinations(startLocation);
    //check that the correct locations are inside
    assertTrue(closestLocations.contains("Union South"));
    assertTrue(closestLocations.contains("Atmospheric, Oceanic and Space Sciences"));

    //ensure the list is smaller than 10
    assertTrue(closestLocations.size() <= 10);
  }

}
