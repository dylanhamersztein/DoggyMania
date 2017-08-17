package doggymania;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoggyMania extends JFrame implements ActionListener    {
    
    // declaring swing componenents
    static JFrame splashWindow, instructionsWindow, gameWindow;
    static JButton start, quit, save, load, autoComplete, restartLevel, instructionsScreenButton;
    static JPanel gameCP;
    static JLabel movesRemaining, score, totalScore;
    static JTextField user;
    static JComboBox difficultySelect;
    
    // difficulty options
    static String[] difficulties = {"Easy", "Normal", "Hard"};
    
    // class difficulty string
    static String diff;
    
    // declaring primitive variables
    static int level = 1, numberOfMoves, reqScore, playerScore, totalPlayerScore;
    
    // declaring String array
    static String[][] gameBoard;
   
    static final String[] dogArray = {
        "labrador.jpg",
        "husky.jpg",
        "shiba.jpg",
        "terrier.jpg",
        "bulldog.jpg",
        "beagle.jpg",
        "chihuahuah.jpg",
        "pug.jpg"
    };
    
    // username of player
    static String username;
    
    public static JFrame splashScreen(final int lvl, String userName) throws IOException    {
        
        // initialising splash window and setting its properties
        splashWindow = new JFrame();
        
        // setting window properties
        splashWindow.setTitle("Doggy Mania");
        splashWindow.setSize(300, 350);
        splashWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        splashWindow.setResizable(false);
        
        // creating the main container for all components
        JLayeredPane jlp = new JLayeredPane();
        jlp.setLayout(null);
        
        // getting background JLabel
        JLabel background = new JLabel(new ImageIcon(DoggyMania.class.getClassLoader().getResource("background.jpg")));
        
        // creating title JLabel and setting its properties
        JLabel title = new JLabel("Doggy Mania!");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(Color.WHITE);
        
        // creating user prompt
        JLabel userPrompt = new JLabel("Enter your name:");
        userPrompt.setFont(new Font("Arial", Font.BOLD, 14));
        userPrompt.setForeground(Color.WHITE);
        
        // getting username from player
        user = new JTextField(userName);
        
        // telling the user to select their difficulty
        JLabel diffPrompt = new JLabel("Select your difficulty:");
        diffPrompt.setFont(new Font("Arial", Font.BOLD, 14));
        diffPrompt.setForeground(Color.WHITE);
        
        // allowing player to select difficulty
        difficultySelect = new JComboBox(difficulties);
        difficultySelect.setSelectedIndex(1);
        
        // initialising start button
        start = new JButton("Start");
        
        // setting click action for start button
        start.addActionListener(new ActionListener()    {
            @Override
            
            public void actionPerformed(ActionEvent actionEvent)   {
                try {
                    
                    username = user.getText();
                    
                    diff = (String)difficultySelect.getSelectedItem();
                    
                    if (!username.equals(""))   {
                        
                        // starting a brand new game
                        playerScore = 0;
                        totalPlayerScore = 0;
                        
                        // closing the splash window
                        splashWindow.dispose();
                        
                        // initialising everything from scratch
                        arrayBuilder(lvl, diff);
                        mainGameScreen(lvl);
                        
                    } else  {
                        
                        // reminding user to enter their name if they haven't
                        JOptionPane.showMessageDialog(new JFrame(), "Please enter your name before starting the game.");
                        
                    }
                    
                } catch (IOException ex) {
                    ex.getMessage();
                } // end try/catch
                
            } // end actionPerformed method
            
        });
        
        // initialising instructionsScreenButton button
        instructionsScreenButton = new JButton("Rules");        
        instructionsScreenButton.addActionListener(new ActionListener()    {
            
            @Override
            public void actionPerformed(ActionEvent actionEvent)   {
                
                splashWindow.dispose();
                instructionsScreen(lvl, username);
                
            } // end actionPerformed method
            
        });
        
        // initialising load button
        load = new JButton("Load");
        load.addActionListener(new ActionListener()    {
            
            @Override
            public void actionPerformed(ActionEvent actionEvent)   {
                
                // getting username
                username = user.getText();
                
                if (!username.equals(""))   {
                    
                    SaveLoad saveLoad = new SaveLoad();
                    
                    // only loading the game if the player has entered a name
                    saveLoad.loadGame(username);
                    
                } else  {
                    
                    // reminding user to enter their name if they haven't
                    JOptionPane.showMessageDialog(new JFrame(), "Please enter your name before loading a game.");

                }// end if
                
            } // end actionPerformed method
            
        });
        
        // initialising quit button
        quit = new JButton("Quit");
        quit.addActionListener(new ActionListener()    {
            
            @Override
            
            public void actionPerformed(ActionEvent actionEvent)   {
                System.exit(0);
            } // end actionPerformed method
            
        });
        
        // adding background image to base layer
        jlp.add(background, JLayeredPane.DEFAULT_LAYER);
        background.setBounds(0, 0, 300, 350);
        
        // adding all other components to a layer above the background image
        jlp.add(title, JLayeredPane.DRAG_LAYER);
        title.setBounds(60, 0, 200, 60);
        
        jlp.add(userPrompt, JLayeredPane.DRAG_LAYER);
        userPrompt.setBounds(30, 50, 200, 70);
        
        jlp.add(user, JLayeredPane.DRAG_LAYER);
        user.setBounds(160, 70, 100, 30);
        
        jlp.add(diffPrompt, JLayeredPane.DRAG_LAYER);
        diffPrompt.setBounds(30, 100, 200, 70);
        
        jlp.add(difficultySelect, JLayeredPane.DRAG_LAYER);
        difficultySelect.setBounds(190, 125, 70, 20);
        
        jlp.add(start, JLayeredPane.DRAG_LAYER);
        start.setBounds(30, 180, 100, 40);
        
        jlp.add(instructionsScreenButton, JLayeredPane.DRAG_LAYER);
        instructionsScreenButton.setBounds(30, 240, 100, 40);
        
        jlp.add(load, JLayeredPane.DRAG_LAYER);
        load.setBounds(160, 180, 100, 40);
        
        jlp.add(quit, JLayeredPane.DRAG_LAYER);
        quit.setBounds(160, 240, 100, 40);
        
        // adding JLayeredPane to window
        splashWindow.add(jlp);
        
        // centering the window on the screen
        splashWindow.setLocationRelativeTo(null);
        
        // visibility stuff
        splashWindow.setVisible(true);
        splashWindow.validate();
        
        return splashWindow;
        
    } // end splashScreen method

    public static JFrame mainGameScreen(final int lvl) throws IOException  {
        
        // initialising game window and setting its properties
        gameWindow = new JFrame();
        
        // setting window properties
        gameWindow.setTitle("Doggy Mania");
        gameWindow.setSize(850, 850);
        gameWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // JPanel for adding game components
        gameCP = new JPanel();
        gameCP.setLayout(new BorderLayout());
        gameCP.setOpaque(false);
        
        // initialising quit JButton
        quit = new JButton("Quit");
        quit.setSize(50, 25);
        quit.addActionListener(new ActionListener()    {
            @Override
            public void actionPerformed(ActionEvent actionEvent)   {
                
                // closing the game window
                gameWindow.dispose();
                
                try {
                    // opening the splashscreen
                    splashScreen(lvl, username);
                } catch (IOException ex) {
                    ex.getMessage();
                }
                
            } // end actionPerformed method
        });
        
        // initialising save JButton
        save = new JButton("Save");
        save.setSize(50, 25);
        save.addActionListener(new ActionListener()    {
            @Override
            public void actionPerformed(ActionEvent actionEvent)   {
                
                //saving the game
                SaveLoad.saveGame(username, gameBoard, playerScore, level, diff);
                
            } // end actionPerformed method
        });
        
        autoComplete = new JButton("Auto Complete");
        autoComplete.setSize(50, 25);
        autoComplete.addActionListener(new ActionListener()    {
            @Override
            public void actionPerformed(ActionEvent actionEvent)   {
                
                try {
                    
                    AutoPlayer.autoPlayer(gameBoard);
                    
                } catch (IOException ex)    {
                    ex.getMessage();
                } // end try/catch
                
            } // end actionPerformed method
        });
        
        restartLevel = new JButton("Restart Level");
        restartLevel.setSize(50, 25);
        restartLevel.addActionListener(new ActionListener()    {
            @Override
            public void actionPerformed(ActionEvent actionEvent)   {
                
                try {
                    GameLogic.restartLevel();
                } catch (IOException ex) {
                    ex.getMessage();
                } // end try/catch
                
            } // end actionPerformed method
        });
        
        // initialising a JLabel to display the player's score and total score
        score = new JLabel("Score: " + playerScore);
        score.setFont(new Font("Arial", Font.BOLD, 14));
        score.setForeground(Color.WHITE);
        
        totalScore = new JLabel("Total Score: " + totalPlayerScore);
        totalScore.setFont(new Font("Arial", Font.BOLD, 14));
        totalScore.setForeground(Color.WHITE);
        
        // initialising a JLabel to display the remaining number of moves
        movesRemaining = new JLabel("Moves Left: " + numberOfMoves);
        movesRemaining.setFont(new Font("Arial", Font.BOLD, 14));
        movesRemaining.setForeground(Color.WHITE);
        
        // initialising a JLabel to display the required score to beat the level
        JLabel requiredScore = new JLabel("Required Score: " + reqScore);
        requiredScore.setFont(new Font("Arial", Font.BOLD, 14));
        requiredScore.setForeground(Color.WHITE);
        
        // initialising a JLabel to display player's current level
        JLabel lvlLabel = new JLabel("Level: " + lvl);
        lvlLabel.setFont(new Font("Arial", Font.BOLD, 14));
        lvlLabel.setForeground(Color.WHITE);
        
        // creating a new JPanel for JButtons and information
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(10, 1));
        sidePanel.setBackground(new Color(0, 0, 0, 64));
        
        // adding the game tiles to the main game screen
        gameCP.add(new DragFunctionality(gameBoard), BorderLayout.CENTER);
        
        // adding relevant objects to sidePanel
        sidePanel.add(lvlLabel);
        sidePanel.add(score);
        sidePanel.add(totalScore);
        sidePanel.add(movesRemaining);
        sidePanel.add(requiredScore);
        sidePanel.add(autoComplete);
        sidePanel.add(restartLevel);
        sidePanel.add(save);
        sidePanel.add(quit);
        
        // adding the side panel to main JPanel
        gameCP.add(sidePanel, BorderLayout.WEST);
        
        // using JLayeredPane to make adding a background easier
        JLayeredPane jlp = new JLayeredPane();
        jlp.setLayout(null);
        
        // background image
        JLabel background = new JLabel(new ImageIcon(DoggyMania.class.getClassLoader().getResource("background2.jpg")));
        
        // adding background and main JPanel at different layers
        jlp.add(background, JLayeredPane.DEFAULT_LAYER);
        jlp.add(gameCP, JLayeredPane.PALETTE_LAYER);
        
        // adding JLayeredPane to JFrame
        gameWindow.add(jlp);
        
        // visibility stuff
        jlp.setVisible(true);
        gameCP.setVisible(true);
        gameWindow.setVisible(true);
        gameWindow.validate();
        
        // setting bounds of background and gameCP
        background.setBounds(0, 0, gameWindow.getSize().width, gameWindow.getSize().height);
        gameCP.setBounds(0, 0, gameWindow.getSize().width, gameWindow.getSize().height);
        
        // catching any matches that happen immediately
        GameLogic.catchMatches(DoggyMania.gameBoard);
        
        return gameWindow;
        
    } // end mainGameScreen method
    
    public static JFrame instructionsScreen(final int level, final String username)   {
        
        // initialising splash window and setting its properties
        instructionsWindow = new JFrame();
        
        // setting window properties
        instructionsWindow.setTitle("Doggy Mania");
        instructionsWindow.setSize(300, 350);
        instructionsWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instructionsWindow.setResizable(false);
        
        String instructions = "<html><u>This game is designed to be a copy of Candy Crush</u><br><br>"
                + "- To form a chain, drag any tile <i>into</i> the chain being formed.<br>"
                + "- For a chain to be valid, it must be at least 3 tiles in length.<br>" 
                + "- Scores are caculated as a product of the chain's length with the tile's value.<br>"
                + "- Your aim is to reach the target score for each level without running out of moves by forming<br>"
                + "the highest scoring chain possible.</html>";
        
        // creating the main container for all components
        JLayeredPane jlp = new JLayeredPane();
        jlp.setLayout(null);
        
        // getting background JLabel
        JLabel background = new JLabel(new ImageIcon(DoggyMania.class.getClassLoader().getResource("background.jpg")));
        
        quit = new JButton("Back");
        quit.addActionListener(new ActionListener()    {
            @Override
            public void actionPerformed(ActionEvent actionEvent)   {
                
                try {
                    instructionsWindow.dispose();
                    splashScreen(level, username);
                } catch (IOException ex) {
                    ex.getMessage();
                } // end try/catch
                
            } // end actionPerformed method
        });
        
        // creating title JLabel and setting its properties
        JLabel title = new JLabel("Doggy Mania!");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(Color.WHITE);
        
        JLabel instructions1 = new JLabel(instructions);
        instructions1.setFont(new Font("Arial", Font.BOLD, 12));
        instructions1.setForeground(Color.WHITE);
        
        jlp.add(background, JLayeredPane.DEFAULT_LAYER);
        background.setBounds(0, 0, 300, 350);
        
        jlp.add(title, JLayeredPane.DRAG_LAYER);
        title.setBounds(60, 0, 200, 60);
        
        jlp.add(instructions1, JLayeredPane.DRAG_LAYER);
        instructions1.setBounds(0, 50, 300, 200);
        
        jlp.add(quit, JLayeredPane.DRAG_LAYER);
        quit.setBounds(100 ,250, 100, 40);
        
        instructionsWindow.add(jlp);
        
        instructionsWindow.setLocationRelativeTo(null);
        
        jlp.setVisible(true);
        instructionsWindow.setVisible(true);
        
        return instructionsWindow;
        
    } // end instructionsScreen method
    
    public static int difficultySelector(String difficulty)    {
        
        int toReturn = 0;
        
        switch (difficulty) {
            
            case "Easy":
                toReturn = 4;
                break;
                
            case "Normal":
                toReturn = 2;
                break;
                
            case "Hard":
                toReturn = 1;
                break;
            case "":
                toReturn = 1;
                break;
            
        } // end switch
        
        return toReturn;
        
    } // end difficultySelector method
    
    static String[][] arrayBuilder(int lvl, String difficulty) {
        
        int tmpDifficulty = difficultySelector(difficulty);
        
        // initialising level-specific variables based on value of lvl
        if (lvl <= 5) {
            
            gameBoard = new String[9][9];
            numberOfMoves = 40;
            
        } else if (lvl > 5 & lvl <= 7)   {
            
            gameBoard = new String[9][9];
            numberOfMoves = 35;
            
        } else if (lvl > 7 & lvl <= 10)   {
            
            gameBoard = new String[10][10];
            numberOfMoves = 35;
            
        } else if (lvl > 10 & lvl <= 15)   {
            
            gameBoard = new String[11][11];
            numberOfMoves = 30;
            
        } else if (lvl > 15 & lvl <= 17)   {
            
            gameBoard = new String[11][11];
            numberOfMoves = 30;
            
        } else if (lvl > 17)   {
            
            gameBoard = new String[9][9];
            numberOfMoves = 25;
            
        } // end if/else
        
        // setting a required score that ties in with the level that the player has reached
        reqScore = lvl * 2500;
        
        // for use in populating gameBoard
        Random r = new Random();
        
        // to avoid making repeated calculations
        int nextIntParam = dogArray.length / tmpDifficulty;
        
        for (int i = 0; i < gameBoard.length; i++)    {
            for (int j = 0; j < gameBoard.length; j++)  {
                
                // filling up the portion of the array that will have the game pieces
                if (i >= 2 & i <= gameBoard.length - 3 & j >= 2 & j <= gameBoard.length - 3)    {
                    
                    // populating each index in gameBoard with a random value from dogArray
                    // based on value of tmpDifficulty
                    gameBoard[i][j] = dogArray[r.nextInt((int)Math.floor(nextIntParam))];
                    
                } else  {
                    
                    // filling the two-wide border of unrelated strings in
                    gameBoard[i][j] = "0";
                    
                } // end if
                
            } // end for loop (columns)
        } // end for loop (rows)
        
        // returning the fully populated game-state data structure
        return gameBoard;
        
    } // end arrayBuilder method
    
    public static void repaintWindow() {
        
        // updating jlabels that change
        score.setText("Score: " + playerScore);
        movesRemaining.setText("Moves Left: " + DoggyMania.numberOfMoves);
        
        // getting the layout for removal of component 
        BorderLayout tmpLayout = (BorderLayout)gameCP.getLayout();
        gameCP.remove(tmpLayout.getLayoutComponent(BorderLayout.CENTER));
        
        // adding a freshly updated game board to the window
        gameCP.add(new DragFunctionality(gameBoard), BorderLayout.CENTER);
        
        // updating the graphics and revalidating the window
        gameCP.repaint();
        gameCP.revalidate();
        
    } // end repaintWindow
    
    @Override
    public void actionPerformed(ActionEvent e)  {} // end actionPerformed method
    
    public static void main(String[] args) throws IOException  {
        
        // initialising class variables to right values each time the program is run
        playerScore = 0;
        totalPlayerScore = 0;
        level = 1;
        
        diff = "Easy";
        
        // empty username for first time opening the game
        username = "";
        
        // initialising gameBoard and populating it
        arrayBuilder(level, diff);
        
        // displaying the splash screen
        splashScreen(level, username);
        
    } // end main method
    
} // end DoggyMania class