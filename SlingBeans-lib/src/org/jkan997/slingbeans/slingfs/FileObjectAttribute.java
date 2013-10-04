/**
 * SlingBeans - NetBeans Sling plugin https://github.com/jkan997/SlingBeans
 * Licensed under Apache 2.0 license http://www.apache.org/licenses/LICENSE-2.0
 */
package org.jkan997.slingbeans.slingfs;

import org.jkan997.slingbeans.helper.JcrTypeHelper;
import org.jkan997.slingbeans.helper.PropertyType;
import java.util.Date;
import org.jkan997.slingbeans.helper.ObjectHelper;
import org.json.ISO8601;
import org.json.JSONObject;

/**
 *
 * @author jkan997
 */
public class FileObjectAttribute {

    private Object convertedValue;
    private int type = PropertyType.UNDEFINED;
    private boolean modified;
    private boolean readOnly;
    private boolean hidden;

    public Object getValue() {
        return convertedValue;
    }

    public Date getDate() {
        return (Date) convertedValue;
    }

    public Long getLong() {
        return (Long) convertedValue;
    }

    public Double getDouble() {
        return (Double) convertedValue;
    }

    public Boolean getBoolean() {
        return (Boolean) convertedValue;
    }

    public void setValue(Object value) {
        this.convertedValue = value;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return PropertyType.nameFromValue(type);
    }

    public Class getTypeClass() {
        if (type == PropertyType.DATE) {
            return Date.class;
        } else if (type == PropertyType.LONG) {
            return Long.class;
        } else if (type == PropertyType.DOUBLE) {
            return Double.class;
        } else if (type == PropertyType.BOOLEAN) {
            return Boolean.class;
        }
        return String.class;

    }

    public void setJsonValue(JSONObject jsonObj, String propName) {
        Object value = jsonObj.get(propName);
        String name = jsonObj.getString(":" + propName);
        type = JcrTypeHelper.getType(name, value);
        readOnly = false;
        hidden = (propName.startsWith(":"));
        if (type == PropertyType.DATE) {
            convertedValue = ISO8601.parseToDate(value.toString());
        } else if (type == PropertyType.LONG) {
            convertedValue = ((Number) value).longValue();
        } else if (type == PropertyType.DOUBLE) {
            convertedValue = (Double) value;
        } else if (type == PropertyType.BOOLEAN) {
            convertedValue = (Boolean) value;
        } else if (type == PropertyType.STRING) {
            convertedValue = (String) value;
        } else if (value != null) {
            convertedValue = value.toString();
            readOnly = true;
        } else {
            convertedValue = "null";
            readOnly = true;
        }
    }

    public String getJsonValue() {
        if (type == PropertyType.DATE) {
            return ISO8601.format(getDate());
        } else if (type == PropertyType.LONG) {
            return getLong().toString();
        }
        if (type == PropertyType.DOUBLE) {
            return getDouble().toString().replace(",", ".");
        } else if (type == PropertyType.BOOLEAN) {
            return ((Boolean) convertedValue) == true ? "true" : "false";
        } else if (convertedValue != null) {
            return convertedValue.toString();
        }
        return null;
    }

    public boolean isModified() {
        return modified;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    @Override
    public boolean equals(Object obj) {
        FileObjectAttribute foa = (FileObjectAttribute) obj;
        return ((ObjectHelper.equalObjects(foa.getTypeClass(), this.getTypeClass()))
                && (ObjectHelper.equalObjects(foa.getValue(), this.getValue())));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.convertedValue != null ? this.convertedValue.hashCode() : 0);
        hash = 67 * hash + this.type;
        return hash;
    }

    @Override
    public String toString() {
        return "FileObjectAttribute{" + "value=" + convertedValue + ", type=" + type + '}';
    }
}
