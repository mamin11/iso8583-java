package theoneamin.isoparser.implementation;

public interface Field {
    void setValue(String value);

    String getValue();

    String encode();

    int decode(String head);

    void setEncodedValue(String encodedValue);

    String getEncodedValue();
}
