package org.jkan997.slingbeans.helper;

/**
 * The property types supported by the JCR standard.
 * <p>
 * This interface defines following property types: <ul> <li><code>STRING</code>
 * <li><code>BINARY</code> <li><code>LONG</code> <li><code>DOUBLE</code>
 * <li><code>DECIMAL</code> <li><code>DATE</code> <li><code>BOOLEAN</code>
 * <li><code>NAME</code> <li><code>PATH</code> <li><code>REFERENCE</code>
 * <li><code>WEAKREFERENCE</code> <li><code>URI</code> </ul>.
 */
public final class PropertyType {

    /*
     * The supported property types.
     */

    /**
     * The <code>STRING</code> property type is used to store strings. It has
     * the same characteristics as the Java <code>String</code> class.
     */
    public static final int STRING = 1;

    /**
     * <code>BINARY</code> properties are used to store binary data.
     */
    public static final int BINARY = 2;

    /**
     * The <code>LONG</code> property type is used to store integers. It has the
     * same characteristics as the Java primitive type <code>long</code>.
     */
    public static final int LONG = 3;

    /**
     * The <code>DOUBLE</code> property type is used to store floating point
     * numbers. It has the same characteristics as the Java primitive type
     * <code>double</code>.
     */
    public static final int DOUBLE = 4;

    /**
     * The <code>DATE</code> property type is used to store time and date
     * information.
     */
    public static final int DATE = 5;

    /**
     * The <code>BOOLEAN</code> property type is used to store boolean values.
     * It has the same characteristics as the Java primitive type
     * <code>boolean</code>.
     */
    public static final int BOOLEAN = 6;

    /**
     * A <code>NAME</code> is a pairing of a namespace and a local name. When
     * read, the namespace is mapped to the current prefix.
     */
    public static final int NAME = 7;

    /**
     * A <code>PATH</code> property is an ordered list of path elements. A path
     * element is a <code>NAME</code> with an optional index. When read, the
     * <code>NAME</code>s within the path are mapped to their current prefix. A
     * path may be absolute or relative.
     */
    public static final int PATH = 8;

    /**
     * A <code>REFERENCE</code> property stores the identifier of a
     * referenceable node (one having type <code>mix:referenceable</code>),
     * which must exist within the same workspace or session as the
     * <code>REFERENCE</code> property. A <code>REFERENCE</code> property
     * enforces this referential integrity by preventing the removal of its
     * target node.
     */
    public static final int REFERENCE = 9;

    /**
     * A <code>WEAKREFERENCE</code> property stores the identifier of a
     * referenceable node (one having type <code>mix:referenceable</code>). A
     * <code>WEAKREFERENCE</code> property does not enforce referential
     * integrity.
     *
     * @since JCR 2.0
     */
    public static final int WEAKREFERENCE = 10;

    /**
     * A <code>URI</code> property is identical to <code>STRING</code> property
     * except that it only accepts values that conform to the syntax of a
     * URI-reference as defined in RFC 3986.
     *
     * @since JCR 2.0
     */
    public static final int URI = 11;

    /**
     * The <code>DECIMAL</code> property type is used to store precise decimal
     * numbers. It has the same characteristics as the Java class
     * <code>java.math.BigDecimal</code>.
     *
     * @since JCR 2.0
     */
    public static final int DECIMAL = 12;

    /**
     * This constant can be used within a property definition (see <i>4.7.5
     * Property Definitions</i>) to specify that the property in question may be
     * of any type. However, it cannot be the actual type of any property
     * instance. For example, it will never be returned by {@link
     * Property#getType} and it cannot be assigned as the type when creating a
     * new property.
     */
    public static final int UNDEFINED = 0;

    /**
     * String constant for type name as used in serialization.
     */
    public static final String TYPENAME_STRING = "String";

    /**
     * String constant for type name as used in serialization.
     */
    public static final String TYPENAME_BINARY = "Binary";

    /**
     * String constant for type name as used in serialization.
     */
    public static final String TYPENAME_LONG = "Long";

