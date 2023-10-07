package theoneamin.isoparser.field;

import lombok.Data;
import theoneamin.isoparser.param.Encoding;
import theoneamin.isoparser.param.Padder;

@Data
public abstract class AbstractIsoField {
    private int fieldNumber;
    private int length;
    private Encoding valueEncoding;
    private Padder padder;
    private String value = "";
    private String encodedValue = "";
}
