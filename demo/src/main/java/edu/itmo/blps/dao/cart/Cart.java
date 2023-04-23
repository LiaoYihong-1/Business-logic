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

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable(name = "devices_in_cart",
			joinColumns = @JoinColumn(name = "cart_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "device_id", referencedColumnName = "id"))
	private List<Device> devices;

	Cart setUserId(Integer id) {
		user = new User(id);
		return this;
	}

	public void addDevice(Device device) {
		if (Objects.isNull(devices))
			devices = new ArrayList<>();
		devices.add(device);
	}

	public void removeDevice(Device device) {
		if (!Objects.isNull(devices))
			devices.remove(device);
	}

	public Cart setId(Integer id) {
		this.id = id;
		return this;
	}

	public Cart setUser(User user) {
		this.user = user;
		return this;
	}

	public Cart setDevices(List<Device> devices) {
		this.devices = devices;
		return this;
	}
}
