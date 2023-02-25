package com.example.demo.Controller;

import com.example.demo.Dao.Device.Device;
import com.example.demo.Dao.Transaction.Transaction;
import com.example.demo.Dto.*;
import com.example.demo.Server.TransactionServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {
    @Autowired
    TransactionServer transactionServer;

    @PostMapping("/adddevice")
    public Response addDevice(@RequestBody DeviceRequest request){
        return transactionServer.addDevice(request.getCompany(),request.getValue(), request.getType());
    }

    @PostMapping("/addcart")
    public Response addCart(@RequestBody CartRequest request){
        return transactionServer.addCart(request.getCustomer(),request.getDevice());
    }
    @PostMapping("/addtransaction")
    public Response addTransaction(@RequestBody TransactionRequest request){
        return transactionServer.addTransaction(request.getCustomer(),request.getDevice(), request.getAmount(),request.getCompany());
    }
    @PostMapping("/getalldevices")
    public List<Device> getDevices(){
        return transactionServer.getDevices();
    }

    @PostMapping("/gettransaction")
    public List<Transaction> getTransaction(@RequestBody GetTransactionRequest request){
        return transactionServer.getTransactions(request.getCustomer());
    }
}
