package theoneamin.isoparser.field;

import lombok.Data;
import theoneamin.isoparser.exception.PackException;
import theoneamin.isoparser.implementation.Field;
import theoneamin.isoparser.param.Encoding;
import theoneamin.isoparser.param.Padder;
import theoneamin.isoparser.util.Converters;
import theoneamin.isoparser.util.Strings;

@Data
public class FixedNumField implements Field {
    private int id;
    private int length;
    private Encoding valueEncoding;
    private Padder padder;
    private String value = "";
    private String encodedValue = "";

    public FixedNumField(IsoField isoField) {
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
            case ZERO_PREPENDER:
            default:
                this.value = Strings.prepend(this.value, "0", this.length);
                break;
        }
        switch (this.valueEncoding) {
            case BCD:
                this.encodedValue = Converters.hexToAscii(this.value);
                break;
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
            case BCD:
                if (this.length % 2 != 0) {
                    nextHeadIndex = (this.length + 1) / 2;
                } else {
                    nextHeadIndex = this.length / 2;
                }
                this.encodedValue = head.substring(0, nextHeadIndex);
                this.value = Converters.asciiToHex(this.encodedValue);
                break;
            case ASC:
            default:
                nextHeadIndex = this.length;
                this.encodedValue = head.substring(0, nextHeadIndex);
                this.value = this.encodedValue;
                break;
        }
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
