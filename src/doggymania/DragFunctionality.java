package doggymania;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

class DragFunctionality extends JLayeredPane {
    
    // declaring class variables
    private JLabel label = null;
    GridBagConstraints gbc = new GridBagConstraints();

    public DragFunctionality(String[][] stringArgs) {
        
        // setting the layout
        setLayout(new GridBagLayout());
        
        // setting the padding
        gbc.ipadx = 10;
        gbc.ipady = 10;
        
        // adding images to JLabels and associating a mouse listener and mouse motion listener to them
        for (int i = 2; i <= stringArgs.length - 3; i++)   {
            
            for (int j = 2; j <= stringArgs.length - 3; j++)   {
                
                // loading the image into an icon as a package resource
                ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(stringArgs[i][j]));
                
                // creating the JLabel
                label = new JLabel(icon);
                
                // setting label properties
                label.setBounds(0,0,icon.getIconWidth(), icon.getIconHeight());
                setBounds(0,0,icon.getIconWidth(), icon.getIconHeight());
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.CENTER);
                
                // positioning the label in right place on grid while compensating for border around gameBoard
                gbc.gridx = j - 2;
                gbc.gridy = i - 2;
                
                // adding label to panel
                add(label, gbc, JLayeredPane.DEFAULT_LAYER);
                
                // initialising MouseHandler class separately for each label object
                MouseHandler handler = new MouseHandler();
                
                // adding class instances to label object
                label.addMouseListener((MouseListener) handler);
                label.addMouseMotionListener((MouseMotionListener) handler);
                
            } // end inner for            
        } // end outer for

    } // end DragFunctionality constructor

} // end DragFunctionality class