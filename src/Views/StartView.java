package Views;

import Controllers.ChannelWorker;
import Controllers.ProgramWorker;
import Models.Channel;
import Models.GetChannels;
import Models.GetPrograms;
import Models.Program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class StartView {
    private final JFrame frame;
    private JButton button;

    private JPanel upperPanel;

    public StartView(String title) {
        //Create the top level frame/window
        frame=new JFrame(title);
        frame.setPreferredSize(new Dimension(800,600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Build panels
        upperPanel = new JPanel();

        //Add panels to the frame
        frame.add(upperPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    /*private JPanel buildUpperPanel(){
        //Create panel


        for(Channel c : channels){
            upperPanel.add(setButton( c.getName(),c.getImage()));
        }


        return upperPanel;
    }*/

    public void setButton(String name, String imgStr){
        button = new JButton(name);

        try{
            Image img = new ImageIcon(new URL(imgStr),"image").getImage();
            Image newImg = img.getScaledInstance(40,40, java.awt.Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(newImg));
        }
        catch (java.net.MalformedURLException e){

        }
        upperPanel.add(button);
    }
    public void setChannelButtonListener(ActionListener a){
        button.addActionListener(a);
    }
}
