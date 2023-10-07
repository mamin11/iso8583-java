package theoneamin.isoparser.field;

import lombok.Data;
import theoneamin.isoparser.exception.PackException;
import theoneamin.isoparser.implementation.Field;
import theoneamin.isoparser.param.Encoding;
import theoneamin.isoparser.param.Padder;
import theoneamin.isoparser.util.Converters;
import theoneamin.isoparser.util.Strings;

import java.util.Objects;

import static theoneamin.isoparser.param.Padder.ZERO_PREPENDER;

@Data
public class FixedBinField implements Field {
    private int fieldNumber;
    private int length;
    private Encoding valueEncoding;
    private Padder padder;
    private String value = "";
    private String encodedValue = "";

    public FixedBinField(IsoField isoField) {
        this.fieldNumber = isoField.getFieldNumber();
        this.length = isoField.getLength();
        this.valueEncoding = isoField.getValueEncoding();
        this.padder = isoField.getPadder();
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
    public String encode() {
        if (this.value.length() == 0) {
            return this.encodedValue;
        }

        if (this.value.length() > this.length) {
            throw new PackException(String.format("Length of field %d (%d) is more than %d", this.fieldNumber, this.value.length(), this.length));
        }

        if (this.padder == ZERO_PREPENDER) {
            this.value = Strings.prepend(this.value, "0", this.length);
        }

        if (Objects.requireNonNull(this.valueEncoding) == Encoding.BCD) {
            this.encodedValue = Converters.binToAscii(this.value);
        } else {
            this.encodedValue = Converters.binToHex(this.value);
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
            nextHeadIndex = this.length / 8;
            this.encodedValue = head.substring(0, nextHeadIndex);
            this.value = Converters.asciiToBin(this.encodedValue);
        } else {
            nextHeadIndex = this.length / 4;
            this.encodedValue = head.substring(0, nextHeadIndex);
            this.value = Converters.hexToBin(this.encodedValue);
        }
        return nextHeadIndex;
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
