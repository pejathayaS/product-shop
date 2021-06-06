package com.philips.downstreamservicems;

import com.philips.downstreamservicems.service.SchedulerJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DownstreamServiceMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DownstreamServiceMsApplication.class, args);
	/*	SchedulerJob job = new SchedulerJob();
		job.run();*/
	}

}
