package Models;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ProgramTableModel extends AbstractTableModel {

    ArrayList<Channel> channelList;

    public ProgramTableModel() {
        channelList =new ArrayList<>();
    }

    public void addChannel(Channel c) {
        channelList.add(c);
        this.fireTableRowsInserted(channelList.size()-1, channelList.size());
    }

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    @Override
    public int getRowCount() {
        return 2;
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    @Override
    public int getColumnCount() {
        return channelList.size();
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Channel channel= channelList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> channel.getName();
            default -> null;
        };
    }
}
