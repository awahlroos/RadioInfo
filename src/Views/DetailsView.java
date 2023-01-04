package Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailsView {
    private final JPanel panel;
    public DetailsView(String name, String startTime, String endTime, String image, String description,
                       JFrame frameHolder, JFrame frame) throws MalformedURLException {
        JDialog dialog = new JDialog(frameHolder, "Mer information: " + name, true);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        addName(name);
        addTime(startTime, endTime);
        addImage(image);
        addDescription(description);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void addImage(String image) throws MalformedURLException {
        Image img;
        Image resizedImg;
        JLabel imageLabel = new JLabel();
        if (image == null) {
            URL imageUrl = this.getClass().getResource("/Assets/imageNotFound.png");
            img = new ImageIcon(imageUrl, "image").getImage();
        } else {
            img = new ImageIcon(new URL(image), "image").getImage();
        }
        resizedImg = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(new ImageIcon(resizedImg).getImage(), "image"));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        panel.add(imageLabel);
    }

    private void addDescription(String description){
        JTextArea descriptionJTA = new JTextArea(description);
        descriptionJTA.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionJTA.setEditable(false);
        descriptionJTA.setLineWrap(true);
        descriptionJTA.setWrapStyleWord(true);
        descriptionJTA.setOpaque(false);
        descriptionJTA.setSize(new Dimension(300, descriptionJTA.getPreferredSize().height));
        panel.add(descriptionJTA);
    }

    private void addName(String name){
        JLabel nameLabel = new JLabel(name);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        panel.add(nameLabel);
    }

    private void addTime(String startTime, String endTime){
        JLabel timeLabel = new JLabel(startTime + " - " + endTime);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(timeLabel);
    }
}
