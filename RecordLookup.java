import java.io.*;
import java.util.Scanner;
/**
 * Interface for managing the record hash table
 * Loads a csv into the record look up table
 */
public class RecordLookup {

    /**
     * Entry point for the terminal
     * prints out the options for the user
     * takes there input and executes there request
     * the user can:
     * 1. get the size
     * 2. print the table
     * 3. Search the table for a record
     * 4. Add a record to the table
     * 5. Remove a record to the table
     * 6. Exits the table
     * 
     * handles errors gracefully
     * calls load CSV function to load "records.csv"
     * 
     * @param args does nothing
     */
    public static void main(String[] args) {
        RecordHashTable table = new RecordHashTable(10);

        loadCSV("records.csv",table);

        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("\n1. Size");
            System.out.println("2. Print");
            System.out.println("3. Search");
            System.out.println("4. Add");
            System.out.println("5. Remove");
            System.out.println("6. Exit");

            int choice;
            try{
                choice = Integer.parseInt(sc.nextLine());
            }catch(Exception e){
                System.out.println("Invalid input");
                continue;
            }

            switch(choice){
                case 1:
                    System.out.println("Size: " + table.size());
                    break;

                case 2:
                    table.dump();
                    break;

                case 3:
                    System.out.print("Enter title: ");
                    String title = sc.nextLine();
                    Record found = table.get(title);
                    System.out.println(found == null ? "Not found" : found);
                    break;

                case 4:
                    try {
                        System.out.print("Genre: ");
                        String g = sc.nextLine();

                        System.out.print("Year: ");
                        int y = Integer.parseInt(sc.nextLine());

                        System.out.print("Artist: ");
                        String a = sc.nextLine();

                        System.out.print("Title: ");
                        String t = sc.nextLine();

                        table.put(new Record(g, y, a, t));
                    } catch (Exception e) {
                        System.out.println("Invalid input");
                    }
                    break;

                case 5:
                    System.out.print("Title to remove: ");
                    String rem = sc.nextLine();
                    Record toRemove = table.get(rem);

                    if (toRemove != null) {
                        table.remove(toRemove);
                        System.out.println("Removed");
                    } else {
                        System.out.println("Not found");
                    }
                    break;

                case 6:
                    System.out.println("Ending program");
                    return;

                default:
                    System.out.println("Invalid option");
            }
        }
    }

    /**
     * Loads in a file and seperates the entires into genre, year, artist and title
     * and puts that into a new record
     * 
     * handles errors gracefully
     * 
     * @param filename name of the file to load in 
     * @param table to add the file entries to 
     */
    private static void loadCSV(String filename, RecordHashTable table){
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            br.readLine();
            while ((line = br.readLine()) != null){
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");

                if (parts.length!= 4){
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                String genre = parts[0].trim();
                int year = Integer.parseInt(parts[1].trim());
                String artist = parts[2].trim();
                String title = parts[3].trim();
                Record r = new Record(genre,year, artist, title);
                table.put(r);
            }
            System.out.println("CSV loaded successfully");
        }
        catch (FileNotFoundException e) 
        {
            System.out.println("CSV file not found");
        }
        catch (Exception e)
        {
            System.out.println("Error reading CSV");
        }
    }
}
