package doggymania;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

class MouseHandler extends MouseAdapter {
    
    // boolean variable to determine if player is clicking or not
    private boolean active = false;
    
    // for setting the jlabel to correct location on screen
    private int xDisp;
    private int yDisp;
    
    // for recording point of initial click
    int startX = 0, startY = 0;
    
    // for accessing element within grid bag
    int gridBagX, gridBagY;
    
    // for swapping values in gameBoard
    String tmp;
    
    // for getting correct swing components to manipulate
    GridBagLayout layout;
    GridBagConstraints tmpgbc;

    @Override
    public void mousePressed(MouseEvent e) throws IllegalArgumentException {
        
        // setting active to true only when player clicks
        active = true;
        
        // getting the label object that's moving
        JLabel label = (JLabel)e.getComponent();
        
        // recording start click location
        startX = e.getX();
        startY = e.getY();
        
        // getting x and y values on the display
        xDisp = startX - label.getLocation().x;
        yDisp = startY - label.getLocation().y;
        
        // getting the label's parent's layout instance and its constraints object
        layout = (GridBagLayout)label.getParent().getLayout();
        tmpgbc = layout.getConstraints(label);
        
        // getting the location of the label within the grid bag constraints for use in gameBoard
        gridBagY = tmpgbc.gridx + 2;
        gridBagX = tmpgbc.gridy + 2;
        
        try {
            
            // adding label to drag layer    
            label.getParent().add(label, JLayeredPane.DRAG_LAYER);
            
        } catch (IllegalArgumentException ex)   {
            // do nothing, game works fine even with error
        }
        
        // setting the cursor to the move type
        label.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        
    } // end mousePressed method

    @Override
    public void mouseReleased(MouseEvent e) {
        
        // keeping a copy of what the board is like before the move is made
        String[][] previousState = GameLogic.arrayCopy(DoggyMania.gameBoard);
        
        int arrayLength = DoggyMania.gameBoard.length;
        
        // getting the point where the mouse is released
        int endX = e.getX(), endY = e.getY();
        
        // values to pass to score method
        int paramX = 0, paramY = 0;
        
        // getting the JLabel object
        JLabel label = (JLabel)e.getComponent();
        
        // ending the drag
        active = false;
        
        // setting the cursor to the default type
        label.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        
        try {
            
            // checking drag direction while allowing tolerance for unintentional movement in other axis
            if (endX > startX + label.getWidth() & (endY >= startY - (label.getHeight()/2) || endY <= startY + (label.getHeight()/2)) & gridBagY < arrayLength - 3)  {
            
                // x increasing - mouse is dragged to the right
                tmp = DoggyMania.gameBoard[gridBagX][gridBagY];
                DoggyMania.gameBoard[gridBagX][gridBagY] = DoggyMania.gameBoard[gridBagX][gridBagY + 1];
                DoggyMania.gameBoard[gridBagX][gridBagY + 1] = tmp;
                
                // setting values to pass to getScore based on clicked tile's final location in array
                paramX = gridBagX;
                paramY = gridBagY + 1;
            
            } else if (endX < startX - label.getWidth() & (endY >= startY - (label.getHeight()/2) || endY <= startY + (label.getHeight()/2)) & gridBagY > 2)  {

                // x decreasing - mouse is dragged to the left
                tmp = DoggyMania.gameBoard[gridBagX][gridBagY];
                DoggyMania.gameBoard[gridBagX][gridBagY] = DoggyMania.gameBoard[gridBagX][gridBagY - 1];
                DoggyMania.gameBoard[gridBagX][gridBagY - 1] = tmp;
                
                // setting values to pass to getScore based on clicked tile's final location in array
                paramX = gridBagX;
                paramY = gridBagY - 1;

            } else if (endY > startY + label.getHeight() & (endX >= startX - (label.getWidth()/2) || endX <= startX + label.getWidth()/2) & gridBagX < arrayLength - 3)  {
                
                // y increasing - mouse is dragged down
                tmp = DoggyMania.gameBoard[gridBagX][gridBagY];
                DoggyMania.gameBoard[gridBagX][gridBagY] = DoggyMania.gameBoard[gridBagX + 1][gridBagY];
                DoggyMania.gameBoard[gridBagX + 1][gridBagY] = tmp;
                
                // setting values to pass to getScore based on clicked tile's final location in array
                paramX = gridBagX + 1;
                paramY = gridBagY;

            } else if (endY < startY - label.getHeight() & (endX >= startX - (label.getWidth()/2) || endX <= startX + (label.getWidth()/2)) & gridBagX > 2)  {

                // y decreasing - mouse is dragged up
                tmp = DoggyMania.gameBoard[gridBagX][gridBagY];
                DoggyMania.gameBoard[gridBagX][gridBagY] = DoggyMania.gameBoard[gridBagX - 1][gridBagY];
                DoggyMania.gameBoard[gridBagX - 1][gridBagY] = tmp;
                
                // setting values to pass to getScore based on clicked tile's final location in array
                paramX = gridBagX - 1;
                paramY = gridBagY;

            } else  {
                
                // invalid move, just repaints the board to previous state
                throw new ArrayIndexOutOfBoundsException();
                
            } // end if/else
            
            // only committing the move if it forms a chain
            if (GameLogic.isValidMove(DoggyMania.gameBoard, paramX, paramY))    {
                
                // decreasing number of moves
                DoggyMania.numberOfMoves--;

                // adding score from this move to player's score
                DoggyMania.playerScore += GameLogic.getScore(DoggyMania.gameBoard, paramX, paramY);

                // catching any additional matches after board is repainted
                GameLogic.catchMatches(DoggyMania.gameBoard);
                
            } else  {
                
                // reverting gameBoard back to its previous state
                DoggyMania.gameBoard = GameLogic.arrayCopy(previousState);
                
            } // end if
            
            //repainting the game window
            DoggyMania.repaintWindow();
            
            // checking if player's move results in an end-level situation
            if (DoggyMania.playerScore >= DoggyMania.reqScore)  {
                
                try {
                    GameLogic.advanceLevel();
                } catch (IOException ex)    {
                    ex.getMessage();
                }
                
            } else if (DoggyMania.numberOfMoves == 0 & DoggyMania.playerScore < DoggyMania.reqScore)   {
                
                try {
                    GameLogic.restartLevel();
                } catch (IOException ex)    {
                    ex.getMessage();
                }
                
            } // end if/else
            
        } catch (ArrayIndexOutOfBoundsException ex) {
          
            // repainting the window if an invalid move happens
            DoggyMania.repaintWindow();
            
        } // end try/catch
        
        GameLogic.catchMatches(DoggyMania.gameBoard);
        
    } // end mouseReleased method

    @Override
    public void mouseDragged(MouseEvent e) {
        
        // checking whether player has clicked
        if (active) {
            
            // getting the label that was clicked
            JLabel label = (JLabel)e.getComponent();
            
            // getting the point where the mouse currently is
            Point point = e.getPoint();
            
            // updating the label's location
            label.setLocation(point.x - xDisp, point.y - yDisp);
            
            // invalidating and repainting the label
            label.invalidate();
            label.repaint();

        } // end if
        
    } // end mouseDragged method

    @Override
    public void mouseMoved(MouseEvent e) {} // end mouseMoved method
    
} // end MouseHandler class