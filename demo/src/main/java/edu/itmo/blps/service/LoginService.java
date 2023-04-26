package edu.itmo.blps.service;

import edu.itmo.blps.dao.customer.User;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    public ResponseEntity<?> login(User user);
}
