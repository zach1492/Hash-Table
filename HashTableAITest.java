import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive JUnit 5 test suite for RecordHashTable.
 * Covers edge cases, collision handling, rehashing, tombstone behaviour,
 * and realistic music-record data.
 */
@DisplayName("RecordHashTable — AI Test Suite")
class HashTableAITest {

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    /** Convenience factory so tests stay readable. */
    private static Record rec(String title, int year, String artist, String genre) {
        return new Record(genre, year, artist, title);
    }

    // A small set of realistic records reused across tests
    private static final Record BOHEMIAN   = rec("Bohemian Rhapsody",    1975, "Queen",            "Rock");
    private static final Record THRILLER   = rec("Thriller",             1982, "Michael Jackson",  "Pop");
    private static final Record HOTEL_CAL  = rec("Hotel California",     1977, "Eagles",            "Rock");
    private static final Record IMAGINE    = rec("Imagine",              1971, "John Lennon",       "Pop");
    private static final Record STAIRWAY   = rec("Stairway to Heaven",   1971, "Led Zeppelin",      "Rock");
    private static final Record PURPLE_H   = rec("Purple Haze",          1967, "Jimi Hendrix",      "Rock");
    private static final Record SMELLS_LIKE = rec("Smells Like Teen Spirit", 1991, "Nirvana",       "Grunge");
    private static final Record BITTER_SWEET = rec("Bitter Sweet Symphony", 1997, "The Verve",     "Britpop");
    private static final Record GOOD_TIMES  = rec("Good Times Bad Times",  1969, "Led Zeppelin",   "Rock");
    private static final Record TINY_DANCER = rec("Tiny Dancer",           1971, "Elton John",     "Pop");

    // -----------------------------------------------------------------------
    // Construction & empty-state
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("Construction & empty state")
    class Construction {

        @Test
        @DisplayName("Newly created table is empty")
        void newTableIsEmpty() {
            RecordHashTable ht = new RecordHashTable(16);
            assertTrue(ht.isEmpty());
        }

        @Test
        @DisplayName("Newly created table has size 0")
        void newTableHasSizeZero() {
            RecordHashTable ht = new RecordHashTable(16);
            assertEquals(0, ht.size());
        }

        @Test
        @DisplayName("get() on empty table returns null")
        void getOnEmptyTableReturnsNull() {
            RecordHashTable ht = new RecordHashTable(16);
            assertNull(ht.get("Bohemian Rhapsody"));
        }

        @Test
        @DisplayName("contains() on empty table returns false")
        void containsOnEmptyTableReturnsFalse() {
            RecordHashTable ht = new RecordHashTable(16);
            assertFalse(ht.contains(BOHEMIAN));
        }
    }

    // -----------------------------------------------------------------------
    // Basic put / get / contains
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("put / get / contains — basic behaviour")
    class BasicPutGet {

        private RecordHashTable ht;

        @BeforeEach
        void setup() {
            ht = new RecordHashTable(16);
        }

        @Test
        @DisplayName("put then get returns correct record")
        void putThenGet() {
            ht.put(BOHEMIAN);
            Record found = ht.get("Bohemian Rhapsody");
            assertNotNull(found);
            assertEquals("Bohemian Rhapsody", found.getTitle());
        }

        @Test
        @DisplayName("contains returns true after put")
        void containsAfterPut() {
            ht.put(THRILLER);
            assertTrue(ht.contains(THRILLER));
        }

        @Test
        @DisplayName("size increments correctly after multiple puts")
        void sizeAfterMultiplePuts() {
            ht.put(BOHEMIAN);
            ht.put(THRILLER);
            ht.put(HOTEL_CAL);
            assertEquals(3, ht.size());
        }

        @Test
        @DisplayName("table is no longer empty after a put")
        void notEmptyAfterPut() {
            ht.put(IMAGINE);
            assertFalse(ht.isEmpty());
        }

        @Test
        @DisplayName("get returns null for a title not in the table")
        void getUnknownTitleReturnsNull() {
            ht.put(STAIRWAY);
            assertNull(ht.get("Purple Haze"));
        }

