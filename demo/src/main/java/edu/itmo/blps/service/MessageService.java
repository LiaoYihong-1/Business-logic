package edu.itmo.blps.service;

import edu.itmo.blps.dao.cart.Cart;
import edu.itmo.blps.dao.cart.CartRepository;
import edu.itmo.blps.dao.customer.UserRepository;
import edu.itmo.blps.dao.message.Message;
import edu.itmo.blps.dao.message.MessageRepository;
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
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private CartRepository cartRepository;
    public ResponseEntity<?> getMessages(Integer id) {
        List<Message> messages = userRepository.findUserById(id).get().getMyMessages();
        return ResponseEntity.ok(messages);
    }

    @Qualifier("kafkaConsumer1")
    @Autowired
    KafkaConsumer<String, String> consumer;

    @Transactional(value = "bitronixTransactionManager")
    public ResponseEntity<?> deleteMessage(Integer message,Integer id) {
        Optional<Message> deviceOptional = messageRepository.findById(message);
        if(deviceOptional.isPresent()){
            Message m = deviceOptional.get();
            if(!m.getCustomer().equals(id)){
                return ResponseEntity.badRequest().body("You are trying to delete a message," +
                        "which is not belong to you");
            }
            messageRepository.delete(m);
        }else {
            return ResponseEntity.badRequest().body("No such a device");
        }
        return ResponseEntity.ok("Success");
    }
    @Transactional(value = "bitronixTransactionManager")
    public void receiveMessage() {
        //System.out.println(message);

        // 拉取消息
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(2000));

        // 处理消息
        for (ConsumerRecord<String, String> record : records) {
            System.out.println("Received message1: " + record.value());
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
    }
    @Transactional(value = "bitronixTransactionManager",rollbackFor = Exception.class)
    public void minLifeOfMessage(){
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
    }
}
