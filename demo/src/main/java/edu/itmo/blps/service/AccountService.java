package edu.itmo.blps.service;

import edu.itmo.blps.dao.cart.Cart;
import edu.itmo.blps.dao.cart.CartRepository;
import edu.itmo.blps.dao.company.Company;
import edu.itmo.blps.dao.company.CompanyRepository;
import edu.itmo.blps.dao.customer.User;
import edu.itmo.blps.dao.customer.UserRepository;
import edu.itmo.blps.dto.Account;
import edu.itmo.blps.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AccountService {
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	public boolean checkString(String s){
		if ((s == null) | (Objects.equals(s, ""))){
			return false;
		}
		return true;
	}

	public Response login(String name, String password, String type) {
		if(!checkString(name)||!checkString(password)||!checkString(type))
			return new Response(false, "You input a illegal user");
		if("company".equalsIgnoreCase(type) && !companyRepository.findByNameAndPassword(password,name).isPresent())
				return new Response(false, "Wrong company name or password");
		else if ("customer".equalsIgnoreCase(type) && !userRepository.findByUsernameAndPassword(name, password).isPresent())
				return new Response(false, "No such user account");
//		else
//			return new Response(false, "Wrong from code, will be fixed");
		return new Response(true, "Success");
	}

	public Response signup(Account account) {
		return signup(account.getName(), account.getPassword(), account.getType());
	}

	public Response signup(String name, String password, String type) {
		if (!checkString(name)||!checkString(password)||!checkString(type))
			return new Response(false, "You input illegal username or password like empty ");
		if ("company".equalsIgnoreCase(type)){
			if (companyRepository.existsByName(name))
				return new Response(false, "Account exists");
			companyRepository.save(new Company(name, password));
		} else if ("customer".equalsIgnoreCase(type)) {
			if (userRepository.findByUsername(name).isPresent())
				return new Response(false, "Account exists");
			User newUser = userRepository.save(new User(name, password));
			cartRepository.save(new Cart().setUser(newUser));
		} else
			return new Response(false, "Wrong from code, will be fixed");
		return new Response(true, "Success");
	}
}
