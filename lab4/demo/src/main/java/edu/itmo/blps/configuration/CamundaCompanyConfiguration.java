package edu.itmo.blps.configuration;

import edu.itmo.blps.dao.cart.Cart;
import edu.itmo.blps.dao.company.CompanyRepository;
import edu.itmo.blps.dao.device.Device;
import edu.itmo.blps.dao.device.DeviceRepository;
import io.jsonwebtoken.Claims;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import utils.JwtUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class CamundaCompanyConfiguration {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    DeviceRepository deviceRepository;
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

    @Bean
    @ExternalTaskSubscription(topicName = "addDevice", processDefinitionKey = "Online_Shopping", lockDuration = 100)
    public ExternalTaskHandler addDevice(){
        return (externalTask, externalTaskService) ->{
            String deviceName =  externalTask.getVariable("deviceName");
            Integer price = Integer.valueOf(externalTask.getVariable("price") + "");
            String deviceType = externalTask.getVariable("type");
            String country = externalTask.getVariable("country");
            Device d = new Device();
            d.setName(deviceName);
            d.setAvailable(true);
            d.setCountry(country);
            d.setPrice(price);
            d.setType(deviceType);

            String token = externalTask.getVariable("token");
            d.setCompany(companyRepository.findById(getIDByToken(token)).get());

            deviceRepository.save(d);
            externalTaskService.complete(externalTask);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "checkDevices", processDefinitionKey = "Online_Shopping", lockDuration = 100)
    public ExternalTaskHandler checkDevices(){
        return (externalTask, externalTaskService) ->{

            String token = externalTask.getVariable("token");
            List<Device> devices = deviceRepository.findAllByCompanyId(getIDByToken(token));
            for(Device d : devices){
                System.out.println(d);
                System.out.println("\n");
            }
            externalTaskService.complete(externalTask);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "changeDevice", processDefinitionKey = "Online_Shopping", lockDuration = 100)
    public ExternalTaskHandler changeDevice(){
        return (externalTask, externalTaskService) ->{
            String token = externalTask.getVariable("token");
            Integer deviceId = Integer.valueOf(externalTask.getVariable("deviceId")+"");
            Map<String,Object> m =  Variables.createVariables();
            if(deviceRepository.findById(deviceId).isPresent() && deviceRepository.findById(deviceId).get().getCompanyId()==getIDByToken(token)){
                Device d = deviceRepository.findById(deviceId).get();
                if(!d.getAvailable().equals(Boolean.valueOf(externalTask.getVariable("deviceAvailable") + ""))){
                    deviceRepository.changeDeviceStatus(deviceId,Boolean.valueOf(externalTask.getVariable("deviceAvailable")+""));
                    m.put("isDeviceChanged","true");
                }else {
                    m.put("isDeviceChanged","false");
                }
            }else {
                System.out.println("You can't change a device, which is not belong to you\n");
                m.put("isDeviceChanged","false");
            }
            externalTaskService.complete(externalTask,m);
        };
    }
}
