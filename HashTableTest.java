import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
/** 
 * hand written JUnit 5 test suite for the record hash table 
 * checks basic function like get, set etc
 */
public class HashTableTest {

    /**
     * checks that the same title always gives the same hash
     * this is important because the hash function should be the same for the same titles
    */
    @Test
    public void testHashConsistent() {
        RecordHashTable table = new RecordHashTable(20);

        Record r1 = new Record("Pop", 1992, "U2", "One");
        Record r2 = new Record("Rock", 1989, "Metallica", "One");

        assertEquals(table.hashFunction(r1), table.hashFunction(r2));
    }

    /**
     * checks that this very long title is still with in the bounds 
     * which is important as long titles should not go out of array bounds
     */
    @Test
    public void testHashLongTitle() {
        RecordHashTable table = new RecordHashTable(50);

        Record r = new Record("Pop", 1, "A", "A very very very very long title");

        int hash = table.hashFunction(r);

        assertTrue(hash >= 0);
        assertTrue(hash < 50);
    }

    /**
     * Tests if put increases the counter correctly
     * this is important to check that the put function acurattly increases
     * the table size
     */
    @Test
    public void testPutIncreasesSize() {
        RecordHashTable table = new RecordHashTable(5);
        Record r = new Record("Rock", 1997, "Radiohead", "OK Computer");

        table.put(r);

        assertEquals(1, table.size());
    }

    /**
     * Tests collision handleing in the put function
     * checks that put handles collisions correctly
     */
    @Test
    public void testPutCollision(){
        RecordHashTable table = new RecordHashTable(2);

        Record r1 = new Record("Pop", 2020, "Dua Lipa", "Future Nostalgia");
        Record r2 = new Record("Rock", 1991, "Nirvana", "Nevermind");

        assertEquals(table.hashFunction(r1), table.hashFunction(r2));//check that they collide

        table.put(r1);
        table.put(r2);

        assertNotNull(table.get("Future Nostalgia"));
        assertNotNull(table.get("Nevermind"));
    }

    /**
     * Tests get if an item is non existent
     * I did this one because get should return null if the record doesnt exist
     * It should not crash
     */
    @Test
    public void testGetNotFound(){
        RecordHashTable table = new RecordHashTable(5);

        assertNull(table.get("Null"));
    }

    /**
     * tests getting mulltiple values from the table
     * this is important that is doesnt have issues
     * with multiple records in the table
     * Records shouldent affect each other for get
     */
    @Test
    public void testGetMultiple() {
        RecordHashTable table = new RecordHashTable(5);

        Record r1 = new Record("Pop", 2020, "Dua Lipa", "Future Nostalgia");
        Record r2 = new Record("Rock", 1997, "Radiohead", "OK Computer");

        table.put(r1);
        table.put(r2);

        assertEquals(r2, table.get("OK Computer"));
    }

    /**
     *tests removeing records from the table
     * it important to check that remove actually works
     */
    @Test
    public void testRemoveExisting() {
        RecordHashTable table = new RecordHashTable(5);

        Record r = new Record("Pop", 2020, "Dua Lipa", "Future Nostalgia");

        table.put(r);
        table.remove(r);

        assertNull(table.get("Future Nostalgia"));
    }

    /**
     * removing something that doesnt exist 
     * should not effect the table or the size
    */
    @Test
    public void testRemoveNonexistent() {
        RecordHashTable table = new RecordHashTable(5);

        Record existing = new Record("Pop", 2020, "Dua Lipa", "Future Nostalgia");
        Record notInTable = new Record("Rock", 1997, "Radiohead", "OK Computer");

        table.put(existing);
        table.remove(notInTable);  

        assertEquals(1, table.size());  
        assertNotNull(table.get("Future Nostalgia")); 
    }

    /**
     * test contains function if the record exists
     * its important to check if the basics of this function actually work
     */
    @Test
    public void testContainsTrue() {
        RecordHashTable table = new RecordHashTable(5);

        Record r = new Record("Rock", 1997, "Radiohead", "OK Computer");

        table.put(r);

        assertTrue(table.contains(r));
    }

