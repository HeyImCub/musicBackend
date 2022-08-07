package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.demo.service.SessionService;

@Configuration
@EnableScheduling
public class SpringConfig {
	
	@Autowired
    private SessionService sessionService;
	//60000 - 1 minute  600000 - 10 minutes 1000 - 1 second
	@Scheduled(fixedRate = 600000)
	public void scheduleFixedRateTask() {
	    System.out.println("Changing Times");
	    //repository.decrease times by one
	    sessionService.decreaseExpireNums();
	}
}
