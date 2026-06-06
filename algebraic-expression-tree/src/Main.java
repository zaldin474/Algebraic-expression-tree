//-----------------------------------------------------
// Title: Main Class
// Author: Zeineddin Zidan
// ID: 99621968516
// Section: 1
// Assignment: 3
// Description: This class processes files containing algebraic expression commands
// and interacts with the AlgExpressionTree class to build, evaluate, and display expressions.
//-----------------------------------------------------

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    private static AlgExpressionTree tree;

    //--------------------------------------------------------
    // Summary: Main method to process input files and handle commands
    //--------------------------------------------------------
    public static void main(String[] args) {
        // Loop through the file numbers (1 to 6) to process all the files at once
        for (int i = 1; i <= 6; i++) {
            String filename = "sample_input" + i + ".txt";
            System.out.println("Processing file: " + filename);
            processFile(filename);
            System.out.println();
        }
    }

    //--------------------------------------------------------
    // Summary: Processes the file and reads the commands
    //--------------------------------------------------------
    private static void processFile(String filename) {
        try {
            File inputFile = new File(filename);
            Scanner scanner = new Scanner(inputFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    processCommand(line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

    //--------------------------------------------------------
    // Summary: Processes a single command line from the file
    //--------------------------------------------------------
    private static void processCommand(String commandLine) {
        String[] parts = commandLine.split(" ");
        String command = parts[0];

        switch (command) {
            //--------------------------------------------------------
            // Summary: Creates a new algebraic expression tree from the input expression
            //--------------------------------------------------------
            case "CREATE_TREE":
                String notation = parts[1];
                String expression = parts[2];
                tree = new AlgExpressionTree(expression, notation);
                break;

            //--------------------------------------------------------
            // Summary: Sets the value for a variable in the expression tree
            //--------------------------------------------------------
            case "SET_VAR":
                if (tree != null) {
                    char varName = parts[1].charAt(0);
                    int value = Integer.parseInt(parts[2]);
                    tree.setVariable(varName, value);
                } else {
                    System.out.println("Error: Tree not initialized.");
                }
                break;

            //--------------------------------------------------------
            // Summary: Evaluates the algebraic expression tree and prints the result
            //--------------------------------------------------------
            case "EVALUATE":
                if (tree != null) {
                    double result = tree.evaluate();
                    System.out.println("Result: " + result);
                } else {
                    System.out.println("Error: Tree not initialized.");
                }
                break;

            //--------------------------------------------------------
            // Summary: Displays the expression in postfix notation
            //--------------------------------------------------------
            case "DISPLAY_POSTFIX":
                if (tree != null) {
                    System.out.print("Postfix: ");
                    tree.displayPostfix();
                } else {
                    System.out.println("Error: Tree not initialized.");
                }
                break;

            //--------------------------------------------------------
            // Summary: Handles unknown commands
            //--------------------------------------------------------
            default:
                System.out.println("Unknown command: " + command);
        }
    }
}
