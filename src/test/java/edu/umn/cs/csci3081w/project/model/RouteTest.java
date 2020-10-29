package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class RouteTest {

  private Route testRoute;
  private List<Stop> stops;
  private List<Double> distances;
  private List<Double> probabilities;
  private PassengerGenerator generator;

  /**
   * Setup operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
    stops = createStopList();
    distances = createDistanceList();
    probabilities = createProbabilitiesList();
    generator = new RandomPassengerGenerator(probabilities, stops);
    testRoute = new Route("testRoute", stops, distances, 3, generator);
  }

  @AfterEach
  public void tearDown() {
    stops = null;
    distances = null;
    probabilities = null;
    generator = null;
    testRoute = null;
  }
  /**
   * Create a list of three stops to use when constructing Routes.
   */
  public List<Stop> createStopList() {
    Stop stop1 = new Stop(0, 44.972392, -93.243774);
    Stop stop2 = new Stop(1, 44.973580, -93.235071);
    Stop stop3 = new Stop(2, 44.975392, -93.226632);
    List<Stop> stops = new ArrayList<Stop>();
    stops.add(stop1);
    stops.add(stop2);
    stops.add(stop3);
    return stops;
  }

  /**
   * Create a list of distances between the three stops used to construct Routes.
   */
  public List<Double> createDistanceList() {
    List<Double> distances = new ArrayList<Double>();
    distances.add(0.008784);
    distances.add(0.008631);
    return distances;
  }

  /**
   * Create a list of probabilities of the three stops used to construct Routes.
   */
  public List<Double> createProbabilitiesList() {
    List<Double> probabilities = new ArrayList<Double>();
    probabilities.add(.15);
    probabilities.add(0.3);
    probabilities.add(.025);
    return probabilities;
  }

  /**
   * Testing state after using constructor.
   */
  @Test
  public void testConstructorNormal() {
    assertEquals("testRoute", testRoute.getName());
    assertEquals(0, testRoute.getDestinationStopIndex());
    assertEquals(stops.get(0), testRoute.getDestinationStop());
    for (int i = 0; i < stops.size(); i++) {
      assertEquals(stops.get(i), testRoute.getStops().get(i));
    }
  }

  /**
   * Testing if shallow copy method makes a copy correctly.
   */
  @Test
  public void testShallowCopy() {
    Route testRouteCopy = testRoute.shallowCopy();
    assertEquals(testRoute.getName(), testRouteCopy.getName());
    assertEquals(testRoute.getDestinationStopIndex(), testRouteCopy.getDestinationStopIndex());
    assertEquals(testRoute.getDestinationStop(), testRouteCopy.getDestinationStop());
    for (int i = 0; i < stops.size(); i++) {
      assertEquals(testRoute.getStops().get(i), testRouteCopy.getStops().get(i));
    }
  }

  /**
   * Test that the next stop is being set correctly for each stop on the Route.
   * Also ensure that any extraneous calls to nextStop() are handled by setting
   * the destination stop and destination stop index to the last stop in the list.
   */
  @Test
  public void testNextStop() {
    assertEquals(0, testRoute.getDestinationStopIndex());
    assertEquals(stops.get(0), testRoute.getDestinationStop());
    testRoute.nextStop();
    assertEquals(1, testRoute.getDestinationStopIndex());
    assertEquals(stops.get(1), testRoute.getDestinationStop());
    testRoute.nextStop();
    assertEquals(2, testRoute.getDestinationStopIndex());
    assertEquals(stops.get(2), testRoute.getDestinationStop());
    testRoute.nextStop();
    assertEquals(3, testRoute.getDestinationStopIndex());
    assertEquals(stops.get(2), testRoute.getDestinationStop());
    testRoute.nextStop();
    assertEquals(4, testRoute.getDestinationStopIndex());
    assertEquals(stops.get(2), testRoute.getDestinationStop());
  }

  /**
   * Check if prevStop() returns the correct stop. If the route is at the first stop,
   * then prevStop() will return the first stop. Once the route has reached the final
   * stop (where the destination stop index exceeds the number of stops,
   * prevStop() will return the final stop.
   */
  @Test
  public void testPrevStop() {
    assertEquals(stops.get(0), testRoute.prevStop());
    testRoute.nextStop();
    assertEquals(stops.get(0), testRoute.prevStop());
    testRoute.nextStop();
    assertEquals(stops.get(1), testRoute.prevStop());
    testRoute.nextStop();
    assertEquals(stops.get(2), testRoute.prevStop());
    testRoute.nextStop();
    assertEquals(stops.get(2), testRoute.prevStop());
  }

  /**
   * Tests if Route can correctly identify it has reached the end of the Route.
   */
  @Test
  public void testIsAtEnd() {
    assertEquals(false, testRoute.isAtEnd());
    testRoute.nextStop();
    assertEquals(false, testRoute.isAtEnd());
    testRoute.nextStop();
    assertEquals(false, testRoute.isAtEnd());
    testRoute.nextStop();
    assertEquals(true, testRoute.isAtEnd());
    testRoute.nextStop();
  }


}
