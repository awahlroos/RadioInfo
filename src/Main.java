import Controllers.ChannelWorker;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        ChannelWorker chWorker = new ChannelWorker();
        //chWorker.execute();
        SwingUtilities.invokeLater(chWorker);
    }
}