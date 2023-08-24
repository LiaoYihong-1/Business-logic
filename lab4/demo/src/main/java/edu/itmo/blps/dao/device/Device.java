package edu.itmo.blps.dao.device;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.itmo.blps.dao.company.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "device")
public class Device {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(nullable = false, name = "type")
	private String type;

	@Column(nullable = false, name = "name")
	private String name;

	@ManyToOne
	@JoinColumn(name = "companyid")
	@JsonIgnore
	private Company company;

	@Column(nullable = false,name="price")
	private Integer price;

	@Column(nullable = false,name="companyid",insertable = false,updatable = false)
	private Integer companyId;

	@Column(nullable = false,name="country")
	private String country;

	@Column(nullable = false,name="available")
	private Boolean available;

	@Override
	public String toString(){
		return "Name: " + this.name + "\n" +
		"Price: " + this.price + "\n" +
		"Company: " + this.company.getName()+ "\n" +
		"Available: " + this.available;
	}
}
