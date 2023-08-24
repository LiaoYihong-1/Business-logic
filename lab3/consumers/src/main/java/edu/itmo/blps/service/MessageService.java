package edu.itmo.blps.service;

import edu.itmo.blps.dao.cart.Cart;
import edu.itmo.blps.dao.cart.CartRepository;
import edu.itmo.blps.dao.customer.UserRepository;
import edu.itmo.blps.dao.message.Message;
import edu.itmo.blps.dao.message.MessageRepository;
import edu.itmo.blps.dao.transaction.Transaction;
import edu.itmo.blps.dao.transaction.TransactionRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

@Service
public class MessageService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private CartRepository cartRepository;

    private static volatile boolean go = true;
    private static volatile boolean go1 = true;

    @Qualifier("kafkaConsumer1")
    @Autowired
    KafkaConsumer<String, String> consumer1;
    @Transactional(value = "bitronixTransactionManager")
    public void receiveCartMessage() {
        if(!go){
            go = !go;
            return;
        }
        go = false;
        // 拉取消息
        ConsumerRecords<String, String> records = consumer1.poll(Duration.ofMillis(2000));

        // 处理消息
        for (ConsumerRecord<String, String> record : records) {
            System.out.println("Received message Cart: " + record.value());
            Set<Integer> users = new HashSet<>();
            List<Cart> cartList = cartRepository.findAll();
            String[] split = record.value().split(":");
            Integer id = Integer.valueOf(split[1]);
            for(Cart c :cartList){
                if(c.getDevice().equals(id)){
                    users.add(c.getCustomer());
                }
            }
            for(int i :users){
                Message m = new Message();
                m.setCustomer(i);
                m.setLife(30);
                if(Boolean.valueOf(split[0])) {
                    m.setText("The device you like now is available");
                }else{
                    m.setText("The device you like now is unavailable");
                }
                messageRepository.save(m);
            }

        }
        go = true;
    }
    @Qualifier("kafkaConsumer2")
    @Autowired
    KafkaConsumer<String, String> consumer2;
    //@KafkaListener(topics = "my-topic",groupId = "my-group")
    @Transactional(value = "bitronixTransactionManager",rollbackFor = Exception.class)
    public void receiveTransactionMessage() {
        if(!go1){
            go1 = !go1;
            return;
        }
        go1 = false;
        // 拉取消息
        ConsumerRecords<String, String> records = consumer2.poll(Duration.ofMillis(2000));

        // 处理消息
        for (ConsumerRecord<String, String> record : records) {
            System.out.println("Received message Transaction: " + record.value());

            Set<Integer> users = new HashSet<>();
            List<Transaction> transactions = transactionRepository.findAll();
            String[] split = record.value().split(":");
            Integer id = Integer.valueOf(split[1]);
            for(Transaction t :transactions){
                if(t.getDevice().getId().equals(id)){
                    Message m = new Message();
                    m.setCustomer(t.getUser().getId());
                    m.setLife(30);
                    users.add(t.getUser().getId());
                    if(!Boolean.valueOf(split[0])) {
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


        }
        go1 = true;

    }
}
