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

	@Column(nullable = false,name="device")
	private Integer deviceId;

	@OneToOne
	@JoinColumn(name = "device",insertable = false, updatable = false)
	private Device device;

	@OneToOne
	@JoinColumn(name = "seller")
	private Company seller;

	@OneToOne
	@JoinColumn(name = "customer")
	@JsonIgnore
	private User user;

	@Column(nullable = false,name="amount")
	private Integer amount;

	@Column(nullable = false,name = "status")
	private String status;

	@Override
	public String toString(){
		return "user: " + this.user.getUsername() + "\n" +
				"device: " + this.device.getName() + "\n" +
				"amount: " + this.amount + "\n" +
				"status: " + this.status + "\n";
	}
}
