package io.inbox;

import java.nio.file.Path;
import java.util.Arrays;

// import javax.annotation.PostConstruct;

// import io.inbox.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.oauth2.core.user.OAuth2User;
// import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import io.inbox.email.Email;
import io.inbox.email.EmailRepository;
import io.inbox.emaillist.EmailListItem;
import io.inbox.emaillist.EmailListItemKey;
import io.inbox.emaillist.EmailListItemRepository;
import io.inbox.folders.Folder;
import io.inbox.folders.FolderRepository;

@SpringBootApplication
@RestController
public class InboxApp {

	// @Autowired
	// private EmailService emailService;

	@Autowired private FolderRepository folderRepository;
	@Autowired private EmailListItemRepository emailListItemRepository;
	@Autowired private EmailRepository emailRepository;

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

		for (int i = 0; i < 10; i++) {
			EmailListItemKey key = new EmailListItemKey();
			key.setId("nikhilyadav06");
			key.setLabel("Inbox");
			key.setTimeUUID(Uuids.timeBased());

			EmailListItem item = new EmailListItem();
			item.setKey(key);
			item.setTo(Arrays.asList("nikhilyadav06"));
			item.setSubject("subject " + i);
			item.setUnread(true);

			emailListItemRepository.save(item);

			Email email = new Email();
			email.setId(key.getTimeUUID());
			email.setFrom("nikhilyadav06");
			email.setSubject(item.getSubject());
			email.setBody("body" + i);
			email.setTo(item.getTo());

			emailRepository.save(email);
		}
	}

	// @PostConstruct
	// public void initializeData() {

	// 	for (int i = 0; i < 10; i++) {

	// 		emailService.sendEmail("koushikkothagal", "koushikkothagal", "Test " + i, "Body " + i);
	// 	}
		

	// }

}
