package com.example.demo.Server;

import com.example.demo.Dao.Company.Company;
import com.example.demo.Dao.Company.CompanyRepository;
import com.example.demo.Dao.Customer.Customer;
import com.example.demo.Dao.Customer.CustomerRepository;
import com.example.demo.Dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AccountService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    CustomerRepository customerRepository;

    public boolean checkString(String s){
        if ((s == null) | (Objects.equals(s, ""))){
            return false;
        }
        return true;
    }
    public Response login(String name, String password, String type){
        Response response = new Response();
        response.setSuccess(true);
        response.setDescription("Success");
        if(!checkString(name)||!checkString(password)||!checkString(type)){
            response.setSuccess(false);
            response.setDescription("You input a illegal user");
            return response;
        }
        if("company".equalsIgnoreCase(type)){
            if(companyRepository.login(password,name).size()!=1){
                response.setSuccess(false);
                response.setDescription("No such company");
            }
        }else if ("customer".equalsIgnoreCase(type)) {
            if(customerRepository.login(password,name).size()!=1){
                response.setSuccess(false);
                response.setDescription("No such user account");
            }
        }else{
            response.setSuccess(false);
            response.setDescription("Wrong from code, will be fixed");
        }
        return response;
    }

    public Response signup(String name, String password, String type){
        Response response = new Response();
        response.setSuccess(true);
        response.setDescription("Success");
        if(!checkString(name)||!checkString(password)||!checkString(type)){
            response.setDescription("You input illegal username or password like empty ");
            response.setSuccess(false);
            return response;
        }
        if("company".equalsIgnoreCase(type)){
            List<String> allName = companyRepository.findAllNames();
            if(allName.contains(name)){
                response.setDescription("Account exists");
                response.setSuccess(false);
                return response;
            }
            Company company = new Company();
            company.setName(name);
            company.setPassword(password);
            companyRepository.save(company);
        } else if ("customer".equalsIgnoreCase(type)) {
            List<String> allName = customerRepository.findAllNames();
            if(allName.contains(name)){
                response.setDescription("Account exist");
                response.setSuccess(false);
                return response;
            }
            Customer customer = new Customer();
            customer.setName(name);
            customer.setPassword(password);
            customerRepository.save(customer);
        }else{
            response.setDescription("Wrong from code, will be fixed");
            response.setSuccess(false);
            return response;
        }
        return response;
    }
}
