package edu.itmo.blps.service;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class MyScheduledJob implements Job {
    @Autowired
    public MyScheduledJob(MessageService messageService) {
        this.messageService = messageService;
    }
    private MessageService messageService;
    @Override
    public void execute(JobExecutionContext context) {
        messageService.minLifeOfMessage();
    }
}