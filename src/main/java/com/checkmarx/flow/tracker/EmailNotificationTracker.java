package com.checkmarx.flow.tracker;

import com.checkmarx.flow.custom.IssueTracker;
import com.checkmarx.flow.dto.Issue;
import com.checkmarx.flow.dto.ScanRequest;
import com.checkmarx.flow.exception.MachinaException;
import com.checkmarx.flow.service.EmailNotificationService;
import com.checkmarx.sdk.dto.ScanResults;
import com.checkmarx.sdk.utils.ScanUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("EmailNotification")
public class EmailNotificationTracker implements IssueTracker {
    private final EmailNotificationService emailNotificationService;
    public static final String WEB_HOOK_PAYLOAD = "web-hook-payload";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EMAIL_HEADING = "heading";
    public static final String VALUE_EMAIL_HEADING = "Scan Summary for ";
    public static final String KEY_PUSHER_NAME = "pusher";
    public static final String KEY_TEAM_NAME = "teamName";
    public static final String KEY_BRANCH = "branch";


    public EmailNotificationTracker(EmailNotificationService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }

    @Override
    public void init(ScanRequest request, ScanResults results) throws MachinaException {

        Map<String, Object> emailCtx = new HashMap<>();
        String emailSubject = "";

        if(request.getNamespace()!= null && request.getRepoName()!=null && request.getRepoUrl()!=null) {
            emailCtx.put(KEY_MESSAGE, (request.getNamespace()).concat("/").concat(request.getRepoName()).concat(" - ")
                    .concat(request.getRepoUrl()));

            emailCtx.put(KEY_EMAIL_HEADING, VALUE_EMAIL_HEADING.concat(request.getRepoName()));
            emailCtx.put("repo", request.getRepoUrl());
            emailSubject = VALUE_EMAIL_HEADING.concat(request.getNamespace()).concat("/").concat(request.getRepoName());
            
            //retrieve the name of the pusher
            String payLoadBody = request.getAdditionalMetadata(WEB_HOOK_PAYLOAD);
            if(!ScanUtils.empty(payLoadBody))
            {
                JSONObject jsonPayLoad = new JSONObject(payLoadBody);
                String pusherName = jsonPayLoad.getJSONObject("pusher").getString("name");
                emailCtx.put(KEY_PUSHER_NAME, "Last push done by: ".concat(pusherName));
            }
        }
        else
        {
            emailSubject = VALUE_EMAIL_HEADING.concat("Project: ").concat(request.getProject());
            emailCtx.put(KEY_MESSAGE, "Project: ".concat(request.getProject()));
            emailCtx.put(KEY_EMAIL_HEADING, VALUE_EMAIL_HEADING.concat("Project: ").concat(request.getProject()));

        }

        if(!ScanUtils.empty(request.getBranch()))
        {
            emailCtx.put(KEY_BRANCH, "Branch: ".concat(request.getBranch()));
        }
        emailCtx.put(KEY_TEAM_NAME,"Team Name: ".concat(request.getTeam()));

        //results
        if (results != null) {
            emailCtx.put("issues", results.getXIssues());
        }
        if (results != null && ! ScanUtils.empty(results.getLink())) {
            emailCtx.put("link", results.getLink());
        }

        emailNotificationService.sendmail(request.getEmail(), emailSubject, emailCtx);
    }

    @Override
    public void complete(ScanRequest request, ScanResults results) throws MachinaException {

    }

    @Override
    public String getFalsePositiveLabel() throws MachinaException {
        return null;
    }

    @Override
    public List<Issue> getIssues(ScanRequest request) throws MachinaException {
        return null;
    }

    @Override
    public Issue createIssue(ScanResults.XIssue resultIssue, ScanRequest request) throws MachinaException {
        return null;
    }

    @Override
    public void closeIssue(Issue issue, ScanRequest request) throws MachinaException {

    }

    @Override
    public Issue updateIssue(Issue issue, ScanResults.XIssue resultIssue, ScanRequest request) throws MachinaException {
        return null;
    }

    @Override
    public String getIssueKey(Issue issue, ScanRequest request) {
        return null;
    }

    @Override
    public String getXIssueKey(ScanResults.XIssue issue, ScanRequest request) {
        return null;
    }

    @Override
    public boolean isIssueClosed(Issue issue, ScanRequest request) {
        return false;
    }

    @Override
    public boolean isIssueOpened(Issue issue, ScanRequest request) {
        return false;
    }
}
