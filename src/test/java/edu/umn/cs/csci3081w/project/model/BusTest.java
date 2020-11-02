package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class BusTest {

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
    Route testRouteOut = new Route("testRouteOut", stopsOut, distancesOut, 3, generatorOut);
    return new Bus("TestBus", testRouteOut, testRouteIn, 5, 1);
  }

  /**
   * Testing state after using constructor.
   */
  @Test
  public void testConstructorNormal() {
    Bus bus = createBus();
    assertEquals("TestBus", bus.getName());
    assertEquals("testRouteOut", bus.getOutgoingRoute().getName());
    assertEquals("testRouteIn", bus.getIncomingRoute().getName());
    assertEquals(5, bus.getCapacity());
  }

  /**
   * Testing state after using report method.
   */
  @Test
  public void testReport() {
    ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(capturedOutput);
    Bus bus = createBus();
    String expectedOutput = "####Bus Info Start####\n"
                            + "Name: TestBus\n"
                            + "Speed: 1.0\n"
                            + "Distance to next stop: 0.0\n"
                            + "****Passengers Info Start****\n"
                            + "Num of passengers: 0\n"
                            + "****Passengers Info End****\n"
                            + "####Bus Info Start####\n";
    bus.report(out);
    assertEquals(expectedOutput, capturedOutput.toString());
  }

  /**
   * Testing isTripComplete method.
   */
  @Test
  public void testIsTripComplete() {
    Bus bus = createBus();
    assertFalse(bus.isTripComplete());
  }

  /**
   * Testing loadPassenger method.
   */
  @Test
  public void testLoadPassenger() {
    Bus bus = createBus();
    assertEquals(0, bus.getNumPassengers());
    Passenger testPassenger = new Passenger(1, "testPassenger");
    bus.loadPassenger(testPassenger);
    assertEquals(1, bus.getNumPassengers());
  }

  /**
   * Testing move method.
   */
  @Test
  public void testMove() {
    Bus bus = createBus();
    assertTrue(bus.move());
  }

  /**
   * Testing update method.
   */
  @Test
  public void testUpdate() {
    Bus bus = createBus();
    bus.update();
  }

  /**
   * Testing updateBus method.
   */
  @Test
  public void testUpdateBus() {
    Bus bus = createBus();
    BusData busData = bus.getBusData();
    bus.updateBusData();
    assertEquals("TestBus", busData.getId());
    assertEquals(busData.getPosition(), bus.getBusData().getPosition());
    assertEquals(0, busData.getNumPassengers());
    assertEquals(5, busData.getCapacity());
  }


}