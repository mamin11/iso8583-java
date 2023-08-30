package theoneamin.isoparser.implementation;

public interface CompoundField extends Field {

    /**
     * Set value of a field using field id
     * eg to set MTI value, use field id 0
     * @param fieldId field id
     * @param value value to set
     */
    void setValue(int fieldId, String value);

    String getValue(int fieldId);

    /**
     * Set value of sub-field of a field
     * @param fieldId field id
     * @param subFieldId sub-field id
     * @param value value to set
     */
    void setValue(int fieldId, int subFieldId, String value);

    String getValue(int fieldId, int subFieldId);

    /**
     * Set a field by passing its id and field object
     * eg to set MTI value, use field id 0
     * @param fieldId field id
     * @param field Field to set
     */
    void setField(int fieldId, Field field);

    Field getField(int fieldId);

    void setField(int fieldId, int subFieldId, Field subField);

    Field getField(int fieldId, int subFieldId);

}