    /**
     * test contains function if the record does not exists
     * important that if record doesnt exist it returns false
     */
    @Test
    public void testContainsFalse() {
        RecordHashTable table = new RecordHashTable(5);

        Record r = new Record("Rock", 1997, "Radiohead", "OK Computer");

        assertFalse(table.contains(r));
    }

    /**
     * important to test that tables size function acuratly returns size
     * 
     */
    @Test
    public void testSizeIncrease() {
        RecordHashTable table = new RecordHashTable(5);

        table.put(new Record("Pop", 2020, "Dua Lipa", "Future Nostalgia"));

        assertEquals(1, table.size());
    }

    /**
     * impostant to test that size function goes down when a record is removed
     */
    @Test
    public void testSizeDecreause() {
        RecordHashTable table = new RecordHashTable(5);

        table.put(new Record("Pop", 2020, "Dua Lipa", "Future Nostalgia"));

        assertEquals(1, table.size());


        table.remove(new Record("Pop", 2020, "Dua Lipa", "Future Nostalgia"));

        assertEquals(0, table.size());
    }

    /**
     * tests if is empty works correctly
     * its important to check that this function works correctly when size is 0
     */
    @Test
    public void testIsEmpty() {
        RecordHashTable table = new RecordHashTable(5);

        assertTrue(table.isEmpty());

        table.put(new Record("Pop", 2020, "Dua Lipa", "Future Nostalgia"));

        assertFalse(table.isEmpty());
    }

    /**
     * tests if is empty works correctly if a record is added and removed
     * important to check if the sizes are acuratly added and subtracted
     */
    @Test
    public void testIsEmptyAddAndRemove() {
        RecordHashTable table = new RecordHashTable(5);

        assertTrue(table.isEmpty());

        table.put(new Record("Pop", 2020, "Dua Lipa", "Future Nostalgia"));

        assertFalse(table.isEmpty());

        table.remove(new Record("Pop", 2020, "Dua Lipa", "Future Nostalgia"));

        assertTrue(table.isEmpty());

    }

    /**
     * tests if the rehash function correctly transfers
     * the old records to the new table
     * important to check that it rehashs 
     * when the load factor gets above 80%
     */
    @Test
    public void testRehashTriggered(){
        RecordHashTable table = new RecordHashTable(2);

        table.put(new Record("Pop", 2020,"Dua Lipa", "Future Nostalgia"));
        table.put(new Record("Rock", 1997, "Radiohead", "OK Computer"));

        assertNotNull(table.get("Future Nostalgia"));
        assertNotNull(table.get("OK Computer"));
    }

    /**
     * Tests if data size stays correct after rehash
     * its important to Check that data size is not corrupted during a rehash
     */
    @Test
    public void testRehashSizeCorrect() {
        RecordHashTable table = new RecordHashTable(2);

        table.put(new Record("Pop", 2020,"Dua Lipa", "Future Nostalgia"));
        table.put(new Record("Rock", 1997, "Radiohead", "OK Computer"));

        assertEquals(2, table.size());
    }

    /**
    * checks that dump prints null for empty spaces
    * important to check that it works for an empty table
     */
    @Test
    public void testDumpEmptyTable() {
       RecordHashTable table = new RecordHashTable(3);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        table.dump();

        String result = output.toString();

        assertTrue(result.contains("0: null"));
        assertTrue(result.contains("1: null"));
        assertTrue(result.contains("2: null"));
    }

    /** 
     * checks that dump prints record information
     * Its important to check that dump prints out all infomation accuratlly
     */
    @Test
    public void testDumpWithRecord(){
        RecordHashTable table = new RecordHashTable(5);

        Record r = new Record("Pop", 2020, "Dua Lipa","Future Nostalgia");

        table.put(r);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        table.dump();
        String result = output.toString();

        assertTrue(result.contains("Future Nostalgia"));
        assertTrue(result.contains("Pop"));
        assertTrue(result.contains("2020"));
        assertTrue(result.contains("Dua Lipa"));
    }
}
