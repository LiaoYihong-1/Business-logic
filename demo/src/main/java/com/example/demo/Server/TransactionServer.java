package com.example.demo.Server;

import com.example.demo.Dao.Cart.Cart;
import com.example.demo.Dao.Cart.CartRepository;
import com.example.demo.Dao.Device.Device;
import com.example.demo.Dao.Device.DeviceRepository;
import com.example.demo.Dao.Transaction.Transaction;
import com.example.demo.Dao.Transaction.TranscationRepository;
import com.example.demo.Dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TransactionServer {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    TranscationRepository transcationRepository;
        private boolean checkString(String s){
        if ((s == null) | (Objects.equals(s, ""))){
            return false;
        }
        return true;
    }
    public Response addDevice(Integer company, Integer value, String type){
        Response response = new Response();
        response.setSuccess(true);
        response.setDescription("Success");
        if(!checkString(type)){
            response.setDescription("You input illegal value of the type of device");
            response.setSuccess(false);
            return response;
        }
        Device device = new Device();
        device.setAvailable(true);
        device.setCompany(company);
        device.setValue(value);
        device.setType(type);
        try {
            deviceRepository.save(device);
        }catch (Exception e){
            response.setDescription("Make sure that your account is still available");
            response.setSuccess(true);
            return response;
        }
        return response;
    }
    public Response addCart(Integer customer,Integer device){
        Response response = new Response();
        response.setSuccess(true);
        response.setDescription("Success");
        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setDevice(device);
        try {
            cartRepository.save(cart);
        }catch (Exception e){
            response.setDescription("No such kind of device. Maybe this device is deleted.");
            response.setSuccess(false);
        }
        return response;
    }

    public Response addTransaction(Integer customer,Integer device,Integer amount,Integer company){
        Response response = new Response();
        response.setSuccess(true);
        response.setDescription("Success");
        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setDevice(device);
        transaction.setAmount(amount);
        transaction.setSeller(company);
        transaction.setStatus("On proccess");
        try {
            transcationRepository.save(transaction);
        }catch (Exception e){
            response.setDescription("Two possibility: You account is not available\nThis device is not deleted");
            response.setSuccess(false);
        }
        return response;
    }
    public List<Device> getDevices(){
        return deviceRepository.findAll();
    }

    public List<Transaction> getTransactions(Integer customer){
        return transcationRepository.findByCostomer(customer);
    }
}
