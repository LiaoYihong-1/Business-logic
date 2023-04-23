package edu.itmo.blps.dto;

import edu.itmo.blps.dao.company.Company;
import edu.itmo.blps.dao.customer.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
	String type;
	String name;
	String password;

	public User asUser(){
		if (type.equals("customer"))
			return new User(name, password);
		throw new IllegalStateException();
	}

	public Company asCompany() {
		if (type.equals("company"))
			return new Company(name, password);
		throw new IllegalStateException();
	}
}
