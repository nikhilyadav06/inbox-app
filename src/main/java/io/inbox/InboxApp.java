package io.inbox;

import java.nio.file.Path;

import javax.annotation.PostConstruct;

// import io.inbox.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.inbox.folders.Folder;
import io.inbox.folders.FolderRepository;

@SpringBootApplication
@RestController
public class InboxApp {

	// @Autowired
	// private EmailService emailService;

	@Autowired private FolderRepository folderRepository;

	public static void main(String[] args) {
		SpringApplication.run(InboxApp.class, args);
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	@Autowired
	public void init() {
		folderRepository.save(new Folder("nikhilyadav06", "Inbox", "blue"));
		folderRepository.save(new Folder("nikhilyadav06", "Sent Items", "green"));
		folderRepository.save(new Folder("nikhilyadav06", "Important", "yellow"));
	}

	// @PostConstruct
	// public void initializeData() {

	// 	for (int i = 0; i < 10; i++) {

	// 		emailService.sendEmail("koushikkothagal", "koushikkothagal", "Test " + i, "Body " + i);
	// 	}
		

	// }

}
