package edu.itmo.blps.dao.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "company")
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Integer id;

	@Column(nullable = false,name="name")
	private String name;

	@Column(nullable = false,name="password")
	@JsonIgnore
	private String password;

	public Company(String name, String password) {
		this.name = name;
		this.password = password;
	}
}
