package edu.itmo.blps.service;

import edu.itmo.blps.dao.company.Company;
import edu.itmo.blps.dao.company.CompanyRepository;
import edu.itmo.blps.dao.customer.User;
import edu.itmo.blps.dao.customer.UserRepository;
<<<<<<< HEAD
<<<<<<< HEAD
import edu.itmo.blps.domain.SecurityUser;
<<<<<<< HEAD
=======
=======
>>>>>>> parent of fb00c3a (start security)
import edu.itmo.blps.dto.Account;
>>>>>>> parent of 9c9ce79 (spring security finised)
=======
import edu.itmo.blps.domain.MyUserDetails;
import edu.itmo.blps.domain.SecurityUser;
>>>>>>> parent of 14631e4 (back)
import edu.itmo.blps.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.JwtUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AccountService implements LoginService {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private UserRepository userRepository;

	public ResponseEntity<Response> login(User user){
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		if(Objects.isNull(authentication)){
			return new ResponseEntity<>(new Response(false,"fail",null),HttpStatus.BAD_REQUEST);
		}
		MyUserDetails details = (MyUserDetails) authentication.getPrincipal();
		SecurityUser securityUser = details.getUser();
		/**
		 * token is made up of id + type
		 */
		String userId = securityUser.getId().toString() + securityUser.getType();
		/**
		 * create token
		 */
		String jwtToken = JwtUtils.createToken(userId);
		Map<String,String> tokenMap = new HashMap<>();
		tokenMap.put("token",jwtToken);
		Response r= new Response(true,"Success",tokenMap);
		return new ResponseEntity<>(r,HttpStatus.OK);
	}

	public boolean checkString(String s){
		if ((s == null) | (Objects.equals(s, ""))){
			return false;
		}
		return true;
	}

	@Transactional(value = "bitronixTransactionManager")
	public ResponseEntity<Response> signup(String name, String password, String type) {
		Response response = new Response(true, "Success",null);
		if (!checkString(name)||!checkString(password)||!checkString(type)) {
			response.setSuccess(false);
			response.setDescription("You input illegal username or password like empty\n");
			response.setExtra(null);
		}
		password = new BCryptPasswordEncoder().encode(password);
		if ("company".equalsIgnoreCase(type)){
			if (companyRepository.existsByName(name)) {
				response.setSuccess(false);
				response.setDescription("Account exists");
				response.setExtra(null);
			}
			companyRepository.save(new Company(name, password));
		} else if ("customer".equalsIgnoreCase(type)) {
			if (userRepository.findByUsername(name).isPresent()) {
				response.setSuccess(false);
				response.setDescription("Account exists");
				response.setExtra(null);
			}
			userRepository.save(new User(name, password));
		} else {
			response.setSuccess(false);
			response.setDescription("Wrong from code, will be fixed");
			response.setExtra(null);
		}
		if(response.getSuccess()) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		}
<<<<<<< HEAD
=======
public class AccountService {
	@Autowired
	private CompanyRepository companyRepository;

<<<<<<< HEAD
>>>>>>> parent of 9c9ce79 (spring security finised)
=======
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
>>>>>>> parent of fb00c3a (start security)
=======
>>>>>>> parent of 14631e4 (back)
	}
}
