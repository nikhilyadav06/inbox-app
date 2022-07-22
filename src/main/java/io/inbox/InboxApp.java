package io.inbox;

import java.nio.file.Path;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import io.inbox.email.Email;
import io.inbox.email.EmailRepository;
import io.inbox.email.EmailService;
import io.inbox.emaillist.EmailListItem;
import io.inbox.emaillist.EmailListItemKey;
import io.inbox.emaillist.EmailListItemRepository;
import io.inbox.folders.Folder;
import io.inbox.folders.FolderRepository;
import io.inbox.folders.UnreadEmailStatsRepository;

@SpringBootApplication
@RestController
public class InboxApp {

	// @Autowired
	// private EmailService emailService;

	@Autowired private FolderRepository folderRepository;
	@Autowired private EmailService emailService;

	public static void main(String[] args) {
		SpringApplication.run(InboxApp.class, args);
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	@PostConstruct
	public void init() {
		folderRepository.save(new Folder("nikhilyadav06", "Work", "blue"));
		folderRepository.save(new Folder("nikhilyadav06", "Home", "green"));
		folderRepository.save(new Folder("nikhilyadav06", "Finally", "yellow"));

		for (int i = 0; i < 10; i++) {
			emailService.sendEmail("nikhilyadav06", Arrays.asList("nikhilyadav06", "abc"), "Hello" + i, "Body");
		}
	}

}
