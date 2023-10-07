package theoneamin.isoparser.field;

import lombok.Data;
import theoneamin.isoparser.exception.PackException;
import theoneamin.isoparser.implementation.Field;
import theoneamin.isoparser.param.Encoding;
import theoneamin.isoparser.param.Padder;
import theoneamin.isoparser.util.Strings;

/**
 * Ans fields eg
 * 54 -> additional amounts
 * 55 -> ICC data
 *
 * This is always encoded in ascii
 */
@Data
public class FixedAnsField implements Field {
    private int id;
    private int length;
    private Encoding valueEncoding;
    private Padder padder;
    private String value = "";
    private String encodedValue = "";

    public FixedAnsField(IsoField isoField) {
        this.id = isoField.getFieldNumber();
        this.length = isoField.getLength();
        this.valueEncoding = isoField.getValueEncoding();
        this.padder = isoField.getPadder();
    }

    @Override
    public String encode() {
        if (this.value.length() == 0) {
            return this.encodedValue;
        }
        if (this.value.length() > this.length) {
            throw new PackException(String.format("Length of field %d (%s) is more than %d", this.id, this.value.length(), this.length));
        }

        switch (this.padder) {
            case SPACE_APPENDER:
            default:
                this.value = Strings.append(this.value, " ", this.length);
                break;
        }

        switch (this.valueEncoding) {
            case ASC:
            default:
                this.encodedValue = this.value;
                break;
        }
        return this.encodedValue;
    }

    @Override
    public int decode(String head) {
        if (this.length == 0) {
            return 0;
        }

        int nextHeadIndex;

        switch (this.valueEncoding) {
            case ASC:
            default:
                nextHeadIndex = this.length;
                this.encodedValue = head.substring(0, nextHeadIndex);
                break;
        }
        this.value = head.substring(0, this.length);
        return nextHeadIndex;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getEncodedValue() {
        return this.encodedValue;
    }

    @Override
    public void setEncodedValue(String encodedValue) {
        this.encodedValue = encodedValue;
    }
}
