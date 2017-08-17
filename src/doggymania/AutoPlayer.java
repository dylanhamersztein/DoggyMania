package doggymania;

import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AutoPlayer {
    
    // to check if the algorithm should carry on running
    static boolean carryOn;
    
    // to check chain lengths in reverse direction
    static int[][] reverseHorizontalMatchArray, reverseVerticalMatchArray;
    
    // used to carry out moves without affecting state of game
    static String[][] prevState, nextState;
    
    // used to swap gameBoard indices around
    static String tmp;
    
    static int paramX, paramY;
    
    // used to save the highest score found during each execution of this method
    static int highestScore = 0;
    
    public static void autoPlayer(String[][] stringArgs) throws IOException    {
        
        carryOn = true;
        
        // resetting the highest score for this iteration of the algorithm
        highestScore = 0;
        
        // saving the previous state of the gameBoard
        prevState = GameLogic.arrayCopy(stringArgs);
        
        // initialising the array for use
        nextState = new String[stringArgs.length][stringArgs.length]; 
        
        // getting most up to date chain lengths each time the method is called
        GameLogic.horizontalMatchFinder(stringArgs);
        GameLogic.verticalMatchFinder(stringArgs);
        
        reverseHorizontalMatchFinder(stringArgs);
        reverseVerticalMatchFinder(stringArgs);
        
        for (int i = 2; i <= stringArgs.length - 3; i++) {
            for (int j = 2; j <= stringArgs.length - 3; j++) {

                if (GameLogic.horizontalMatchArray[i][j] >= 2)  {

                    // checking all valid swap locations for match
                    checkScore(stringArgs, horizontalMatchArrayChecker(stringArgs, i, j), i, j);

                } // end horizontalMatchArray if

                if (GameLogic.verticalMatchArray[i][j] >= 2)    {

                    // checking all valid swap locations for match
                    checkScore(stringArgs, verticalMatchArrayChecker(stringArgs, i, j), i, j);

                } // end verticalMatchArray if

                if (reverseHorizontalMatchArray[i][j] >= 2) {

                    // checking all valid swap locations for match
                    checkScore(stringArgs, reverseHorizontalMatchArrayChecker(stringArgs, i, j), i, j);

                } // end reverseHorizontalMatchArray if

                if (reverseVerticalMatchArray[i][j] >= 2)   {

                    // checking all valid swap locations for match
                    checkScore(stringArgs, reverseVerticalMatchArrayChecker(stringArgs, i, j), i, j);

                } // end reverseVerticalMatchArray if
                
                if (stringArgs[i][j].equals(stringArgs[i][j + 2]))  {
                    
                    // checking all valid swap locations for match
                    checkScore(stringArgs, extraCase1(stringArgs, i, j), i, j);
                    
                } // end if
                
                if (stringArgs[i][j].equals(stringArgs[i + 2][j]))  {
                    
                    // checking all valid swap locations for match
                    checkScore(stringArgs, extraCase2(stringArgs, i, j), i, j);
                    
                } // end if
                
            } // end inner for
        } // end outer for
        
        // updating gameBoard to the value of nextState only if it's not full of nulls
        if (nextState[0][0] != null)  {
            
            // adding the highest score to playerScore
            DoggyMania.playerScore += highestScore;

            // decrementing number of moves remaining
            DoggyMania.numberOfMoves--;
            
            // updating gameBoard to nextState
            DoggyMania.gameBoard = GameLogic.arrayCopy(nextState);
            
            // repainting the window to show the move being made
            DoggyMania.repaintWindow();
            
            // checking whether or not the player wants to continue with the algorithm's execution
            algorithmContinueCheck(stringArgs[paramX][paramY], paramY, paramX);
            
            // catching any new matches that are made as a result of this move
            GameLogic.catchMatches(stringArgs);
            
        } else  {
            
            carryOn = false;
            
        } // end if/else
        
        // calling the method again if the score hasn't been reached
        if (carryOn & highestScore > 0 & DoggyMania.playerScore < DoggyMania.reqScore & DoggyMania.numberOfMoves >= 0)   {
            
            // calling the algorithm again
            autoPlayer(DoggyMania.gameBoard);
        
        } else if (DoggyMania.playerScore >= DoggyMania.reqScore & DoggyMania.numberOfMoves >= 0)    {
            
            // going to the next level
            GameLogic.advanceLevel();
            
        } else if (DoggyMania.numberOfMoves < 0 & DoggyMania.playerScore < DoggyMania.reqScore || nextState[0][0] == null)    {
            
            // restart level
            noMovesFound();
            
        } // end if/else if
        
    } // end autoPlayer method
    
    public static void checkScore(String[][] stringArgs, int score, int x, int y) {
        
        if (score > highestScore)    {

            // saving the highest score
            highestScore = score;
            
            // saving the location of the higest score
            paramX = x;
            paramY = y;

            // saving the state that is yielded by making the highest scoring move found until that point
            nextState = GameLogic.arrayCopy(stringArgs);
            
            // reverting back to previous state so the algorithm can keep checking
            DoggyMania.gameBoard = GameLogic.arrayCopy(prevState);
            
            // repainting the window to show move made
            DoggyMania.repaintWindow();

        } // end if
        
    } // end checkScore method
    
    //cases arising from a 2 in horizontalMatchArray
    public static int horizontalMatchArrayChecker(String[][] stringArgs, int x, int y)   {
        
        int toReturn = 0;
        
        // checking diagonal tile
        if (stringArgs[x][y].equals(stringArgs[x - 1][y - 1]))  {
            
            tmp = stringArgs[x][y - 1];
            stringArgs[x][y - 1] = stringArgs[x - 1][y - 1];
            stringArgs[x - 1][y - 1] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
        
        // checking other diagonal tile
        } else if (stringArgs[x][y].equals(stringArgs[x + 1][y - 1]))   {
            
            tmp = stringArgs[x][y - 1];
            stringArgs[x][y - 1] = stringArgs[x + 1][y - 1];
            stringArgs[x + 1][y - 1] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
        
        // checking two tiles to the left
        } else if (stringArgs[x][y].equals(stringArgs[x][y - 2]))  {
            
            tmp = stringArgs[x][y - 1];
            stringArgs[x][y - 1] = stringArgs[x][y - 2];
            stringArgs[x][y - 2] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
        
        } // end if/else
            
        return toReturn;
        
    } // end horizontalMatchArrayChecker method
    
    //cases arising from a 2 in verticalMatchArray
    public static int verticalMatchArrayChecker(String[][] stringArgs, int x, int y)    {
        
        int toReturn = 0;
        
        // checking diagonal tile
        if (stringArgs[x][y].equals(stringArgs[x - 1][y - 1]))  {
            
            tmp = stringArgs[x - 1][y - 1];
            stringArgs[x - 1][y - 1] = stringArgs[x - 1][y];
            stringArgs[x - 1][y] = tmp;        
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
            
        // checking other diagonal tile
        } else if (stringArgs[x][y].equals(stringArgs[x - 1][y + 1]))   {
            
            tmp = stringArgs[x - 1][y + 1];
            stringArgs[x - 1][y + 1] = stringArgs[x - 1][y];
            stringArgs[x - 1][y] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
        
        // checking two tiles up
        } else if (stringArgs[x][y].equals(stringArgs[x - 2][y]))  {
            
            tmp = stringArgs[x - 1][y];
            stringArgs[x - 1][y] = stringArgs[x - 2][y];
            stringArgs[x - 2][y] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
            
        } // end if/else
        
        return toReturn;
        
    } // end verticalMatchArrayChecker method
    
    // cases arising from a 2 in reverseHorizontalMatchArray
    public static int reverseHorizontalMatchArrayChecker(String[][] stringArgs, int x, int y)    {
        
        int toReturn = 0;
        
        // checking diagonal tile
        if (stringArgs[x][y].equals(stringArgs[x - 1][y + 1]))  {
            
            tmp = stringArgs[x][y + 1];
            stringArgs[x][y + 1] = stringArgs[x - 1][y + 1];
            stringArgs[x - 1][y + 1] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
        
        // checking other diagonal tile    
        } else if (stringArgs[x][y].equals(stringArgs[x + 1][y + 1]))   {
            
            tmp = stringArgs[x][y + 1];
            stringArgs[x][y + 1] = stringArgs[x + 1][y + 1];
            stringArgs[x + 1][y + 1] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
            
        // checking two tiles to the right
        } else if (stringArgs[x][y].equals(stringArgs[x][y + 2]))  {
            
            tmp = stringArgs[x][y + 1];
            stringArgs[x][y + 1] = stringArgs[x][y + 2];
            stringArgs[x][y + 2] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
            
        } // end if/else
        
        return toReturn;
        
    } // end reverseHorizontalMatchArrayChecker method
    
    // cases arising from a 2 in reverseVerticalMatchArray
    public static int reverseVerticalMatchArrayChecker(String[][] stringArgs, int x, int y)    {
        
        int toReturn = 0;
        
        // checking diagonal tile
        if (stringArgs[x][y].equals(stringArgs[x + 1][y - 1]))  {
            
            tmp = stringArgs[x + 1][y];
            stringArgs[x + 1][y] = stringArgs[x + 1][y - 1];
            stringArgs[x + 1][y - 1] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
        
        // checking other diagonal tile    
        } else if (stringArgs[x][y].equals(stringArgs[x + 1][y + 1]))  {
            
            tmp = stringArgs[x + 1][y];
            stringArgs[x + 1][y] = stringArgs[x + 1][y + 1];
            stringArgs[x + 1][y + 1] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
        
        // checking two tiles down
        } else if (stringArgs[x][y].equals(stringArgs[x + 2][y]))  {
            
            tmp = stringArgs[x + 1][y];
            stringArgs[x + 1][y] = stringArgs[x + 2][y];
            stringArgs[x + 2][y] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
            
        } // end if
        
        return toReturn;
        
    } // end reverseVerticalMatchArrayChecker method
    
    public static int extraCase1(String[][] stringArgs, int x, int y)  {
        
        int toReturn = 0;
        
        // checking tile one row above inbetween the two identical tiles
        if (stringArgs[x][y].equals(stringArgs[x + 1][y + 1]))  {
            
            tmp = stringArgs[x][y + 1];
            stringArgs[x][y + 1] = stringArgs[x + 1][y + 1];
            stringArgs[x + 1][y + 1] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
        
        // checking tile one row below inbetween the two identical tiles
        } else if (stringArgs[x][y].equals(stringArgs[x - 1][y + 1])) {
            
            tmp = stringArgs[x][y + 1];
            stringArgs[x][y + 1] = stringArgs[x - 1][y + 1];
            stringArgs[x - 1][y + 1] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
            
        } // end if/else if
        
        return toReturn;
        
    } // end extraCase1 method
    
    public static int extraCase2(String[][] stringArgs, int x, int y)   {
        
        int toReturn = 0;
        
        if (stringArgs[x][y].equals(stringArgs[x + 1][y + 1]))  {
            
            tmp = stringArgs[x + 1][y];
            stringArgs[x + 1][y] = stringArgs[x + 1][y + 1];
            stringArgs[x + 1][y + 1] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);

        } else if (stringArgs[x][y].equals(stringArgs[x + 1][y - 1]))  {
            
            tmp = stringArgs[x + 1][y];
            stringArgs[x + 1][y] = stringArgs[x + 1][y - 1];
            stringArgs[x + 1][y - 1] = tmp;
            
            toReturn = GameLogic.getScore(stringArgs, x, y);
            
        } //end if/else if
        
        return toReturn;
        
    } // end extraCase2 method
    
    public static void reverseHorizontalMatchFinder(String[][] stringArgs)  {
        
        reverseHorizontalMatchArray = new int[stringArgs.length][stringArgs.length];
        
        for (int i = 0; i < stringArgs.length; i++) {
            
            int matchCount = 1;
            
            reverseHorizontalMatchArray[i][0] = matchCount;
            
            for (int j = 1; j < stringArgs.length; j++) {
                
                if (stringArgs[i][j].equals(stringArgs[i][j - 1]))  {
                    
                    matchCount++;
                    
                } else  {
                    
                    matchCount = 1;
                    
                } // end if/else
                
                reverseHorizontalMatchArray[i][j] = matchCount;
                
            } // end inner for
        } // end outer for
        
    } // end reverseHorizontalMatchFinder method
    
    public static void reverseVerticalMatchFinder(String[][] stringArgs)    {
        
        reverseVerticalMatchArray = new int[stringArgs.length][stringArgs.length];
        
        for (int i = 0; i < stringArgs.length; i++) {
            
            int matchCount = 1;
            reverseVerticalMatchArray[0][i] = matchCount;
            
            for (int j = 1; j < stringArgs.length; j++) {
                
                
                if (stringArgs[j][i].equals(stringArgs[j - 1][i]))  {
                    
                    matchCount++;
                    
                } else  {
                    
                    matchCount = 1;
                    
                } // end if/else
                
                reverseVerticalMatchArray[j][i] = matchCount;
                
            } // end inner for
        } // end outer for
        
    } // end reverseVerticalMatchFinder method
    
    public static void algorithmContinueCheck(String matchedDog, int x, int y) throws IOException   {
        
        String msg = "The highest scoring chain on the board consists of " + ((matchedDog.substring(0, 1).toUpperCase() + matchedDog.substring(1)).replaceAll(".jpg", "")) + " tiles, found at: (" + (x - 2) + ", " + (y - 2) + ").\nThe move was successfully executed. Would you like to continue?";
        
        // allowing user to decide whether to quit or try again
        int input = JOptionPane.showOptionDialog(new JFrame(), msg, "Bark bark woof!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, GameLogic.options, GameLogic.options[0]);

        // letting the user decide whether or not to execute the algorithm again
        if (input == JOptionPane.NO_OPTION)    {
            
            carryOn = false;

        } // end if/else
        
    } // end restartLevel method
    
    public static void noMovesFound() throws IOException   {
        
        // allowing user to decide whether to quit or try again
        int input = JOptionPane.showOptionDialog(new JFrame(), "The algorithm could not find any moves. Would you like to restart the level?", "Bark bark woof!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, GameLogic.options, GameLogic.options[0]);

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
    
} // end AutoPlayer class