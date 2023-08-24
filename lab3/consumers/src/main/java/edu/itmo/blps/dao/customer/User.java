package edu.itmo.blps.dao.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.itmo.blps.dao.cart.Cart;
import edu.itmo.blps.dao.message.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Integer id;

	@Column(nullable = false, name="username", length = 20)
	private String username;

	@Column(nullable = false,name="password")
	private String password;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@OneToMany(targetEntity = Cart.class, cascade=CascadeType.ALL)
	@JoinColumn(name="customerid")
	List<Cart> myCarts;

	@OneToMany(targetEntity = Message.class, cascade=CascadeType.ALL)
	@JoinColumn(name="customer")
	List<Message> myMessages;
}
