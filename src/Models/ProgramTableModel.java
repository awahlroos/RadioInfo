package Models;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ProgramTableModel extends AbstractTableModel {

    ArrayList<Program> programList;

    public ProgramTableModel() {
        programList =new ArrayList<>();
    }

    public void addProgram(Program p) {
        programList.add(p);
        this.fireTableRowsInserted(programList.size()-1, programList.size());
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
        System.out.println(rowIndex);
        System.out.println(programList.size());
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
}
