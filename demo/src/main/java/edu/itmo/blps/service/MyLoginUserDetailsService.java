package edu.itmo.blps.service;

import edu.itmo.blps.dao.company.Company;
import edu.itmo.blps.dao.company.CompanyRepository;
import edu.itmo.blps.dao.customer.User;
import edu.itmo.blps.dao.customer.UserRepository;
import edu.itmo.blps.domain.MyUserDetails;
import edu.itmo.blps.domain.SecurityUser;
import edu.itmo.blps.domain.SecurityUserSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import utils.XmlUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.List;

@Service
public class MyLoginUserDetailsService implements UserDetailsService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SecurityUserSet securityUserSet;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null || "".equals(username)){
            return null;
        }
        SecurityUser securityUser;
        List<String> permissions;
        try {
            User customer = userRepository.findByUsername(username).get();
            permissions = Arrays.asList("user");
            securityUser = new SecurityUser(customer.getUsername(), customer.getPassword(),customer.getId(),"user");
        }catch (NoSuchElementException e){
            try{
                Company company = companyRepository.findByName(username).get();
                permissions = Arrays.asList("company");
                securityUser = new SecurityUser(company.getName(), company.getPassword(),company.getId(),"company");
            }catch (NoSuchElementException e1){
                e1.printStackTrace();
                return null;
            }
        }
        /**
         * save to xml for later token auth
         */
        try {
            securityUserSet.getUsers().add(securityUser);
            XmlUtils.createUsers(securityUserSet);
        }catch (JAXBException | IOException | NullPointerException e){
            e.printStackTrace();
            throw new RuntimeException("Fail to write XML\n");
        }
        return new MyUserDetails(securityUser,permissions);
    }
}
