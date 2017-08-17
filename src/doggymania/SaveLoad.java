package doggymania;

import java.io.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SaveLoad {
    
    public static void saveGame(String username, String[][] stringArgs, int score, int level, String difficulty)  {
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(username.concat(".dat")))) {
            
            // writing the stringArgs parameter
            oos.writeObject(stringArgs);
            oos.flush();
            
            // writing the score parameter
            oos.writeInt(score);
            oos.flush();

            // writing the level parameter
            oos.writeInt(level);
            oos.flush();
            
            // wriring the difficulty
            oos.writeUTF(difficulty);
            oos.flush();
            
            // closing the stream
            oos.close();
            
        } catch (IOException ex) {
            ex.getMessage();
        } // end try/catch
        
    } // end saveGame method
    
    public void loadGame(String username)    {
        
        String fileName = "./" + username + ".dat";
        
        // checking whether the file exists or not and that it's not a directory
        if (new File(fileName).exists() & !new File(fileName).isDirectory())   {
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName)))    {
                
                // reading gameBoard from the file
                DoggyMania.gameBoard = (String[][])ois.readObject();
                
                // reading the saved score from the file
                DoggyMania.playerScore = ois.readInt();
                
                System.out.print(DoggyMania.playerScore);
                
                // reading level object
                DoggyMania.level = ois.readInt();

                // reading difficulty
                DoggyMania.diff = ois.readUTF();
                
                // closing the stream
                ois.close();
                
                // closing the splashWindow
                DoggyMania.splashWindow.dispose();
                
                // starting the game
                DoggyMania.mainGameScreen(DoggyMania.level);

            } catch (IOException | ClassNotFoundException ex) {
                ex.getMessage();
            } // end try/catch
            
        } else  {
            
            // telling the user that their save can't be found
            JOptionPane.showMessageDialog(new JFrame(), "No save could be found that matches your username.\nPlease make sure it's spelled correctly.");
            
        } // end if/else
        
    } // end loadGame method
    
} // end SaveLoad class
