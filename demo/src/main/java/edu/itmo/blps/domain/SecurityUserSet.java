package edu.itmo.blps.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;

@XmlRootElement(name="Users")
@Component
@Data
public class SecurityUserSet {

    private HashSet<SecurityUser> users = new HashSet<>();

    @XmlElement(name = "User")
    public void setUsers(HashSet<SecurityUser> users) {
        this.users = users;
    }
}
