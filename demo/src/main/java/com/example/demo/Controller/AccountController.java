package com.example.demo.Controller;


import com.example.demo.Dao.Company.Company;
import com.example.demo.Dao.Company.CompanyRepository;
import com.example.demo.Dao.Customer.Customer;
import com.example.demo.Dao.Customer.CustomerRepository;
import com.example.demo.Dto.AccountRequest;
import com.example.demo.Dto.Response;
import com.example.demo.Server.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RestController
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping("/login")
    //@RequestMapping(value="/login/{username}/{password}",method = {RequestMethod.GET})
    public Response login(@RequestBody AccountRequest request){
        String name = request.getName();
        String password = request.getPassword();
        String type = request.getType();
        return accountService.login(name,password,type);
    }
    @PostMapping("/register")
    //@RequestMapping(value="/login/{username}/{password}",method = {RequestMethod.GET})
    public Response signup(@RequestBody AccountRequest request){
        String name = request.getName();
        String password = request.getPassword();
        String type = request.getType();
        return accountService.signup(name,password,type);
    }
}
