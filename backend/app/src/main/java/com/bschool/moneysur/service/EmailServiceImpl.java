package com.bschool.moneysur.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    //  On récupère la valeur du YAML
    @Value("${app.mail-from}")
    private String mailFrom;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String to, String token) {
        String subject = "Bienvenue sur MoneySur ! Confirmez votre email";
        String verificationUrl = baseUrl + "/api/auth/verify-email?token=" + token;

        String htmlContent = "<h1>Bienvenue !</h1>"
                + "<p>Merci de votre inscription.</p>"
                + "<p>Cliquez ci-dessous pour activer votre compte :</p>"
                + "<a href=\"" + verificationUrl + "\">ACTIVER MON COMPTE</a>"
                + "<br><br>"
                + "<p>Lien valide 24h.</p>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom(mailFrom);

            mailSender.send(message);
            System.out.println("Email envoyé à " + to);

        } catch (MessagingException e) {
            throw new RuntimeException("Erreur d'envoi d'email", e);
        }
    }
}
