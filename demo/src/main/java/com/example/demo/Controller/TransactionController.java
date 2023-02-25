package com.example.demo.Controller;

import com.example.demo.Dao.Cart.Cart;
import com.example.demo.Dao.Device.Device;
import com.example.demo.Dao.Transaction.Transaction;
import com.example.demo.Dto.*;
import com.example.demo.Server.TransactionServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {
    @Autowired
    TransactionServer transactionServer;

    @PostMapping("/add-device")
    public Response addDevice(@RequestBody Device request){
        return transactionServer.addDevice(request.getCompany(),request.getValue(), request.getType());
    }

    @PostMapping("/add-cart")
    public Response addCart(@RequestBody Cart request){
        return transactionServer.addCart(request.getCustomer(),request.getDevice());
    }
    @PostMapping("/add-transaction")
    public Response addTransaction(@RequestBody Transaction request){
        return transactionServer.addTransaction(request.getCustomer(),request.getDevice(), request.getAmount(),request.getSeller());
    }

    @GetMapping("/get-device")
    public List<Device> getDevices(){
        return transactionServer.getDevices();
    }

    @RequestMapping(value="/get-transaction/{id}",method = {RequestMethod.GET})
    public List<Transaction> getTransaction(@PathVariable Integer id){
        return transactionServer.getTransactions(id);
    }
}
