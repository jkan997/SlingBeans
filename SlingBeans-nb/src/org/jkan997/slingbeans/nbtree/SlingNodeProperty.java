/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jkan997.slingbeans.nbtree;


import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.jkan997.slingbeans.slingfs.FileObject;
import org.jkan997.slingbeans.slingfs.FileObjectAttribute;
import org.openide.nodes.Node;

/**
 *
 * @author jakaniew
 */
public class SlingNodeProperty extends Node.Property {

    public final static int ATTR_CREATED = 1;
    public final static int ATTR_LAST_MODIFIED = 2;
    public final static int ATTR_SIZE = 3;
    private int specialAttr = -1;
    private Object specialValue = null;
    private String attrName;
    private FileObject fileObject = null;
    private Map<String, FileObjectAttribute> attributesMap;

    public static Set<String> readOnlyProps = new TreeSet<String>();
    
    static{
       readOnlyProps.add("jcr:created");
       readOnlyProps.add("jcr:createdBy");
    }
    
    public SlingNodeProperty(Class valueType) {
        super(valueType);
    }

    @Override
    public boolean canRead() {
        return true;
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        if (specialValue != null) {
            return specialValue;
        }
        if (attributesMap.containsKey(this.attrName)) {
            FileObjectAttribute foa = attributesMap.get(this.attrName);
            return foa.getValue();
        }
        return null;
    }

    @Override
    public boolean canWrite() {
        if (specialAttr > 0) {
            return false;
        }
        if (readOnlyProps.contains(attrName)){
            return false;
        }
        FileObjectAttribute foa = attributesMap.get(this.attrName);
        if ((foa != null) && (foa.isReadOnly())) {
            return false;
        }
        return true;
    }

    @Override
    public void setValue(Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        try {
            fileObject.setAttribute(attrName, value);
            fileObject.saveAttributes();
        } catch (Exception ex) {
            throw new RuntimeException();
        }

    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public void setFileObject(FileObject fileObject) {
        this.fileObject = fileObject;
        this.attributesMap = fileObject.getAttributesMap();
    }

    public void setSpecialAttr(int specialAttr, Object specialValue) {
        this.specialAttr = specialAttr;
        this.specialValue = specialValue;
    }
}
