package se.iths.martin.springmessenger.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import se.iths.martin.springmessenger.model.Email;
import se.iths.martin.springmessenger.model.Message;

@Component("email")
public class EmailSender implements Messenger {
    private final JavaMailSender mailSender;
    private final String from;

    public EmailSender(JavaMailSender mailSender, @Value("${spring.mail.username}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    @Override
    public void send(Message message) {
        if (!(message instanceof Email email)) {
            throw new IllegalArgumentException("Fel typ av meddelande");
        }

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(from);
        mail.setTo(email.getRecipient());
        mail.setSubject(email.getSubject());
        mail.setText(email.getMessage());

        try {
            mailSender.send(mail);
            System.out.println("Mail sent");
        } catch (MailException e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }
}
