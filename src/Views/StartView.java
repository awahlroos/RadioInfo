package Views;

import Models.ProgramTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class to display the GUI. Uses layout manager CardLayout to switch between channels and tableau.
 */
public class StartView {
    private final JFrame frame;
    private final JFrame frameHolder = new JFrame();
    private JButton button;
    private final JPanel channelPanel;
    private final JPanel tableauPanel;
    private final JMenuItem allChannels;
    private final JMenuItem updateChannels;
    private JTable tableauTable;
    private final CardLayout cl = new CardLayout();
    private final JPanel panelContent = new JPanel();

    public StartView(String title) {

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

        channelPanel = new JPanel();
        tableauPanel = new JPanel();

        panelContent.add(channelPanel, "channels");
        panelContent.add(tableauPanel, "tableau");
        cl.show(panelContent, "channels");

        frame.add(panelContent);
        frame.setPreferredSize(new Dimension(800,600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    public void setVisible(){
        frame.setVisible(true);
    }
    public void setShowChannelsPanel(){
        cl.show(panelContent, "channels");
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

        tableauTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        tableauTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        tableauTable.getColumnModel().getColumn(2).setPreferredWidth(30);
        cl.show(panelContent, "tableau");
    }

    /**
     * setButton(): Used for setting buttons with name and image of a fetched channel.
     */
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

    public JFrame getFrame(){
        return frame;
    }
}
