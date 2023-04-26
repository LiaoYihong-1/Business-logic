package edu.itmo.blps.dao.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Integer id;

	@Column(nullable = false, name="name", length = 20)
	private String username;

	@Column(nullable = false,name="password")
	private String password;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User(Integer id) {
		this.id = id;
	}
}