        @Test
        @DisplayName("Multiple records can be retrieved independently")
        void multipleIndependentRecords() {
            ht.put(BOHEMIAN);
            ht.put(THRILLER);
            ht.put(HOTEL_CAL);
            ht.put(IMAGINE);

            assertEquals("Bohemian Rhapsody",  ht.get("Bohemian Rhapsody").getTitle());
            assertEquals("Thriller",            ht.get("Thriller").getTitle());
            assertEquals("Hotel California",    ht.get("Hotel California").getTitle());
            assertEquals("Imagine",             ht.get("Imagine").getTitle());
        }
    }

    // -----------------------------------------------------------------------
    // Duplicate / update handling
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("Duplicate key handling")
    class DuplicateHandling {

        @Test
        @DisplayName("Putting same title twice does not increase size")
        void duplicatePutDoesNotIncreaseSize() {
            RecordHashTable ht = new RecordHashTable(16);
            ht.put(BOHEMIAN);
            int sizeAfterFirst = ht.size();

            // Same title, different year — counts as an update/replacement
            Record updatedBohemian = rec("Bohemian Rhapsody", 2000, "Queen Tribute", "Rock");
            ht.put(updatedBohemian);

            assertEquals(sizeAfterFirst, ht.size(),
                    "Size should not grow when the same title is inserted again");
        }

        @Test
        @DisplayName("Putting same title twice updates the stored record")
        void duplicatePutUpdatesRecord() {
            RecordHashTable ht = new RecordHashTable(16);
            ht.put(BOHEMIAN);

            Record updated = rec("Bohemian Rhapsody", 2024, "AI Queen", "Rock");
            ht.put(updated);

            assertEquals(2024, ht.get("Bohemian Rhapsody").getYear());
        }
    }

    // -----------------------------------------------------------------------
    // Remove & tombstone behaviour
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("remove() and tombstone behaviour")
    class RemoveAndTombstone {

        private RecordHashTable ht;

        @BeforeEach
        void setup() {
            ht = new RecordHashTable(16);
        }

        @Test
        @DisplayName("contains returns false after remove")
        void containsFalseAfterRemove() {
            ht.put(BOHEMIAN);
            ht.remove(BOHEMIAN);
            assertFalse(ht.contains(BOHEMIAN));
        }

        @Test
        @DisplayName("get returns null after remove")
        void getReturnsNullAfterRemove() {
            ht.put(THRILLER);
            ht.remove(THRILLER);
            assertNull(ht.get("Thriller"));
        }

        @Test
        @DisplayName("size decrements after remove")
        void sizeDecrementsAfterRemove() {
            ht.put(BOHEMIAN);
            ht.put(THRILLER);
            ht.remove(BOHEMIAN);
            assertEquals(1, ht.size());
        }

        @Test
        @DisplayName("isEmpty returns true when last record is removed")
        void isEmptyAfterLastRemove() {
            ht.put(IMAGINE);
            ht.remove(IMAGINE);
            assertTrue(ht.isEmpty());
        }

        @Test
        @DisplayName("Removing a non-existent record does not alter size")
        void removeNonExistentDoesNotChangeSize() {
            ht.put(BOHEMIAN);
            ht.remove(THRILLER); // never added
            assertEquals(1, ht.size());
        }

        @Test
        @DisplayName("Records inserted after removal are still retrievable (tombstone probing)")
        void insertAfterRemovalStillRetrievable() {
            ht.put(BOHEMIAN);
            ht.remove(BOHEMIAN);
            ht.put(STAIRWAY);
            assertNotNull(ht.get("Stairway to Heaven"));
        }

        @Test
        @DisplayName("Re-inserting a removed title works correctly")
        void reInsertAfterRemove() {
            ht.put(HOTEL_CAL);
            ht.remove(HOTEL_CAL);
            ht.put(HOTEL_CAL);
            assertTrue(ht.contains(HOTEL_CAL));
            assertEquals(1, ht.size());
        }

        @Test
        @DisplayName("Removing a record does not affect other records at nearby slots")
        void removeDoesNotDisruptNeighbours() {
            ht.put(BOHEMIAN);
            ht.put(THRILLER);
            ht.put(HOTEL_CAL);
            ht.remove(THRILLER);
            assertTrue(ht.contains(BOHEMIAN));
            assertTrue(ht.contains(HOTEL_CAL));
        }
    }

