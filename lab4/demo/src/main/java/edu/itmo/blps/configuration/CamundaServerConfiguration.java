package edu.itmo.blps.configuration;

import edu.itmo.blps.dao.cart.Cart;
import edu.itmo.blps.dao.cart.CartRepository;
import edu.itmo.blps.dao.customer.UserRepository;
import edu.itmo.blps.dao.device.Device;
import edu.itmo.blps.dao.message.Message;
import edu.itmo.blps.dao.message.MessageRepository;
import edu.itmo.blps.dao.transaction.Transaction;
import edu.itmo.blps.dao.transaction.TransactionRepository;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Configuration
public class CamundaServerConfiguration {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Bean
    @ExternalTaskSubscription(topicName = "sendMessage", processDefinitionKey = "Online_Shopping", lockDuration = 100)
    public ExternalTaskHandler sendMessage(){
        return (externalTask, externalTaskService) ->{
            System.out.println("SEND MESSAGES");
            String idInfo = externalTask.getVariable("deviceId")+"";
            String statusInfo =  externalTask.getVariable("statusAfterChanged")+"";
            System.out.println("Device: " + idInfo + "\n" + "Status: " + statusInfo + "\n");

            Boolean statusAfterChanged = Boolean.valueOf(externalTask.getVariable("statusAfterChanged")+"");
            Integer deviceId = Integer.valueOf(externalTask.getVariable("deviceId")+"");
            Set<Integer> users = new HashSet<>();
            List<Cart> cartList = cartRepository.findAll();
            externalTaskService.complete(externalTask);
            for(Cart c :cartList){
                if(c.getDevice().equals(deviceId)){
                    users.add(c.getCustomer());
                }
            }
            for(int i :users){
                Message m = new Message();
                m.setCustomer(i);
                m.setLife(30);
                if(statusAfterChanged) {
                    m.setText("The device you like now is available");
                }else{
                    m.setText("The device you like now is unavailable");
                }
                messageRepository.save(m);
            }

            List<Transaction> transactions = transactionRepository.findAll();
            for(Transaction t :transactions){
                if(t.getDevice().getId().equals(deviceId)){
                    Message m = new Message();
                    m.setCustomer(t.getUser().getId());
                    m.setLife(30);
                    if(!statusAfterChanged) {
                        transactionRepository.changeTransactionStatus(t.getId(),"Hung up");
                        m.setText("Sorry, your device is not available because of some unpredicted\nYou can cancel it or wait for some days\n");
                        messageRepository.save(m);
                    }else if(t.getStatus().equals("Hung up")){
                        transactionRepository.changeTransactionStatus(t.getId(),"On process");
                        m.setText("Your transaction is recovery. Thank for your waiting");
                        messageRepository.save(m);
                    }
                }
            }
        };
    }
    @Bean
    @ExternalTaskSubscription(topicName = "reduceLifeOfMessage", processDefinitionKey = "Timer", lockDuration = 1000)
    public ExternalTaskHandler reduceLifeOfMessage(){
        return (externalTask, externalTaskService) ->{
            List<Message> messages = messageRepository.findAll();
            for(Message m : messages){
                Integer id = m.getId();
                Integer life = m.getLife() - 1;
                if(life <= 0){
                    messageRepository.delete(m);
                }else {
                    messageRepository.updateMessageByLife(id, life);
                }
            }
            externalTaskService.complete(externalTask);
        };
    }

}
