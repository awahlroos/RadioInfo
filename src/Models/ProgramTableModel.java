package Models;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * ProgramTableModel: Class to display the data in an AbstractTableModel allowing dynamic updates to the GUI.
 */
public class ProgramTableModel extends AbstractTableModel {

    private ArrayList<Program> programList;

    public ProgramTableModel() {
        programList = new ArrayList<>();
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
        return programList.size();
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
        return 3;
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
        Program program= programList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> program.getName();
            case 1 -> program.getStartTime();
            case 2 -> program.getEndTime();
            default -> null;
        };
    }

    public Program getProgramDetails(int rowIndex){
        return programList.get(rowIndex);
    }

    @Override
    public String getColumnName(int columnIndex) {

        return switch (columnIndex) {
            case 0 -> "Program";
            case 1 -> "Startar";
            case 2 -> "Slutar";
            default -> null;
        };
    }

    public void resetProgramList(){
        programList = new ArrayList<>();
    }

    public void setProgramList(ArrayList<Program> programList){
        this.programList = programList;
        this.fireTableDataChanged();
    }
}
