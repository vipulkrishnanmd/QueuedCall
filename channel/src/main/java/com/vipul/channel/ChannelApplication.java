package com.vipul.channel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ChannelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChannelApplication.class, args);
	}

}
