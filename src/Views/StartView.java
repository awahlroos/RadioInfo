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
    private ArrayList<Channel> channels;
    private JButton button;

    public StartView(String title, ArrayList<Channel> channels) {
        this.channels = channels;
        //Create the top level frame/window
        frame=new JFrame(title);
        frame.setPreferredSize(new Dimension(800,600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Build panels
        JPanel upperPanel = buildUpperPanel();

        //Add panels to the frame
        frame.add(upperPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    private JPanel buildUpperPanel(){
        //Create panel
        JPanel upperPanel = new JPanel();

        for (Channel c : channels) {

            button = new JButton(c.getName());
            try{
                Image img = new ImageIcon(new URL(c.getImage()),"image").getImage();
                Image newImg = img.getScaledInstance(40,40, java.awt.Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(newImg));
            }
            catch (java.net.MalformedURLException e){

            }

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ProgramWorker worker = new ProgramWorker(c);
                    worker.execute();
                }
            });
            upperPanel.add(button);
        }

        return upperPanel;
    }

    public void setChannelButtonListener(ActionListener a){
        button.addActionListener(a);
    }
}
