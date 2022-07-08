package io.inbox.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import io.inbox.emailslist.EmailsList;
import io.inbox.emailslist.EmailsListRepository;
import io.inbox.folders.Folder;
import io.inbox.folders.FolderRepository;
import io.inbox.folders.FoldersService;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InboxPageController {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private EmailsListRepository emailsListRepository;
    @Autowired
    private FoldersService foldersService;

    private PrettyTime prettyTime = new PrettyTime();

    @GetMapping(value = "/")
    public String getHomePage(@RequestParam(required = false) String folder,
            @AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null && principal.getAttribute("login") != null) {
            String loginId = principal.getAttribute("login");
            List<Folder> folders = folderRepository.findAllById(loginId);
            List<Folder> initFolders = foldersService.init(loginId);
            // initFolders.stream().forEach(folderRepository::save);
            model.addAttribute("defaultFolders", initFolders);
            if (folders.size() > 0) {
                model.addAttribute("userFolders", folders);
            }
            if (StringUtils.isBlank(folder)) {
                folder = "Inbox";
            }
            model.addAttribute("currentFolder", folder);
            Map<String, Integer> folderToUnreadCounts = foldersService.getUnreadCountsMap(loginId);
            model.addAttribute("folderToUnreadCounts", folderToUnreadCounts);
            List<EmailsList> emails = emailsListRepository.findAllById_UserIdAndId_Label(loginId, folder);
            emails.stream().forEach(email -> {
                Date emailDate = new Date(Uuids.unixTimestamp(email.getId().getTimeId()));
                email.setAgoTimeString(prettyTime.format(emailDate));
            });
            model.addAttribute("folderEmails", emails);

            return "inbox-page";
        }
        return "index";

    }

    

}
