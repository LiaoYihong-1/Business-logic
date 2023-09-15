package edu.itmo.blps.configuration;

import edu.itmo.blps.dao.cart.Cart;
import edu.itmo.blps.dao.cart.CartRepository;
import edu.itmo.blps.dao.company.Company;
import edu.itmo.blps.dao.company.CompanyRepository;
import edu.itmo.blps.dao.customer.User;
import edu.itmo.blps.dao.customer.UserRepository;
import edu.itmo.blps.dao.device.Device;
import edu.itmo.blps.dao.device.DeviceRepository;
import edu.itmo.blps.dao.message.Message;
import edu.itmo.blps.dao.message.MessageRepository;
import edu.itmo.blps.dao.transaction.Transaction;
import edu.itmo.blps.dao.transaction.TransactionRepository;
import io.jsonwebtoken.Claims;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import utils.JwtUtils;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class CamundaUserConfiguration {
    final Pattern patternType = Pattern.compile("[a-zA-Z]+");

    public int getIDByToken(String token){
        /**
         * get 1user like
         */
        Claims claims = JwtUtils.parseToken(token);
        token = claims.getSubject();
        /**
         * get id part
         */
        Matcher matcherType = patternType.matcher(token);
        Integer id = Integer.parseInt(matcherType.replaceAll(""));
        return id;
    }

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Bean
    @ExternalTaskSubscription(topicName = "validation", processDefinitionKey = "Online_Shopping", lockDuration = 1000)
    public ExternalTaskHandler validateAccount(){
        return (externalTask, externalTaskService) ->{
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String identity = externalTask.getVariable("identity");
            String password = externalTask.getVariable("password");
            String username = externalTask.getVariable("username");
            Map<String,Object> m =  Variables.createVariables();
            try {
                if ("company".equals(identity)) {
                    Company c = companyRepository.findByName(username).get();
                    if(encoder.matches(password,c.getPassword())){
                        m.put("existed","true");
                        m.put("id",c.getId().toString());
                    }else {
                        m.put("existed","false");
                    }
                } else {
                    User u = userRepository.findByUsername(username).get();
                    if (encoder.matches(password,u.getPassword())){
                        m.put("existed","true");
                        m.put("id",u.getId().toString());
                    }else{
                        m.put("existed","false");
                    }
                }
            }catch (NoSuchElementException e){
                m.put("existed","false");
            }
            externalTaskService.complete(externalTask,m);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "login", processDefinitionKey = "Online_Shopping", lockDuration = 100)
    public ExternalTaskHandler login(){
        return (externalTask, externalTaskService) ->{
            Map<String,Object> m =  Variables.createVariables();
            String userId = externalTask.getVariable("id").toString() + externalTask.getVariable("identity");
            String jwtToken = JwtUtils.createToken(userId);
            m.put("token",jwtToken);
            m.put("password","");
            m.put("id","");
            externalTaskService.complete(externalTask, m);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "register", processDefinitionKey = "Online_Shopping", lockDuration = 100)
    public ExternalTaskHandler register(){
        return (externalTask, externalTaskService) ->{
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            Map<String,Object> m =  Variables.createVariables();
            String identity = externalTask.getVariable("identity");
            String password = externalTask.getVariable("password");
            String username = externalTask.getVariable("username");
            password = encoder.encode(password);
            if("company".equals(identity)){
                Company c = new Company();
                c.setPassword(password);
                c.setName(username);
                companyRepository.save(c);
                m.put("id",companyRepository.findByName(username).get().getId().toString());
            }else{
                User u = new User();
                u.setPassword(password);
                u.setUsername(username);
                userRepository.save(u);
                m.put("id",userRepository.findByUsername(username).get().getId().toString());
            }
            externalTaskService.complete(externalTask,m);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "searchDevice", processDefinitionKey = "Online_Shopping", lockDuration = 100)
    public ExternalTaskHandler searchDevice(){
        return (externalTask, externalTaskService) ->{
            String device = externalTask.getVariable("device") + "";
            Optional<Device> o = deviceRepository.findById(Integer.valueOf(device));
            Map<String,Object> m =  Variables.createVariables();
            if(o.isPresent()){
                System.out.println(o.get());
                m.put("available",o.get().getAvailable().toString());
                m.put("found","true");
            }else {
                System.out.println("No device with such id");
                m.put("found","false");
            }
            externalTaskService.complete(externalTask,m);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "buyDevice", processDefinitionKey = "Online_Shopping", lockDuration = 100)
    public ExternalTaskHandler buyDevice(){
        return (externalTask, externalTaskService) ->{
            String token = externalTask.getVariable("token") + "";
            Integer id = getIDByToken(token);

            String device = externalTask.getVariable("device") + "";
            Integer amount = Integer.valueOf(externalTask.getVariable("amount") + "");

            Transaction t = new Transaction();
            User u = userRepository.findUserById(id).get();
            Device d = deviceRepository.findById(Integer.valueOf(device)).get();

            t.setUser(u);
            t.setStatus("On process");
            t.setSeller(d.getCompany());
            t.setDevice(d);
            t.setAmount(amount);
            t.setDeviceId(d.getId());
            transactionRepository.save(t);
            externalTaskService.complete(externalTask);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "checkTransaction", processDefinitionKey = "Online_Shopping", lockDuration = 100)
    public ExternalTaskHandler getTransactions(){
        return (externalTask, externalTaskService) ->{
            String token = externalTask.getVariable("token") + "";
            Integer id = getIDByToken(token);

            Optional<User> userOptional = userRepository.findUserById(id);
            User user = userOptional.get();
            List<Transaction> list = transactionRepository.findByUser(user);
            for(Transaction t : list){
                System.out.print(t.toString());
                System.out.print("\n");
            }
            externalTaskService.complete(externalTask);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "addToCart", processDefinitionKey = "Online_Shopping", lockDuration = 100)
    public ExternalTaskHandler addToCart(){
        return (externalTask, externalTaskService) ->{
            String token = externalTask.getVariable("token") + "";
            Integer id = getIDByToken(token);

            String device = externalTask.getVariable("device") + "";

            Cart c = new Cart();
            c.setDevice(Integer.valueOf(device));
            c.setCustomer(id);
            cartRepository.save(c);
            externalTaskService.complete(externalTask);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "checkCart", processDefinitionKey = "Online_Shopping", lockDuration = 100)
    public ExternalTaskHandler checkCart(){
        return (externalTask, externalTaskService) ->{
            String token = externalTask.getVariable("token") + "";
            Integer id = getIDByToken(token);
            List<Cart> cartList = userRepository.findUserById(id).get().getMyCarts();
            for(Cart c : cartList){
                System.out.println(c);
            }
            externalTaskService.complete(externalTask);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "readMessages", processDefinitionKey = "Online_Shopping", lockDuration = 100)
    public ExternalTaskHandler readMessages(){
        return (externalTask, externalTaskService) ->{
            String token = externalTask.getVariable("token") + "";
            Integer id = getIDByToken(token);
            List<Message> messages = messageRepository.findMessagesByCustomer(id);
            for(Message m : messages){
                System.out.println(m);
            }
            externalTaskService.complete(externalTask);
        };
    }
}
