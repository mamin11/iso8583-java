package theoneamin.isoparser.field;

import lombok.Data;
import theoneamin.isoparser.exception.PackException;
import theoneamin.isoparser.implementation.Field;
import theoneamin.isoparser.param.Encoding;
import theoneamin.isoparser.param.Padder;
import theoneamin.isoparser.util.Converters;
import theoneamin.isoparser.util.Strings;

import java.util.Objects;

@Data
public class FixedLengthField implements Field {
    private int fieldNumber;
    private int length;
    private Encoding valueEncoding;
    private Padder padder;
    private String value = "";
    private String encodedValue = "";

    public FixedLengthField(IsoField isoField) {
        this.fieldNumber = isoField.getFieldNumber();
        this.length = isoField.getLength();
        this.valueEncoding = isoField.getValueEncoding();
        this.padder = isoField.getPadder();
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String encode() {
        if (this.value.length() == 0) {
            return this.encodedValue;
        }

        if (this.value.length() > this.length) {
            throw new PackException(String.format("Value length %s is greater than field length %s", this.value.length(), this.length));
        }

        this.value = Strings.prepend(this.value, "0", this.length);

        if (Objects.requireNonNull(this.valueEncoding) == Encoding.BCD) {
            this.encodedValue = Converters.hexToAscii(this.value);
        } else {
            this.encodedValue = this.value;
        }

        return this.encodedValue;
    }

    @Override
    public int decode(String head) {
        if (this.length == 0) {
            return 0;
        }

        int nextHeadIndex;

        if (Objects.requireNonNull(this.valueEncoding) == Encoding.BCD) {
            if (this.length % 2 != 0) {
                nextHeadIndex = (this.length + 1) / 2;
            } else {
                nextHeadIndex = this.length / 2;
            }
            this.encodedValue = head.substring(0, nextHeadIndex);
            this.value = Converters.asciiToHex(this.encodedValue);
        } else {
            nextHeadIndex = this.length;
            this.encodedValue = head.substring(0, nextHeadIndex);
            this.value = this.encodedValue;
        }
        return nextHeadIndex;
    }

    @Override
    public void setEncodedValue(String encodedValue) {
        this.encodedValue = encodedValue;
    }

    @Override
    public String getEncodedValue() {
        return this.encodedValue;
    }
}
