package utils;

import edu.itmo.blps.domain.SecurityUser;
import edu.itmo.blps.domain.SecurityUserSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class XmlUtils {
    private final static String path = "src/main/resources/MySecurityUsers.xml";
    public static void createUser(SecurityUser user) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(SecurityUser.class);
        Marshaller mar= context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(user, new File(path));
    }

    public static void createUsers(SecurityUserSet list) throws JAXBException,IOException{
        JAXBContext context = JAXBContext.newInstance(SecurityUserSet.class);
        Marshaller mar= context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(list, new File(path));
    }
    public static SecurityUserSet unmarshall() throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(SecurityUserSet.class);
        return (SecurityUserSet) context.createUnmarshaller()
                .unmarshal(new FileReader(path));
    }
}