    // -----------------------------------------------------------------------
    // Collision handling (linear probing)
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("Collision handling via linear probing")
    class CollisionHandling {

        @Test
        @DisplayName("All records are retrievable when collisions force probing")
        void allRecordsRetrievableUnderCollision() {
            // Small table forces collisions
            RecordHashTable ht = new RecordHashTable(4);
            ht.put(BOHEMIAN);
            ht.put(THRILLER);
            ht.put(HOTEL_CAL);

            assertNotNull(ht.get("Bohemian Rhapsody"));
            assertNotNull(ht.get("Thriller"));
            assertNotNull(ht.get("Hotel California"));
        }

        @Test
        @DisplayName("get() for absent record does not return a collision neighbour")
        void getDoesNotReturnCollisionNeighbour() {
            RecordHashTable ht = new RecordHashTable(4);
            ht.put(BOHEMIAN);
            ht.put(STAIRWAY);
            assertNull(ht.get("Imagine")); // not inserted
        }
    }

    // -----------------------------------------------------------------------
    // Rehashing
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("Rehashing — load threshold behaviour")
    class RehashingBehaviour {

        @Test
        @DisplayName("All records survive a rehash")
        void allRecordsSurviveRehash() {
            // Start very small so we hit the 0.8 threshold quickly
            RecordHashTable ht = new RecordHashTable(5);
            Record[] records = {BOHEMIAN, THRILLER, HOTEL_CAL, IMAGINE, STAIRWAY,
                    PURPLE_H, SMELLS_LIKE, BITTER_SWEET};

            for (Record r : records) {
                ht.put(r);
            }

            for (Record r : records) {
                assertNotNull(ht.get(r.getTitle()),
                        "Expected to find '" + r.getTitle() + "' after rehash");
            }
        }

        @Test
        @DisplayName("Size count is correct after rehash")
        void sizeCorrectAfterRehash() {
            RecordHashTable ht = new RecordHashTable(5);
            Record[] records = {BOHEMIAN, THRILLER, HOTEL_CAL, IMAGINE, STAIRWAY, PURPLE_H};
            for (Record r : records) ht.put(r);
            assertEquals(records.length, ht.size());
        }

        @Test
        @DisplayName("Tombstones are not rehashed (not counted in new table)")
        void tombstonesNotPreservedAcrossRehash() {
            RecordHashTable ht = new RecordHashTable(5);
            ht.put(BOHEMIAN);
            ht.put(THRILLER);
            ht.put(HOTEL_CAL);
            ht.remove(THRILLER);

            // Force additional inserts to trigger rehash
            ht.put(IMAGINE);
            ht.put(STAIRWAY);

            assertNull(ht.get("Thriller"),
                    "Removed record should not reappear after rehash");
        }
    }

    // -----------------------------------------------------------------------
    // hash function
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("hashFunction — boundary / determinism checks")
    class HashFunctionTests {

        @Test
        @DisplayName("hashFunction returns a non-negative index")
        void hashFunctionNonNegative() {
            RecordHashTable ht = new RecordHashTable(16);
            // We call put and verify no ArrayIndexOutOfBoundsException is thrown
            assertDoesNotThrow(() -> {
                ht.put(BITTER_SWEET);
                ht.put(SMELLS_LIKE);
                ht.put(GOOD_TIMES);
            });
        }

        @Test
        @DisplayName("hashFunction is deterministic — same title always maps to same slot")
        void hashFunctionIsDeterministic() {
            RecordHashTable ht1 = new RecordHashTable(16);
            RecordHashTable ht2 = new RecordHashTable(16);
            ht1.put(TINY_DANCER);
            ht2.put(TINY_DANCER);
            // Both tables should find the record (same hash → same index)
            assertNotNull(ht1.get("Tiny Dancer"));
            assertNotNull(ht2.get("Tiny Dancer"));
        }

