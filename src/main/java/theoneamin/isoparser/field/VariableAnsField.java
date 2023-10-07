package theoneamin.isoparser.field;

import lombok.Data;
import theoneamin.isoparser.exception.PackException;
import theoneamin.isoparser.exception.UnPackException;
import theoneamin.isoparser.implementation.Field;
import theoneamin.isoparser.param.Encoding;
import theoneamin.isoparser.util.Converters;
import theoneamin.isoparser.util.Strings;

@Data
public class VariableAnsField implements Field {
    private int id;
    private int length;
    private int maxLength;
    private int lengthOfLength;
    private Encoding lengthEncoding;
    private Encoding valueEncoding;
    private String value = "";
    private String encodedValue = "";

    public VariableAnsField(IsoField isoField) {
        this.id = isoField.getFieldNumber();
        this.length = isoField.getLength();
        this.maxLength = isoField.getMaxLength();
        this.lengthOfLength = String.valueOf(this.maxLength).length();
        this.lengthEncoding = isoField.getLengthEncoding();
        this.valueEncoding = isoField.getValueEncoding();
    }

    @Override
    public String encode() {
        if (this.value.length() == 0) {
            return this.encodedValue;
        }
        if (this.value.length() > this.maxLength) {
            throw new PackException(String.format("Length of field %d (%s) is more than %d", this.id, this.value.length(), this.maxLength));
        }
        this.length = this.value.length();
        String encodedLength;
        encodedLength = String.valueOf(this.length);
        encodedLength = Strings.prepend(encodedLength, "0", this.lengthOfLength);

        switch (this.lengthEncoding) {
            case BCD:
                encodedLength = Converters.hexToAscii(encodedLength);
                break;
            case ASC:
            default:
                break;
        }

        switch (this.valueEncoding) {
            case ASC:
            default:
                this.encodedValue = this.value;
                break;
        }
        this.encodedValue = encodedLength + this.encodedValue;
        return this.encodedValue;
    }

    @Override
    public int decode(String head) {
        int nextValueIndex;
        switch (this.lengthEncoding) {
            case BCD:
                if (this.lengthOfLength % 2 != 0) {
                    nextValueIndex = (this.lengthOfLength + 1) / 2;
                } else {
                    nextValueIndex = this.lengthOfLength / 2;
                }
                this.length = Integer.parseInt(Converters.asciiToHex(head.substring(0, nextValueIndex)));
                break;
            case ASC:
            default:
                nextValueIndex = this.lengthOfLength;
                this.length = Integer.parseInt(head.substring(0, nextValueIndex));
                break;
        }
        if (this.length > this.maxLength) {
            throw new UnPackException(String.format("Length of field %d (%d) is more than %d", this.id, this.length, this.maxLength));
        }

        int nextHeadIndex;

        switch (this.valueEncoding) {
            case ASC:
            default:
                nextHeadIndex = nextValueIndex + this.length;
                this.encodedValue = head.substring(nextValueIndex, nextHeadIndex);
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
