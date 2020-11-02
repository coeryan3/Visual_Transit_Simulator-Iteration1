package edu.umn.cs.csci3081w.project.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PassengerTest {

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
     * Testing state after using constructor.
     */
    @Test
    public void testConstructorNormal(){
        Passenger passenger = new Passenger(1, "Goldy");
        assertEquals(1, passenger.getDestination());
        assertEquals(0, passenger.getTotalWait());
        assertEquals(false, passenger.isOnBus());
    }

    /**
     * Testing if time on the bus increases.
     */
    @Test
    public void testPasUpdateOnBus(){
        Passenger passenger = new Passenger(1, "Goldy");
        passenger.getOnBus();
        passenger.pasUpdate();
        assertEquals(2,passenger.getTotalWait());
    }

    /**
     * Testing if time at stop increases.
     */
    @Test
    public void testPasUpdateNotOnBus(){
        Passenger passenger = new Passenger(1, "Goldy");
        passenger.pasUpdate();
        assertEquals(1,passenger.getTotalWait());
    }

    /**
     * Testing if passenger gets on the bus.
     */
    @Test
    public void testGetOnBus(){
        Passenger passenger = new Passenger(1, "Goldy");
        passenger.getOnBus();
        assertTrue(passenger.isOnBus());
    }

    /**
     * Testing reporting functionality for passenger.
     */
    @Test
    public void testReport(){
        try {
            Passenger passenger = new Passenger(1, "Goldy");
            final Charset charset = StandardCharsets.UTF_8;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream testStream = new PrintStream(outputStream, true, charset.name());
            passenger.report(testStream);
            outputStream.flush();
            String data = new String(outputStream.toByteArray(), charset);
            testStream.close();
            outputStream.close();
            String strToCompare =
                    "####Passenger Info Start####" + System.lineSeparator()
                            + "Name: Goldy" + System.lineSeparator()
                            + "Destination: 1" + System.lineSeparator()
                            + "Total wait: 0" + System.lineSeparator()
                            + "Wait at stop: 0" + System.lineSeparator()
                            + "Time on bus: 0" + System.lineSeparator()
                            + "####Passenger Info End####" + System.lineSeparator();
            assertEquals(data, strToCompare);
        } catch (IOException ioe) {
            fail();
        }

    }
}
