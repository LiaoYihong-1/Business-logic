package edu.itmo.blps.service;

import edu.itmo.blps.dao.company.Company;
import edu.itmo.blps.dao.company.CompanyRepository;
import edu.itmo.blps.dao.customer.User;
import edu.itmo.blps.dao.customer.UserRepository;
import edu.itmo.blps.domain.MyUserDetails;
import edu.itmo.blps.domain.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class MyLoginUserDetailsService implements UserDetailsService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null || "".equals(username)){
            return null;
        }
        try {
            User customer = userRepository.findByUsername(username).get();
            return new MyUserDetails(new SecurityUser(customer.getUsername(), customer.getPassword()));
        }catch (NoSuchElementException e){
            try{
                Company company = companyRepository.findByName(username).get();
                return new MyUserDetails(new SecurityUser(company.getName(), company.getPassword()));
            }catch (NoSuchElementException e1){
                e1.printStackTrace();
                return null;
            }
        }
    }
}
