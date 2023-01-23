import Controllers.ChannelWorker;

import javax.swing.*;


/**
 * Author: Alex Wahlroos - oi19aws
 * Version information:
 * 2023-01-04: v1.0. First version.
 * 2023-01-23: v2.0. Second version.
 *
 * RadioInfo is a program built with Swing that presents radio information from the SR API.
 * The program provides information about radio channels and tableau for each channel.
 * New data is fetched every hour or when the user manually updates the program.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChannelWorker::new);
    }
}