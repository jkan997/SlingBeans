package org.jkan997.slingbeans.nbpropertyeditor;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.jkan997.slingbeans.components.valueeditor.ValueEditorContainer;
import org.jkan997.slingbeans.helper.PropertyType;
import org.jkan997.slingbeans.slingfs.FileObjectAttribute;

public class PropertyTableCellEditor extends AbstractCellEditor
        implements TableCellEditor {

    private ValueEditorContainer valueEditorContainer;

    @Override
    public Object getCellEditorValue() {
        return valueEditorContainer.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        PropertyTableModel tableModel = (PropertyTableModel) table.getModel();
        valueEditorContainer = new ValueEditorContainer();
        valueEditorContainer.hideBorders();
        FileObjectAttribute foa = tableModel.getAttribute(row);
        valueEditorContainer.setTypeAndValue(foa.getType(), foa.getValue());
        return valueEditorContainer;
    }

}
