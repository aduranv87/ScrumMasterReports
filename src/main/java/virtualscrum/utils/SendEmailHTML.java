package virtualscrum.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Component
public class SendEmailHTML {

    private final String scrumBuddyEmail;
    private final String enableTLS;
    private final String port;
    private final String host;
    private final String auth;
    private final String username;
    private final String password;

    public SendEmailHTML(@Value("${application.scrumBuddyEmail}") String scrumBuddyEmail,
                         @Value("${application.mailServer.enableTLS}") String enableTLS,
                         @Value("${application.mailServer.port}") String port,
                         @Value("${application.mailServer.host}") String host,
                         @Value("${application.mailServer.auth}") String auth,
                         @Value("${application.mailServer.username}") String username,
                         @Value("${application.mailServer.password}") String password) {
        this.enableTLS = enableTLS;
        this.port = port;
        this.host = host;
        this.auth = auth;
        this.username = username;
        this.password = password;
        this.scrumBuddyEmail = scrumBuddyEmail;
    }

    public void send(String body, String email) {

        System.out.println("SSLEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", enableTLS);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", auth);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,
                    password);
            }
        });
        System.out.println("Session created");

        sendEmail(session, email,"ScrumBuddy", body);
    }

    private void sendEmail(Session session, String toEmail, String subject, String body){
        try
        {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(scrumBuddyEmail, "NoReply-JD"));

            msg.setReplyTo(InternetAddress.parse(scrumBuddyEmail, false));

            msg.setSubject(subject, "UTF-8");

            msg.setContent(body, "text/html; charset=utf-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, toEmail);
            System.out.println("Message is ready");
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
