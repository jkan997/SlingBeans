package org.jkan997.slingbeans.nbpropertyeditor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.jkan997.slingbeans.helper.LogHelper;
import org.jkan997.slingbeans.helper.PropertyType;
import org.jkan997.slingbeans.nbprojects.maven.LocalSlingNodeFactory;
import org.jkan997.slingbeans.slingfs.FileObjectAttribute;
import org.jkan997.slingbeans.slingfs.SlingFileObject;
import org.jkan997.slingbeans.slingfs.local.LocalFileObject;

/**
 *
 * @author jakaniew
 */
public class PropertyTableModel extends AbstractTableModel {

    private SlingFileObject slingFileObject;
    private String[] attributeNames;
    private FileObjectAttribute[] attributes;

    private final static Comparator propertyNameComparator = new Comparator() {

        @Override
        public int compare(Object o1, Object o2) {
            FileObjectAttribute foa1 = (FileObjectAttribute) o1;
            FileObjectAttribute foa2 = (FileObjectAttribute) o2;
            if ((foa1 == null) && (foa2 == null)) {
                return 0;
            }
            if ((foa1 != null) && (foa2 == null)) {
                return 1;
            }
            if ((foa1 == null) && (foa2 != null)) {
                return -1;
            }
            return foa1.getPropertyName().compareTo(foa2.getPropertyName());
        }
    };

    public PropertyTableModel() {
        setEmpty();
    }

    public void setFileObject(SlingFileObject slingFileObject) {
        this.slingFileObject = slingFileObject;
        refresh();
    }

    public void refresh() {
        setEmpty();
        if (slingFileObject != null) {
            Map<String, FileObjectAttribute> attributesMap = slingFileObject.getAttributesMap();
            FileObjectAttribute foa;
            if (attributesMap.size() > 0) {
                List<String> attributeNamesList = new ArrayList<String>();
                List<FileObjectAttribute> attributesList = new ArrayList<FileObjectAttribute>();
                for (Map.Entry<String, FileObjectAttribute> me : attributesMap.entrySet()) {
                    String name = me.getKey();
                    foa = me.getValue();
                    if (!foa.isHidden()) {
                        attributesList.add(foa);
                    }
                }
                Collections.sort(attributesList, propertyNameComparator);
                attributes = attributesList.toArray(new FileObjectAttribute[]{});
                attributeNames = new String[attributes.length];
                for (int i = 0; i < attributes.length; i++) {
                    foa = attributes[i];
                    attributeNames[i] = foa.getPropertyName();
                }
            }

        }
        this.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return attributes.length;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        FileObjectAttribute foa = attributes[rowIndex];
        switch (columnIndex) {
            case 0: {
                return foa.getPropertyName();
            }
            case 1: {
                return foa.getValue();
            }
            case 2: {
                return PropertyType.nameFromValue(foa.getType());
            }
             case 3: {
                return foa.isReadOnly()?"Yes":"No";
            }

        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            try {
                FileObjectAttribute foa = attributes[rowIndex];
                foa.setValue(aValue);
                slingFileObject.setAttribute(foa.getPropertyName(), foa.getValue(), foa.getType());
                slingFileObject.saveAttributes();
                saveFileObject();
            } catch (IOException ex) {
                LogHelper.logError(ex);
            }
        }
    }
    
   

    public FileObjectAttribute getAttribute(int rowIndex) {
        return attributes[rowIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            FileObjectAttribute foa = getAttribute(rowIndex);
            String propName = foa.getPropertyName();
            return !(foa.isReadOnly());
        }
        return false;
    }

    private final void setEmpty() {
        this.attributeNames = new String[]{};
        this.attributes = new FileObjectAttribute[]{};
    }
    
    private void saveFileObjectAsync(){
        
    }

    private void saveFileObject() {
        if (slingFileObject instanceof LocalFileObject) {
            LogHelper.logInfo(this, "Saving object via VLT");
            LocalSlingNodeFactory.instance.export(slingFileObject.getFilePath());
        }
    }
}
