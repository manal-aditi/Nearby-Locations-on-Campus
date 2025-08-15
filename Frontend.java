import java.util.List;
public class Frontend implements FrontendInterface {

  public final Backend backend;

  /**
   * Implementing classes should support the constructor below.
   * @param backend is used for shortest path computations
   */
  public Frontend(BackendInterface backend){
    this.backend = (Backend) backend;
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a larger html page.  This HTML
   * output should include: - a text input field with the id="start", for the start location - a
   * text input field with the id="end", for the destination - a button labelled "Find Shortest
   * Path" to request this computation Ensure that these text fields are clearly labelled, so that
   * the user can understand how to use them.
   *
   * @return an HTML string that contains input controls that the user can make use of to request a
   * shortest path computation
   */
  @Override
  public String generateShortestPathPromptHTML() {
    return "<div>" +
        "<label for=\"start\">Start Location:</label>" +
        "<input type=\"text\" id=\"start\" name=\"start\">" +
        "<label for=\"end\">Destination:</label>" +
        "<input type=\"text\" id=\"end\" name=\"end\">" +
        "<button onclick=\"findShortestPath()\">Find Shortest Path</button>" +
        "</div>";
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a larger html page.  This HTML
   * output should include: - a paragraph (p) that describes the path's start and end locations - an
   * ordered list (ol) of locations along that shortest path - a paragraph (p) that includes the
   * total travel time along this path Or if there is no such path, the HTML returned should instead
   * indicate the kind of problem encountered.
   *
   * @param start is the starting location to find a shortest path from
   * @param end   is the destination that this shortest path should end at
   * @return an HTML string that describes the shortest path between these two locations
   */
  @Override
  public String generateShortestPathResponseHTML(String start, String end) {
    try {
      //find path and the travel times from Backend
      List<String> path = backend.findLocationsOnShortestPath(start, end);
      List<Double> travelTimes = backend.findTimesOnShortestPath(start, end);

      // check if they are empty
      if (travelTimes.isEmpty() || path.isEmpty()) {
        return "<p>No path found between " + start + " and " + end + ".</p>";
      }

      // create a string to concatenate the info together for the shortest paths
      String shortestPathString = String.format("<p>Shortest path from %s to %s:</p>", start, end);
      shortestPathString += "<ol>";

      // add the locations in the list
      for (String location : path) {
        shortestPathString += String.format("<li>%s</li>", location);
      }
      shortestPathString += "</ol>";

      // find out the total travel time and concatenate it
      double totalTime = travelTimes.get(travelTimes.size() - 1);
      shortestPathString += String.format("<p>Total travel time: %.2f units.</p>", totalTime);

      // return the string
      return shortestPathString;
    } catch (Exception e) {
      return "<p>ERROR: Couldn't find the shortest path between " + start + " and " + end + ". " + e.getMessage() + "</p>";
    }
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a larger html page.  This HTML
   * output should include: - a text input field with the id="from", for the start location - a
   * button labelled "Ten Closest Destinations" to submit this request Ensure that this text field
   * is clearly labelled, so that the user can understand how to use it.
   *
   * @return an HTML string that contains input controls that the user can make use of to request a
   * ten closest destinations calculation
   */
  @Override
  public String generateTenClosestDestinationsPromptHTML() {
    return "<div>" +
        "<label for=\"from\">Starting Location:</label>" +
        "<input type=\"text\" id=\"from\" name=\"from\">" +
        "<button onclick=\"findTenClosestDestinations()\">Ten Closest Destinations</button>" +
        "</div>";
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a larger html page.  This HTML
   * output should include: - a paragraph (p) that describes the start location that travel time to
   * the closest destinations are being measured from - an unordered list (ul) of the ten locations
   * that are closest to start Or if no such destinations can be found, the HTML returned should
   * instead indicate the kind of problem encountered.
   *
   * @param start is the starting location to find close destinations from
   * @return an HTML string that describes the closest destinations from the specified start
   * location.
   */
  @Override
  public String generateTenClosestDestinationsResponseHTML(String start) {
    try {
      // find the ten closest destinations from backend
      List<String> closestDestinations = backend.getTenClosestDestinations(start);

      // check if the list is empty
      if (closestDestinations.isEmpty()) {
        return "<p>There are no close destinations from " + start + ".</p>";
      }

      // create a new string that we will later add the destinations to
      String closestDest = String.format("<p>The ten closest destinations are from %s:</p>", start);
      closestDest += "<ul>";

      // add the destinations
      for (String location : closestDestinations) {
        closestDest += String.format("<li>%s</li>", location);
      }
      closestDest += "</ul>";

      // return the string
      return closestDest;

    } catch (Exception e) {
      //if there's an exception, then the closest destination can't be found
      return "<p>ERROR: Couldn't find the closest destination from" + start + ". " + e.getMessage() + "</p>";
    }
  }
}
