package edu.itmo.blps.dao.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.itmo.blps.dao.customer.User;
import edu.itmo.blps.dao.device.Device;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "cart")
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Integer id;
	@Column(nullable = false,name="customerid")
	private Integer customer;
	@Column(nullable = false,name="deviceid")
	private Integer device;
}