    /**
     * String constant for type name as used in serialization.
     */
    public static final String TYPENAME_DOUBLE = "Double";

    /**
     * String constant for type name as used in serialization.
     *
     * @since JCR 2.0
     */
    public static final String TYPENAME_DECIMAL = "Decimal";

    /**
     * String constant for type name as used in serialization.
     */
    public static final String TYPENAME_DATE = "Date";

    /**
     * String constant for type name as used in serialization.
     */
    public static final String TYPENAME_BOOLEAN = "Boolean";

    /**
     * String constant for type name as used in serialization.
     */
    public static final String TYPENAME_NAME = "Name";

    /**
     * String constant for type name as used in serialization.
     */
    public static final String TYPENAME_PATH = "Path";

    /**
     * String constant for type name as used in serialization.
     */
    public static final String TYPENAME_REFERENCE = "Reference";

    /**
     * String constant for type name as used in serialization.
     *
     * @since JCR 2.0
     */
    public static final String TYPENAME_WEAKREFERENCE = "WeakReference";

    /**
     * String constant for type name as used in serialization.
     *
     * @since JCR 2.0
     */
    public static final String TYPENAME_URI = "URI";

    /*
     * String constant for type name as used in serialization.
     */
    public static final String TYPENAME_UNDEFINED = "undefined";

    /**
     * Returns the name of the specified <code>type</code>, as used in
     * serialization.
     *
     * @param type the property type
     * @return the name of the specified <code>type</code>
     * @throws IllegalArgumentException if <code>type</code> is not a valid
     *                                  property type.
     */
    public static String nameFromValue(int type) {
        switch (type) {
            case STRING:
                return TYPENAME_STRING;
            case BINARY:
                return TYPENAME_BINARY;
            case BOOLEAN:
                return TYPENAME_BOOLEAN;
            case LONG:
                return TYPENAME_LONG;
            case DOUBLE:
                return TYPENAME_DOUBLE;
            case DECIMAL:
                return TYPENAME_DECIMAL;
            case DATE:
                return TYPENAME_DATE;
            case NAME:
                return TYPENAME_NAME;
            case PATH:
                return TYPENAME_PATH;
            case REFERENCE:
                return TYPENAME_REFERENCE;
            case WEAKREFERENCE:
                return TYPENAME_WEAKREFERENCE;
            case URI:
                return TYPENAME_URI;
            case UNDEFINED:
                return TYPENAME_UNDEFINED;
            default:
                throw new IllegalArgumentException("unknown type: " + type);
        }
    }

    /**
     * Returns the numeric constant value of the type with the specified name.
     *
     * @param name the name of the property type.
     * @return the numeric constant value.
     * @throws IllegalArgumentException if <code>name</code> is not a valid
     *                                  property type name.
     */
    public static int valueFromName(String name) {
        if (name.equals(TYPENAME_STRING)) {
            return STRING;
        } else if (name.equals(TYPENAME_BINARY)) {
            return BINARY;
        } else if (name.equals(TYPENAME_BOOLEAN)) {
            return BOOLEAN;
        } else if (name.equals(TYPENAME_LONG)) {
            return LONG;
        } else if (name.equals(TYPENAME_DOUBLE)) {
            return DOUBLE;
        } else if (name.equals(TYPENAME_DECIMAL)) {
            return DECIMAL;
        } else if (name.equals(TYPENAME_DATE)) {
            return DATE;
        } else if (name.equals(TYPENAME_NAME)) {
            return NAME;
        } else if (name.equals(TYPENAME_PATH)) {
            return PATH;
        } else if (name.equals(TYPENAME_REFERENCE)) {
            return REFERENCE;
        } else if (name.equals(TYPENAME_WEAKREFERENCE)) {
            return WEAKREFERENCE;
        } else if (name.equals(TYPENAME_URI)) {
            return URI;
        } else if (name.equals(TYPENAME_UNDEFINED)) {
            return UNDEFINED;
        } else {
            throw new IllegalArgumentException("unknown type: " + name);
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private PropertyType() {
    }
}