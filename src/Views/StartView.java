package Views;

import Models.ProgramTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class StartView {
    private final JFrame frame;
    private JFrame frameHolder = new JFrame();
    private JButton button;
    private JPanel channelPanel;
    private JPanel tableauPanel;
    private JMenuItem allChannels;
    private JMenuItem updateChannels;
    private JTable tableauTable;
    private String image;
    private String description;
    private String startTime;
    private String endTime;
    private String name;
    private CardLayout cl = new CardLayout();
    private JPanel panelContent = new JPanel();


    public StartView(String title) {
        //Create the top level frame/window
        frame=new JFrame(title);
        tableauTable = new JTable();


        JMenuBar menubar = new JMenuBar( );
        JMenu menu = new JMenu("Meny");
        allChannels = new JMenuItem("Alla kanaler");
        updateChannels = new JMenuItem("Uppdatera");


        menubar.add(menu);
        menu.add(allChannels);
        menu.add(updateChannels);

        frame.setJMenuBar(menubar);

        panelContent.setLayout(cl);
        // Build panels
        channelPanel = new JPanel();
        tableauPanel = new JPanel();

        panelContent.add(channelPanel, "channels");
        panelContent.add(tableauPanel, "tableau");
        cl.show(panelContent, "channels");
        //Add panels to the frame
        frame.add(panelContent);
        frame.setPreferredSize(new Dimension(800,600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private void buildTableauPanel(){

    }

    public void setShowChannelsPanel(){
        cl.show(panelContent, "channels");
    }

    public void setShowTableauPanel(String name, String image, ProgramTableModel tbl){

        this.image = image;
        this.description = description;

        tableauPanel.removeAll();
        tableauTable.setModel(tbl);


        //TODO: Kan detta lösas mha controller som de andra listeners

        /*tableauTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Klickad");
                int programRow = tableauTable.rowAtPoint(e.getPoint());
                showDetails(programRow);
            }
        });*/



        JScrollPane scrollPane = new JScrollPane(tableauTable);
        Label programName = new Label(name);

        JPanel container = new JPanel(new BorderLayout());


        programName.setFont(new Font("Tahoma", Font.BOLD, 24));

        container.add(programName, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        tableauPanel.add(container);

        cl.show(panelContent, "tableau");

    }

    public void setButton(String name, String imgStr){
        button = new JButton(name);
        try{
            Image img = new ImageIcon(new URL(imgStr),"image").getImage();
            Image newImg = img.getScaledInstance(40,40, java.awt.Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(newImg));
        }
        catch (java.net.MalformedURLException e){

        }
        channelPanel.add(button);
    }
    public void setChannelButtonListener(ActionListener a){
        button.addActionListener(a);
    }
    public void setAllChannelsButtonListener(ActionListener a){
        allChannels.addActionListener(a);
    }
    public void setUpdateChannelsButtonListener(ActionListener a){
        updateChannels.addActionListener(a);
    }


    //TODO: Får man göra detta här i view?
    public void setJTableOnClickListener(MouseAdapter adapter) {
        tableauTable.addMouseListener(adapter);
    }

    public void showDetails(String name, String startTime, String endTime, String image, String description)
            throws MalformedURLException {
        JDialog dialog = new JDialog(frameHolder, "Mer information: " + name);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD,16));
        panel.add(nameLabel);

        JLabel timeLabel = new JLabel(startTime + " - " + endTime);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(timeLabel);

        //TODO: Får man göra denna logik i View?
        Image img;
        Image resizedImg;
        JLabel imageLabel = new JLabel();
        if(image == null) {
            URL imageUrl = this.getClass().getResource("/Assets/imageNotFound.png");
            img = new ImageIcon(imageUrl, "image").getImage();
        } else {
            img = new ImageIcon(new URL(image), "image").getImage();
        }
        resizedImg = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(new ImageIcon(resizedImg).getImage(), "image"));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(imageLabel);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(descriptionLabel);

        dialog.add(panel);
        dialog.pack();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    public int getTableuRow(MouseEvent e){
        return tableauTable.rowAtPoint(e.getPoint());
    }


}