        @ParameterizedTest(name = "Single-char title \"{0}\" does not crash")
        @ValueSource(strings = {"X", "Z", "M", "Q"})
        @DisplayName("Single-character titles handled without error")
        void singleCharTitleHandled(String title) {
            RecordHashTable ht = new RecordHashTable(16);
            Record r = rec(title, 2020, "Artist", "Genre");
            assertDoesNotThrow(() -> ht.put(r));
            assertNotNull(ht.get(title));
        }
    }

    // -----------------------------------------------------------------------
    // Long / unusual titles (real-world edge cases)
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("Unusual and long titles")
    class UnusualTitles {

        @Test
        @DisplayName("Very long title can be stored and retrieved")
        void veryLongTitle() {
            RecordHashTable ht = new RecordHashTable(16);
            String longTitle = "A" + "b".repeat(500) + "Z";
            Record r = rec(longTitle, 2020, "Some Artist", "Experimental");
            ht.put(r);
            assertNotNull(ht.get(longTitle));
        }

        @Test
        @DisplayName("Title with spaces and special characters")
        void titleWithSpecialCharacters() {
            RecordHashTable ht = new RecordHashTable(16);
            Record r = rec("(Don't Fear) The Reaper", 1976, "Blue Öyster Cult", "Rock");
            ht.put(r);
            assertNotNull(ht.get("(Don't Fear) The Reaper"));
        }

        @Test
        @DisplayName("Title with numbers")
        void titleWithNumbers() {
            RecordHashTable ht = new RecordHashTable(16);
            Record r = rec("99 Luftballons", 1983, "Nena", "New Wave");
            ht.put(r);
            assertNotNull(ht.get("99 Luftballons"));
        }

        @Test
        @DisplayName("Unicode / non-ASCII title")
        void unicodeTitle() {
            RecordHashTable ht = new RecordHashTable(16);
            Record r = rec("Rødhåd — Synaptic Memories", 2018, "Rødhåd", "Techno");
            ht.put(r);
            assertNotNull(ht.get("Rødhåd — Synaptic Memories"));
        }
    }

    // -----------------------------------------------------------------------
    // Stress / load tests
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("Stress tests — large number of records")
    class StressTests {

        @Test
        @DisplayName("Inserting 500 unique records all remain retrievable")
        void insert500Records() {
            RecordHashTable ht = new RecordHashTable(16);
            for (int i = 0; i < 500; i++) {
                Record r = rec("Track_" + i, 1900 + i % 125, "Artist_" + (i % 50), "Genre_" + (i % 10));
                ht.put(r);
            }
            assertEquals(500, ht.size());
            for (int i = 0; i < 500; i++) {
                assertNotNull(ht.get("Track_" + i),
                        "Expected Track_" + i + " to be present");
            }
        }

        @Test
        @DisplayName("Alternating put/remove keeps size consistent")
        void alternatingPutRemove() {
            RecordHashTable ht = new RecordHashTable(16);
            for (int i = 0; i < 200; i++) {
                Record r = rec("Song_" + i, 2000, "Band", "Pop");
                ht.put(r);
            }
            // Remove even-indexed entries
            for (int i = 0; i < 200; i += 2) {
                ht.remove(rec("Song_" + i, 2000, "Band", "Pop"));
            }
            assertEquals(100, ht.size());
            // Odd-indexed ones must still be present
            for (int i = 1; i < 200; i += 2) {
                assertNotNull(ht.get("Song_" + i));
            }
            // Even-indexed ones must be gone
            for (int i = 0; i < 200; i += 2) {
                assertNull(ht.get("Song_" + i));
            }
        }
    }

    // -----------------------------------------------------------------------
    // dump() — smoke test (no crash, correct output shape)
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("dump() — smoke tests")
    class DumpTests {

        @Test
        @DisplayName("dump() does not throw on empty table")
        void dumpEmptyTable() {
            RecordHashTable ht = new RecordHashTable(8);
            assertDoesNotThrow(ht::dump);
        }

        @Test
        @DisplayName("dump() does not throw after puts and removes")
        void dumpAfterPutsAndRemoves() {
            RecordHashTable ht = new RecordHashTable(8);
            ht.put(BOHEMIAN);
            ht.put(THRILLER);
            ht.remove(BOHEMIAN);
            assertDoesNotThrow(ht::dump);
        }
    }
}
