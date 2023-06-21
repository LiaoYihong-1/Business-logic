package edu.itmo.blps.dao.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.itmo.blps.dao.company.Company;
import edu.itmo.blps.dao.customer.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(nullable = false,name="text")
    private String text;
    @Column(nullable = false,name="customer")
    private Integer customer;
    @Column(nullable = false,name="life")
    private Integer life;
}
