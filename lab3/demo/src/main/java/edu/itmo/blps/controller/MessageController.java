package edu.itmo.blps.controller;

import edu.itmo.blps.domain.SecurityUser;
import edu.itmo.blps.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/myMessage")
public class MessageController {
    @Autowired
    MessageService service;
    @GetMapping()
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> getMessages() {
        SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user.toString());
        return service.getMessages(user.getId());
    }
    @DeleteMapping("/{message}")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> deleteMessages(@PathVariable Integer message) {
        SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user.toString());
        return service.deleteMessage(message,user.getId());
    }
}
