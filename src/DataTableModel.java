
import javax.swing.table.AbstractTableModel;
import java.util.Map;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author ahmed
 */
public final class DataTableModel extends AbstractTableModel {

    private final String[] columns;
    private final Map<Integer, Object[]> data;

    public DataTableModel(String[] columns, Map<Integer, Object[]> data) {
        this.columns = columns;
        this.data = data;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }
}
