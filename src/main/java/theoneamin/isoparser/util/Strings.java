package theoneamin.isoparser.util;


import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.stream.StreamSource;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.springframework.stereotype.Service;


public class Strings {

    public static <I> String marshal(I object, String contentType) throws JAXBException {
        StringWriter writer = new StringWriter();
        JAXBContext jbc = JAXBContext.newInstance(object.getClass());
        Marshaller m = jbc.createMarshaller();
        m.setProperty(MarshallerProperties.MEDIA_TYPE, contentType);
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(object, writer);
        return writer.toString();
    }

    public static <E> E unmarshal(String object, Class<E> cls, String contentType) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(cls);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, contentType);
        StreamSource xml = new StreamSource(new StringReader(object));
        return unmarshaller.unmarshal(xml, cls).getValue();
    }

    public static String append(String value, String character, int expectedLength) {
        int length = value.length();
        for (int i = 0; i < expectedLength - length; i++) {
            value = value + character;
        }
        return value;
    }

    public static String prepend(String value, String character, int expectedLength) {
        int length = value.length();
        for (int i = 0; i < expectedLength - length; i++) {
            value = character + value;
        }
        return value;
    }

}