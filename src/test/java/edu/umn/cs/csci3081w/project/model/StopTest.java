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
import org.junit.jupiter.api.Test;

public class StopTest {

  /**
   * Setup operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Create a bus with outgoing and incoming routes and three stops per route.
   */
  public Bus createBus() {
    Stop stop1 = new Stop(0, 44.972392, -93.243774);
    Stop stop2 = new Stop(1, 44.973580, -93.235071);
    Stop stop3 = new Stop(2, 44.975392, -93.226632);
    List<Stop> stopsIn = new ArrayList<Stop>();
    stopsIn.add(stop1);
    stopsIn.add(stop2);
    stopsIn.add(stop3);
    List<Double> distancesIn = new ArrayList<Double>();
    distancesIn.add(0.008784);
    distancesIn.add(0.008631);
    List<Double> probabilitiesIn = new ArrayList<Double>();
    probabilitiesIn.add(.15);
    probabilitiesIn.add(0.3);
    probabilitiesIn.add(.025);
    PassengerGenerator generatorIn = new RandomPassengerGenerator(probabilitiesIn, stopsIn);
    Route testRouteIn = new Route("testRouteIn", stopsIn, distancesIn, 3, generatorIn);
    List<Stop> stopsOut = new ArrayList<>();
    stopsOut.add(stop3);
    stopsOut.add(stop2);
    stopsOut.add(stop1);
    List<Double> distancesOut = new ArrayList<>();
    distancesOut.add(0.008631);
    distancesOut.add(0.008784);
    List<Double> probabilitiesOut = new ArrayList<>();
    probabilitiesOut.add(.025);
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.15);
    PassengerGenerator generatorOut = new RandomPassengerGenerator(probabilitiesOut, stopsOut);
    Route testRouteOut = new Route("testRouteIn", stopsOut, distancesOut, 3, generatorOut);
    return new Bus("TestBus", testRouteOut, testRouteIn, 5, 1);
  }

  /**
   * Testing state after using constructor.
   */
  @Test
  public void testConstructorNormal() {
    Stop stop = new Stop(0, 44.972392, -93.243774);
    assertEquals(0, stop.getId());
    assertEquals(44.972392, stop.getLongitude());
    assertEquals(-93.243774, stop.getLatitude());
    assertEquals(0, stop.getNumPassengersPresent());
  }

  /**
   * Testing state of stop after adding passenger.
   */
  @Test
  public void testAddPassengers() {
    Passenger passenger = new Passenger(1, "Goldy");
    Stop stop = new Stop(0, 44.972392, -93.243774);
    int added = stop.addPassengers(passenger);
    assertEquals(1, added);
    assertEquals(1, stop.getNumPassengersPresent());
  }

  /**
   * Tests if passengers can be loaded onto an empty bus correctly from an empty stop
   * nothing should happen, loadPassengers returns 0.
   */
  @Test
  public void testLoadPassengersEmptyStopEmptyBus() {
    Bus testBus = createBus();
    int passengersLoaded = testBus.getNextStop().loadPassengers(testBus);
    assertEquals(0, passengersLoaded);
    long passengersOnBus = testBus.getNumPassengers();
    assertEquals(0, passengersOnBus);
  }

  /**
   * Tests if passengers can be loaded onto an full bus correctly from an empty stop
   * nothing should happen, loadPassengers returns 0.
   */
  @Test
  public void testLoadPassengersEmptyStopFullBus() {
    Bus testBus = createBus();
    Passenger passenger = new Passenger(1, "Goldy");
    Passenger passenger2 = new Passenger(1, "Gopher");
    Passenger passenger3 = new Passenger(1, "The Entire School");
    Passenger passenger4 = new Passenger(1, "Walked onto");
    Passenger passenger5 = new Passenger(1, "This Bus");
    testBus.loadPassenger(passenger);
    testBus.loadPassenger(passenger2);
    testBus.loadPassenger(passenger3);
    testBus.loadPassenger(passenger4);
    testBus.loadPassenger(passenger5);
    int passengersLoaded = testBus.getNextStop().loadPassengers(testBus);
    assertEquals(0, passengersLoaded);
    long passengersOnBus = testBus.getNumPassengers();
    assertEquals(5, passengersOnBus);
  }

  /**
   * Tests if passengers can be loaded onto a full bus correctly from a non-empty stop
   * nothing should happen, loadPassengers returns 0.
   */
  @Test
  public void testLoadPassengersNonEmptyStopFullBus() {
    Bus testBus = createBus();
    Passenger passenger = new Passenger(1, "Goldy");
    Passenger passenger2 = new Passenger(1, "Gopher");
    Passenger passenger3 = new Passenger(1, "The Entire School");
    Passenger passenger4 = new Passenger(1, "Walked onto");
    Passenger passenger5 = new Passenger(1, "This Bus");
    testBus.loadPassenger(passenger);
    testBus.loadPassenger(passenger2);
    testBus.loadPassenger(passenger3);
    testBus.loadPassenger(passenger4);
    testBus.loadPassenger(passenger5);
    Passenger passenger6 = new Passenger(1, "I never leave the stop");
    testBus.getNextStop().addPassengers(passenger6);
    int passengersLoaded = testBus.getNextStop().loadPassengers(testBus);
    assertEquals(0, passengersLoaded);
    long passengersOnBus = testBus.getNumPassengers();
    assertEquals(5, passengersOnBus);
  }

  /**
   * Tests if passengers can be loaded onto a bus with space correctly from a stop
   * loadPassengers returns the number of passengers at stop.
   */
  @Test
  public void testLoadPassengersNonEmptyStopEmptyBus() {
    Bus testBus = createBus();
    Passenger passenger = new Passenger(1, "I never leave the stop");
    testBus.getNextStop().addPassengers(passenger);
    long passengersOnBusStart = testBus.getNumPassengers();
    assertEquals(0, passengersOnBusStart);
    int passengersLoaded = testBus.getNextStop().loadPassengers(testBus);
    assertEquals(1, passengersLoaded);
    long passengersOnBusEnd = testBus.getNumPassengers();
    assertEquals(1, passengersOnBusEnd);
  }

  /**
   * Test simulation update on non-empty stop.
   */
  @Test
  public void testSimulationUpdateOnNonEmptyStop() {
    Stop stop = new Stop(0, 44.972392, -93.243774);
    Passenger passenger = new Passenger(1, "Goldy");
    stop.addPassengers(passenger);
    int initialWait = passenger.getTotalWait();
    assertEquals(0, initialWait);
    stop.update();
    int updatedWait = passenger.getTotalWait();
    assertEquals(1, updatedWait);
  }


  /**
   * Testing reporting functionality with no passenger.
   */
  @Test
  public void testStopReportNoPassengers() {
    try {
      Stop stop = new Stop(0, 44.972392, -93.243774);
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      stop.report(testStream);
      outputStream.flush();
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Stop Info Start####" + System.lineSeparator()
              + "ID: 0" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num passengers waiting: 0" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Stop Info End####" + System.lineSeparator();
      assertEquals(data, strToCompare);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Testing reporting functionality with passenger.
   */
  @Test
  public void testStopReportWithPassenger() {
    try {
      Stop stop = new Stop(0, 44.972392, -93.243774);
      Passenger passenger = new Passenger(1, "Goldy");
      stop.addPassengers(passenger);
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      stop.report(testStream);
      outputStream.flush();
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Stop Info Start####" + System.lineSeparator()
              + "ID: 0" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num passengers waiting: 1" + System.lineSeparator()
              + "####Passenger Info Start####" + System.lineSeparator()
              + "Name: Goldy" + System.lineSeparator()
              + "Destination: 1" + System.lineSeparator()
              + "Total wait: 0" + System.lineSeparator()
              + "Wait at stop: 0" + System.lineSeparator()
              + "Time on bus: 0" + System.lineSeparator()
              + "####Passenger Info End####" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Stop Info End####" + System.lineSeparator();
      assertEquals(data, strToCompare);
    } catch (IOException ioe) {
      fail();
    }
  }
}
