package doggymania;

import java.io.IOException;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameLogic {
    
    // button text for pass/fail notification
    static Object[] options = {"BORK (yes)", "BORK BORK (no)"};
    
    // arrays to implement Dynamic Programming
    static int[][] horizontalMatchArray, verticalMatchArray;
    
    // generates random numbers to repopulate the array after a match has been dealt with
    private static final Random r = new Random();
    
    // implementation of Dynamic Programming
    public static void horizontalMatchFinder(String[][] stringArgs)  {
        
        horizontalMatchArray = new int[stringArgs.length][stringArgs.length];
        
        for (int i = 0; i < stringArgs.length; i++) {
            
            // counter to keep track of chain length
            int matchCount = 1;

            // filling the last index on every row with a 1, the only value it can take
            horizontalMatchArray[i][horizontalMatchArray.length - 1] = matchCount;
            
            for (int j = stringArgs.length - 2; j >= 0; j--) {
                
                if (stringArgs[i][j].equals(stringArgs[i][j + 1]))  {
                    
                    // incrementing matchCount to because the chain continues
                    matchCount++;
                    
                } else  {
                    
                    // resetting matchCount because a new chain starts
                    matchCount = 1;
                    
                } // end if/else
                
                horizontalMatchArray[i][j] = matchCount;
                
            } // end inner for
        } // end outer for
        
    } // end horizontalMatchFinder
    
    public static int getHorizontalScore(String[][] stringArgs, int x, int y)    {
        
        // getting the type of tile that makes up the chain
        String matchedDog = stringArgs[x][y];
        
        // getting final length of chain from array
        int longestChain = horizontalMatchArray[x][y];
        
        // returning score
        return getScoreValue(longestChain, matchedDog);
        
    } // end getHorizontalScore
    
    public static void horizontalReplacement(String[][] stringArgs, int x, int y, int chainLength)    {
        
        for (int i = x; i >= 2; i--)  {
            for (int j = y; j <= y + chainLength; j++)    {
                
                if (i != 2) {
                    
                    // pulling tiles down from the row above
                    stringArgs[i][j] = DoggyMania.gameBoard[i - 1][j];
                    
                } else  {
                    
                    // only generating new tiles for the top row of the stack
                    stringArgs[i][j] = DoggyMania.dogArray[r.nextInt(DoggyMania.dogArray.length)];
                    
                } // end if/else
                
            } // end for
        } // end for
        
    } // end horizontalReplacement
    
    public static void verticalMatchFinder(String[][] stringArgs)    {
        
        verticalMatchArray = new int[stringArgs.length][stringArgs.length];
        
        for (int i = stringArgs.length - 1; i >= 0; i--) {
            
            // counter to keep track of chain length
            int matchCount = 1;
            
            // filling the last index on every column with a 1, the only value it can take
            verticalMatchArray[stringArgs.length - 1][i] = matchCount;
            
            for (int j = stringArgs.length - 2; j >= 0; j--) {
                
                if (stringArgs[j][i].equals(stringArgs[j + 1][i]))  {
                    
                    // incrementing matchCount to show chain continues
                    matchCount++;
                    
                } else  {
                    
                    // resetting matchCount because a new chain starts
                    matchCount = 1;
                    
                } // end if/else
                
                verticalMatchArray[j][i] = matchCount;
                
            } // end inner for            
        } // end outer for
        
    } // end verticalMatchFinder
    
    public static int getVerticalScore(String[][] stringArgs, int x, int y)   {
        
        // getting dog that is matched
        String matchedDog = stringArgs[x][y];
        
        // getting final length of chain from array
        int longestChain = verticalMatchArray[x][y];
        
        // returning score
        return getScoreValue(longestChain, matchedDog);
        
    } // end getVerticalScore
    
    public static void verticalReplacement(String[][] stringArgs, int x, int y, int chainLength)  {
        
        for (int i = x; i < x + chainLength; i++)   {
            
            // replacing matched entries with random new ones
            stringArgs[i][y] = DoggyMania.dogArray[r.nextInt(DoggyMania.dogArray.length)];
            
        } // end for
        
    } // end verticalReplacement
    
    public static int getScore(String[][] stringArgs, int x, int y)   {
        
        // used for exploring arrays in each direction
        int copyX = x;
        int copyY = y;
        
        // score variable to return
        int toReturn = 0;
        
        // dynamically populating tables to reflect most up to date state
        horizontalMatchFinder(stringArgs);
        verticalMatchFinder(stringArgs);
        
        // getting the longest vertical chain from that index
        while (copyX > 2 & verticalMatchArray[copyX - 1][y] > verticalMatchArray[copyX][y]) {
                
            copyX--;

        } // end if
        
        // getting the longest horizontal chain from that index
        while (copyY > 2 & horizontalMatchArray[x][copyY - 1] > horizontalMatchArray[x][copyY]) {
                
            copyY--;

        } // end if
        
        // choosing the longer chain at the index player is dealing with
        if (horizontalMatchArray[x][copyY] >= 3 & horizontalMatchArray[x][copyY] > verticalMatchArray[copyX][y]) {
            
            // assigning value of full chain to score
            toReturn = getHorizontalScore(stringArgs, x, copyY);
            
            // replacing horizontally matched tiles
            horizontalReplacement(stringArgs, x, copyY, horizontalMatchArray[x][copyY]);
            
        } else if (verticalMatchArray[copyX][y] >= 3 & verticalMatchArray[copyX][y] > horizontalMatchArray[x][copyY])  {
            
            // asigning value of full chain to score
            toReturn = getVerticalScore(stringArgs, copyX, y);
            
            // replacing vertically matched tiles
            verticalReplacement(stringArgs, copyX, y, verticalMatchArray[copyX][y]);
            
        } else if (horizontalMatchArray[x][copyY] == verticalMatchArray[copyX][y])  {
            
            // getting score values for comparison
            int vertScore = getVerticalScore(stringArgs, copyX, y);
            int horScore = getHorizontalScore(stringArgs, x, copyY);
            
            // returning the highest score out of the two chains
            if (vertScore >= horScore)  {
                
                // assigning score to return variable
                toReturn = vertScore;
                
                // replacing vertically matched tiles
                verticalReplacement(stringArgs, copyX, y, verticalMatchArray[copyX][y]);
                
            } else  {
                
                // assigning score to return variable
                toReturn = horScore;
                
                // replacing horizontally matched tiles
                horizontalReplacement(stringArgs, x, copyY, horizontalMatchArray[x][copyY]);
                
            } // end if/else
            
        } // end if/else
        
        // returning the variable
        return toReturn;
        
    } // end getScore method
    
    public static void catchMatches(String[][] stringArgs)    {
        
        // dynamically populating tables to reflect most up to date state
        horizontalMatchFinder(stringArgs);
        verticalMatchFinder(stringArgs);
        
        // checking if there's a match on the board and dealing with it
        for (int i = 2; i <= horizontalMatchArray.length - 3; i++) {
            for (int j = 2; j <= horizontalMatchArray.length - 3; j++) {

                if (horizontalMatchArray[i][j] >= 3 | verticalMatchArray[i][j] >= 3)   {
                    
                    DoggyMania.playerScore += getScore(DoggyMania.gameBoard, i, j);

                } // end if

            } // end inner for
        } // end outer for
        
    } // end catchMatches method
    
    public static String[][] arrayCopy(String[][] stringArgs)   {
        
        final String[][] result = new String[stringArgs.length][stringArgs.length];
        
        for (int i = 0; i < stringArgs.length; i++) {
            
            System.arraycopy(stringArgs[i], 0, result[i], 0, stringArgs.length);
            
        } // end for
        
        return result;
        
    } // end arrayCopy method
    
    public static boolean isValidMove(String[][] stringArgs, int x, int y) {
        
        horizontalMatchFinder(stringArgs);
        verticalMatchFinder(stringArgs);
        
        int copyX = x, copyY = y;
        
        // getting to the start of the vertical chain
        while (copyX > 2 & verticalMatchArray[copyX - 1][y] > verticalMatchArray[copyX][y])  {
            copyX--;
        } // end while
        
        // getting to the start of the horizontal chain
        while (copyY > 2 & horizontalMatchArray[x][copyY - 1] > horizontalMatchArray[x][copyY])    {
            copyY--;
        } // end while
        
        return horizontalMatchArray[x][copyY] >= 3 | verticalMatchArray[copyX][y] >= 3;
        
    } // end isValidMove method
    
    public static void advanceLevel() throws IOException  {
        
        // allowing user to decide whether to quit or continue
        int input = JOptionPane.showOptionDialog(new JFrame(), "Would you like to go to the next level?", "Woof bark woof! You've won!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        // closing current game window after a choice has been made
        DoggyMania.gameWindow.dispose();

        // if the player wants to go to next level
        if (input == JOptionPane.YES_OPTION)    {

            // increasing the player's level counter
            DoggyMania.level++;

            // adding the score from this level to the player's total score
            DoggyMania.totalPlayerScore += DoggyMania.playerScore;

            // resetting player score for each level
            DoggyMania.playerScore = 0;

            // reinitialising the array for the next level
            DoggyMania.arrayBuilder(DoggyMania.level, DoggyMania.diff);

            // starting the next level
            DoggyMania.gameWindow.dispose();
            DoggyMania.mainGameScreen(DoggyMania.level);

        } else  {

            // player has selected quit, go to splash screen with same level value because they beat the level
            DoggyMania.splashScreen(DoggyMania.level, DoggyMania.username);

        } // end if/else
        
    } // end advanceLevel method
    
    public static void restartLevel() throws IOException   {
        
        // allowing user to decide whether to quit or try again
        int input = JOptionPane.showOptionDialog(new JFrame(), "Would you like to restart the level?", "Bark bark woof!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        // closing current game window after a choice has been made
        DoggyMania.gameWindow.dispose();

        // restart the same level
        if (input == JOptionPane.YES_OPTION)    {
            
            // resetting score value for the level to 0
            DoggyMania.playerScore = 0;

            // reinitialising the array for same level
            DoggyMania.arrayBuilder(DoggyMania.level, DoggyMania.diff);

            // starting level
            DoggyMania.mainGameScreen(DoggyMania.level);

        } else  {

            // go to splash screen
            DoggyMania.splashScreen(DoggyMania.level, DoggyMania.username);

        } // end if/else
        
    } // end restartLevel method
    
    public static int getScoreValue(int chainLength, String matchedDog)    {
        
        int toReturn = 0;
        
        // switching over the matched dog to assign a score based on chain length
        switch(matchedDog)    {
            
            case "labrador.jpg":
                toReturn = 200 * chainLength;
                break;

            case "husky.jpg":
                toReturn = 300 * chainLength;
                break;

            case "shiba.jpg":
                toReturn = 400 * chainLength;
                break;

            case "bulldog.jpg":
                toReturn = 500 * chainLength;
                break;

            case "beagle.jpg":
                toReturn = 600 * chainLength;
                break;

            case "pug.jpg":
                toReturn = 700 * chainLength;
                break;

            case "terrier.jpg":
                toReturn = 800 * chainLength;
                break;

            case "chihuahuah.jpg":
                toReturn = 900 * chainLength;
                break;

        } // end switch
        
        return toReturn;
        
    } // end getScoreValue method
    
} // end gameLogic class