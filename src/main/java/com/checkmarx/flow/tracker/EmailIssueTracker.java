package com.checkmarx.flow.tracker;

import com.checkmarx.flow.custom.IssueTracker;
import com.checkmarx.flow.dto.Issue;
import com.checkmarx.flow.dto.ScanRequest;
import com.checkmarx.flow.exception.MachinaException;
import com.checkmarx.flow.service.EmailIssueService;
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
public class EmailIssueTracker implements IssueTracker {
    private final EmailIssueService emailIssueService;
    public static final String WEB_HOOK_PAYLOAD = "web-hook-payload";
    public static final String MESSAGE_KEY = "message";
    public static final String HEADING_KEY = "heading";
    public static final String EMAIL_HEADING = "Scan Summary for ";
    public static final String PUSHER_NAME_KEY = "pusher";

    public EmailIssueTracker(EmailIssueService emailIssueService) {
        this.emailIssueService = emailIssueService;
    }

    @Override
    public void init(ScanRequest request, ScanResults results) throws MachinaException {

        Map<String, Object> emailCtx = new HashMap<>();

        emailCtx.put(MESSAGE_KEY, "Scan Summary for "
                        .concat(request.getNamespace()).concat("/").concat(request.getRepoName()).concat(" - ")
                        .concat(request.getRepoUrl()));
        emailCtx.put(HEADING_KEY, EMAIL_HEADING.concat(request.getRepoName()));
        String emailSubject = EMAIL_HEADING.concat(request.getNamespace()).concat("/").concat(request.getRepoName());

        //retrieve the name of the pusher
        String payLoadBody = request.getAdditionalMetadata(WEB_HOOK_PAYLOAD);
        if(!ScanUtils.empty(payLoadBody))
        {
            JSONObject jsonPayLoad = new JSONObject(payLoadBody);
            String pusherName = jsonPayLoad.getJSONObject("pusher").getString("name");
            emailCtx.put(PUSHER_NAME_KEY, "Last Push done by: ".concat(pusherName));
        }

        //results
        if (results != null) {
            emailCtx.put("issues", results.getXIssues());
        }
        if (results != null && ! ScanUtils.empty(results.getLink())) {
            emailCtx.put("link", results.getLink());
        }

        emailIssueService.sendmail(request.getEmail(), emailSubject, emailCtx, "mailTemplate.html");

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
