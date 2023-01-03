package Views;

import Models.ProgramTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;

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

    private boolean channelsViewActive;
    private Cursor c = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

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
        channelsViewActive = true;
    }

    public void setVisible(){
        frame.setVisible(true);
    }
    public void setShowChannelsPanel(){
        cl.show(panelContent, "channels");
        channelsViewActive = true;
    }

    public void setShowTableauPanel(String name, ProgramTableModel tbl){

        tableauPanel.removeAll();
        tableauTable = new JTable();
        panelContent.add(tableauPanel, "tableau");
        tableauTable.setModel(tbl);

        JScrollPane scrollPane = new JScrollPane(tableauTable);
        Label programName = new Label(name);

        JPanel container = new JPanel(new BorderLayout());

        programName.setFont(new Font("Tahoma", Font.BOLD, 24));

        container.add(programName, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        tableauPanel.add(container);

        cl.show(panelContent, "tableau");
        channelsViewActive = false;
    }

    public void setButton(String name, String imgStr){
        button = new JButton(name);
        Image img;
        Image resizedImg;
        try{
            if(imgStr == null) {
                URL imageUrl = this.getClass().getResource("/Assets/imageNotFound.png");
                img = new ImageIcon(imageUrl, "image").getImage();
            } else {
                img = new ImageIcon(new URL(imgStr), "image").getImage();
            }
            resizedImg = img.getScaledInstance(40,40, java.awt.Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(resizedImg));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        channelPanel.add(button);
        frame.revalidate();
        //frame.repaint();
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

    public void setJTableOnClickListener(MouseAdapter adapter) {
        tableauTable.addMouseListener(adapter);
    }

    public int getTableauRow(MouseEvent e){
        return tableauTable.rowAtPoint(e.getPoint());
    }

    public void showErrorDialog(String message){
        JOptionPane.showMessageDialog(null ,message,"Error",JOptionPane.ERROR_MESSAGE);
    }

    public JFrame getFrameHolder(){
        return frameHolder;
    }

    /*public void setCursor(Cursor c){
        this.c = c;
    }*/

    public JFrame getFrame(){
        return frame;
    }
}
