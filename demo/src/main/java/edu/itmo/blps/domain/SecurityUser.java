package edu.itmo.blps.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement( name = "User" )
@XmlType(propOrder = { "id", "username", "password","type" })
public class SecurityUser {
    private String username;
    private String password;
    private Integer id;
    private String type;
    @XmlElement(name="id")
    public void setId(Integer id) {
        this.id = id;
    }
    @XmlElement(name="password")
    public void setPassword(String password) {
        this.password = password;
    }
    @XmlElement(name="type")
    public void setType(String type) {
        this.type = type;
    }
    @XmlElement(name="username")
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecurityUser that = (SecurityUser) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(id, that.id) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, id, type);
    }
}
