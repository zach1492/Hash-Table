/**
 * A hash table of records
 * 
 * Allows for putting records in,
 * removeing records,
 * rehashing the table,
 * printing the table,
 * empty checking, 
 * size checking and
 * checking if record is in the table
 */
public class RecordHashTable{
    private Record[] table;
    private int size;
    private int count;
    private final double LOAD_THRESHOLD = 0.8;
    private final Record TOMBSTONE = new Record("",0,"","DELETED");//place holder for removed records

    /**
     * Hash table constructor
     * 
     * @param size is the varible that hold the tables size
     */
    public RecordHashTable(int size) {
        this.size = size;
        this.table = new Record[size];
        this.count = 0;
    }

    /**
     * Uses folding and remainder to generate and index for a records title
     * 
     * @param r is the record to generate the index for
     * @return index for the record
     */
    public int hashFunction(Record r){
        String key = r.getTitle();
        int sum = 0;

        for (int i = 0; i < key.length(); i += 2) {
            int chunk;
            if (i + 1 <key.length()){
                chunk = (key.charAt(i) << 7) + key.charAt(i + 1);
            } else{
                chunk = key.charAt(i);
            }
            sum += chunk;
        }

        return sum % size;
    }

    /**
     * Puts a record in to the table 
     * Rehashes if the load factor is to high
     * 
     * @param r is the record to put into the table
     */
    public void put(Record r){
        int index = hashFunction(r);

        while(table[index] != null && table[index] != TOMBSTONE && !table[index].getTitle().equals(r.getTitle())){
            index = (index + 1) % size;
        }
        if(table[index] == null || table[index] == TOMBSTONE) {//Only increase count if a duplicate wasnt replaced
            count++;
        }
        table[index] = r;

        if((double) count / size >= LOAD_THRESHOLD){
            rehash();
        }
    }

    /**
     * Removes a record from the table
     * 
     * @param r is the record to remove
     */
    public void remove(Record r){
        int index = hashFunction(r);
        int probe = 0;
        while(table[index] != null && probe < size){
            if(table[index] != TOMBSTONE &&table[index].getTitle().equals(r.getTitle())){
                table[index] = TOMBSTONE;
                count--;
                
                return;
            }
            index = (index + 1) % size;

            probe ++;
        }
    }

    /**
     * Doubles the tables size if load factor is to high
     * puts old records into new table
     */
    public void rehash(){
        Record[] old = table;
        size *= 2;
        table = new Record[size];
        count = 0;

        for(Record r : old) {
            if(r != null && r != TOMBSTONE) {
                put(r);
            }
        }
    }

    /**
     * Bool check for if a record is in the table
     * 
     * @param r is the record to check
     * @return true if the record is there, false if the record is not
     */
    public boolean contains(Record r){
        return get(r.getTitle())!=null;
    }

    /**
     * Returns a record based on its title
     * 
     * @param title of the record to find
     * @return returns the record of that title
     */
    public Record get(String title) {
        Record probe = new Record("", 0, "", title);
        int index = hashFunction(probe);
        while (table[index] != null) {
            if (table[index] != TOMBSTONE && table[index].getTitle().equals(title)) {
                return table[index];
            }
            index = (index + 1) % size;
        }
        return null;
    }

    /**
     * Returns a bool based on if the table is empty
     * 
     * @return true if table if empty false if not
     */
    public boolean isEmpty() {
        return count == 0;
    }


    /**
     * Returns a amount of records in table
     * 
     * @return returns an int on the number of records
     */
    public int size() {
        return count;
    }


    /**
     * Prints out entire table 
     * and all information of records
     */
    public void dump() {
        for (int i = 0; i < size; i++) {
            if (table[i] == null || table[i] == TOMBSTONE) {
                System.out.println(i + ": null");
            } else {
                Record r = table[i];
                System.out.println(i + ": " + r.getTitle() + ", " + r.getGenre() + " | " +r.getYear() + " | " + r.getArtist());
            }
        }
    }
}
