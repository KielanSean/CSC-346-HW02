import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by kiwis on 2/7/2018.
 */
class MainTest {
    City stJoe = new City("Saint Joseph", "MO", "64505", 39.77, -94.85, 12330, 5298);
    City shef = new City("Sheffield", "IA", "50475", 42.89, -93.21, 1559, 650);
    City den = new City("Denver", "CO", "80123", 39.61, -105.07, 41552, 16936);
    City who = new City("H", "aks", "12345", 0, 0, 15, 15);


    @Test
    void calcDistance() {
        assertEquals(0, Main.calcDistance(stJoe.getLat(), stJoe.getLat(), stJoe.getLon(), stJoe.getLon()));
        assertEquals(231.7, Main.calcDistance(stJoe.getLat(), shef.getLat(), stJoe.getLon(),shef.getLon()));
        assertEquals(6380.9, Main.calcDistance(who.getLat(), shef.getLat(), who.getLon(),shef.getLon()));
        assertEquals(655.7, Main.calcDistance(den.getLat(), shef.getLat(), den.getLon(), shef.getLon()));
    }

}