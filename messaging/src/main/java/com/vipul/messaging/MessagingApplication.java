package com.vipul.messaging;

import com.vipul.messaging.service.ConversationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication(scanBasePackages = {"com.vipul.channel", "com.vipul.messaging"})
public class MessagingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessagingApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ConversationService conversationService) {
		return args -> {
			System.out.println("Testing application starting");
//			conversationService.addParticipant("64f6c2e52c533345a6145474",
//					List.of("64e85c14b554ab2d7a643e66", "64e85d724ff8ae3f93fd70bc"));
		};
	}
}
