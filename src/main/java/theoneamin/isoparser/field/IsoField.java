package theoneamin.isoparser.field;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import theoneamin.isoparser.param.Encoding;
import theoneamin.isoparser.param.Padder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "isoField")
@XmlAccessorType(XmlAccessType.FIELD)
public class IsoField {
    private int fieldNumber; // eg 0 for MTI
    private int length; // length of the field eg 4 for MTI
    private int maxLength; // max length of the field
    private String className; // class name of the field
    private Padder padder; // padding type
    private String description; // description of the field
    private Encoding valueEncoding; // encoding of the value
    private Encoding lengthEncoding; // encoding of the length
    @XmlElement(name = "isoField")
    private List<IsoField> isoFields; // sub-fields
}
