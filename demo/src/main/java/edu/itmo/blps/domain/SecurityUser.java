package edu.itmo.blps.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;
=======
>>>>>>> parent of 9c9ce79 (spring security finised)

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUser {
    private String username;
    private String password;
}
