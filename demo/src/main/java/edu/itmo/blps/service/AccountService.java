package edu.itmo.blps.service;

import edu.itmo.blps.dao.company.Company;
import edu.itmo.blps.dao.company.CompanyRepository;
import edu.itmo.blps.dao.customer.User;
import edu.itmo.blps.dao.customer.UserRepository;
import edu.itmo.blps.domain.MyUserDetails;
import edu.itmo.blps.domain.SecurityUser;
import edu.itmo.blps.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AccountService {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	CompanyRepository companyRepository;
	@Autowired
	UserRepository userRepository;

	@Transactional(value = "bitronixTransactionManager")
	public ResponseEntity<?> login(User user){
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		if(Objects.isNull(authentication)){
			return ResponseEntity.badRequest().body("Fail login");
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
		return ResponseEntity.ok(tokenMap);
	}
	public boolean checkString(String s){
		if ((s == null) | (Objects.equals(s, ""))){
			return false;
		}
		return true;
	}

	@Transactional(value = "bitronixTransactionManager")
	public ResponseEntity<?> signup(String name, String password, String type) {
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
			return ResponseEntity.ok("Success");
		}else {
			return ResponseEntity.badRequest().body(response.getDescription());
		}
	}
}
