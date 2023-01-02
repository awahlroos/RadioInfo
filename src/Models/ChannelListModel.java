package Models;

import javax.swing.*;
import java.util.ArrayList;

public class ChannelListModel extends AbstractListModel {

    private ArrayList<Channel> channelList;
    public ChannelListModel(){
        channelList = new ArrayList<>();
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Object getElementAt(int index) {
        return null;
    }

    public void addChannel(Channel c) {
        channelList.add(c);
        //this.fireTableRowsInserted(channelList.size()-1, channelList.size());
    }
}
