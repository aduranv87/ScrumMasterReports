package virtualscrum.report;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import virtualscrum.domain.JiraIssue;
import virtualscrum.domain.Sprint;
import virtualscrum.utils.SendEmailHTML;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ScheduledReports {

    final RestTemplate restTemplate;

    private final String password;
    private final String username;
    private final String velocityUrl;
    private final String issuesUrl;
    private final List<JiraIssue> greenIssues = new ArrayList<>();
    private final List<JiraIssue> redIssues = new ArrayList<>();
    private final List<JiraIssue> orangeIssues = new ArrayList<>();
    private final Map<String, String> defaultRedIssues = new HashMap<>();
    private final Map<String, String> defaultOrangeIssues = new HashMap<>();
    private final List<Sprint> sprints = new ArrayList<>();
    private final SendEmailHTML sendEmail;

    public ScheduledReports(@Value("${application.jira.username}") String username,
                            @Value("${application.jira.password}") String password,
                            @Value("${application.jira.velocityUrl}") String velocityUrl,
                            @Value("${application.jira.issuesUrl}") String issuesUrl,
                            RestTemplate restTemplate, SendEmailHTML sendEmail) {
        this.username = username;
        this.password = password;
        this.velocityUrl = velocityUrl;
        this.issuesUrl = issuesUrl;
        this.restTemplate = restTemplate;
        this.sendEmail = sendEmail;
    }

    private HttpHeaders authenticateHeaders() {
        byte[] base64CredsBytes = Base64.encodeBase64(String.format("%s:%s", username, password).getBytes());
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Basic %s", base64Creds));

        return headers;
    }

    private void sortIssueToVelocityStatus(JiraIssue issue) {
        // Write logic to sort issues according the velocity report.
    }

    @Scheduled(fixedRateString = "${application.jira.scheduler.velocityReport}")
    private void processVelocitySprintData() {
        HttpEntity<String> request = new HttpEntity<>(authenticateHeaders());
        ResponseEntity<ResponseVelocity> response = restTemplate.exchange(velocityUrl, HttpMethod.GET, request, ResponseVelocity.class);
        ResponseVelocity sprintData = response.getBody();

        sprintData.getSprints().stream().forEach(sprint -> {

            String id = sprint.get("id").toString();
            String name = sprint.get("name").toString();

            HashMap<String, Object> sprintEstimation = sprintData.getVelocityStatEntries().get(id);

            String estimated = ((HashMap<String, Object>)sprintEstimation.get("estimated")).get("text").toString();
            String completed = ((HashMap<String, Object>)sprintEstimation.get("completed")).get("text").toString();

            Sprint sprintObj = new Sprint();
            sprintObj.setName(name);
            sprintObj.setCompleted(completed);
            sprintObj.setEstimated(estimated);

            sprints.add(sprintObj);
        });

        ResponseEntity<Object> responseIssues = restTemplate.exchange(issuesUrl, HttpMethod.GET, request, Object.class);
        Object issues = responseIssues.getBody();

        AtomicReference<Double> sprintCarryOn = new AtomicReference<>(0d);
        ((List<Object>) ((Map<String, Object>) issues).get("issues")).stream()
            .forEach(issue -> sprintCarryOn.updateAndGet(v -> v + Double.parseDouble(((Map<String, Object>) ((Map<String, Object>) issue).get("fields")).get("customfield_10005").toString())));

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("sprints", sprints);
        templateData.put("carryon",  sprintCarryOn.get());
        templateData.put("estimated", 32d - sprintCarryOn.get());

        String email1 = "emails@fromdatabase.com";

        try {
            sendEmail.send(templateConfig(templateData, "test"), email1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRateString = "${application.jira.scheduler.issuesStatus}")
    private void processIssuesStatus() {
        HttpEntity<String> request = new HttpEntity<>(authenticateHeaders());
        ResponseEntity<Object> responseIssuesMark = restTemplate.exchange(issuesUrl, HttpMethod.GET, request, Object.class);
        Object issuesMark = responseIssuesMark.getBody();

        ((List<Object>)((Map<String, Object>) issuesMark).get("issues")).stream()
            .forEach(issue -> {
                Map<String, Object> issueJson = ((Map<String, Object>) issue);
                Map<String, Object> fields = ((Map<String, Object>) issueJson.get("fields"));
                JiraIssue jiraIssue = new JiraIssue();
                jiraIssue.setKey(issueJson.get("key").toString());
                jiraIssue.setPoints(Double.parseDouble(Optional.ofNullable(fields.get("customfield_10005")).orElse("0").toString()));

                sortIssueToVelocityStatus(jiraIssue);
            });

        Map<String, Object> templateDataIssues = new HashMap<>();
        templateDataIssues.put("greenIssues", greenIssues);
        templateDataIssues.put("orangeIssues", orangeIssues);
        templateDataIssues.put("redIssues", redIssues);

        String email2 = "emails@fromdatabase.com";

        try {

            String body = templateConfig(templateDataIssues, "sla");
            sendEmail.send(body, email2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    public String templateConfig(Map<String, Object> templateData, String templateName) throws IOException, TemplateException {
        Configuration cfg = new Configuration(new Version("2.3.23"));

        cfg.setClassForTemplateLoading(ScheduledReports.class, "/");
        cfg.setDefaultEncoding("UTF-8");

        Template template = cfg.getTemplate(templateName + ".ftl");

        try (StringWriter out = new StringWriter()) {
            template.process(templateData, out);
            return out.toString();
        }
    }

}
