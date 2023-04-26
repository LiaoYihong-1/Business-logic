package edu.itmo.blps.service;

<<<<<<< HEAD
=======
import edu.itmo.blps.dao.cart.Cart;
import edu.itmo.blps.dao.cart.CartRepository;
>>>>>>> parent of 9c9ce79 (spring security finised)
import edu.itmo.blps.dao.company.Company;
import edu.itmo.blps.dao.company.CompanyRepository;
import edu.itmo.blps.dao.customer.User;
import edu.itmo.blps.dao.customer.UserRepository;
import edu.itmo.blps.domain.SecurityUser;
<<<<<<< HEAD
=======
import edu.itmo.blps.dto.Account;
>>>>>>> parent of 9c9ce79 (spring security finised)
import edu.itmo.blps.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import org.springframework.transaction.annotation.Transactional;
import utils.JwtUtils;
=======
>>>>>>> parent of 9c9ce79 (spring security finised)

import java.util.Objects;

@Service
<<<<<<< HEAD
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
=======
public class AccountService {
	public void login(SecurityUser user){

>>>>>>> parent of 9c9ce79 (spring security finised)
	}
}
