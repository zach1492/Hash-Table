# Hash Table

The objective of this university project was to build a hash table for a record object with a name, year, artist and genre. We had to also provide some UI through terminal and to test the project with JUnit. We had to write some JUnit tests our selves and then do the rest with AI and then analysis it preformance in AIDocumentation.txt to see how well it expanded test converage.

## Technolgies

 • Java
 
 • Visual Studio Code 
 
 • Terminal

 • JUnit

 ## Features
 
 • Add, remove and get records
 
 • Collision handling
 
 • Rehashing if the load factor get above 80%
 
 • Loading in a CSV file of records
 
 • Returns size

 ## Commands

To run:

 ```Console
javac *.java

java RecordLookup
```

## Controls
Type:
 1. To get the size
 2. Print the table
 3. Search a record
 4. To add a record
 5. To remove a record
 6. To Exit

 ## Process

Luckily before I began this project I already the record script pre written from an AVL tree project so firstly I imported that.

After that I wrote the hash table script with all the required functions like remove, a hash function, add etc

Next I added the look up script to allow the user to use the hash table through terminal.

Then I coded a JUnit test script to test the inputs I thought were important.

After that I got Claude to write its own JUnit test script to improve coverage. Then I wrote an analysis of prompts used and how well it did.

## What I Learned

### JUnit

I had never used JUnit before so the project helped me learn the basic commands and how it works. It also taught me the value in getting AI to write tests for you because its a lot quicker and can therefor cover more inputs

### Hashing

Hashing was an interting new concept in project. It taught the importance of Modulo, colision handling and how much faster hash tables are then other data structures

### Testing

Before this project I only really tested by just using a program normally but this project showed me the importance of purposeful testing and the value in checking boundry cases and edge cases

## How it can be improved

### Allow the user to pass there own file
Right now the file is hard coded so it would be better if the user could choose a file to load in

### Have propor user interface
The user interface currently runs on terminal which isn't very user friendly. An improvement would be having an actual application with buttons for commands

### A better search engine
Right now you can only search by title but it would be cool if you could search records by year and genre too.

## To run the project locally:

Clone the repository to your machine

Open a terminal in the folder and use the commands above
