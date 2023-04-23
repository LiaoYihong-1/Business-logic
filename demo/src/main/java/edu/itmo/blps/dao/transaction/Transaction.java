package edu.itmo.blps.dao.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.itmo.blps.dao.company.Company;
import edu.itmo.blps.dao.customer.User;
import edu.itmo.blps.dao.device.Device;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;

	@ManyToOne
	@JoinColumn(name = "seller_id")
	private Company seller;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private User user;

	@Column(nullable = false,name="amount")
	private Integer amount;

	@Column(nullable = false,name = "status")
	private String status;
}
