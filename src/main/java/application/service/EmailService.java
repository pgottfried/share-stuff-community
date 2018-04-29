package application.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import application.entity.User;

@Component
public class EmailService{
	
		@Autowired private JavaMailSender javaMailSender;
		@Autowired private MessageSource messageSource;
		
		public void sendConfirmationEmail(User user) throws MessagingException{
			
	        MimeMessage cEmail = javaMailSender.createMimeMessage();
	     
	        	try {
	                MimeMessageHelper helper = new MimeMessageHelper(cEmail,true);
	                	String sender = messageSource.getMessage("confEmail.from", null,LocaleContextHolder.getLocale());
	                	helper.setFrom(sender);
	                	helper.setTo(user.getEmail());
	                	String subject = messageSource.getMessage("confEmail.subject", null, LocaleContextHolder.getLocale());
	                	helper.setSubject(subject);
	                	helper.setText("http://localhost:8080/email/confirmation?code=" +user.getUuid());
	        	
	        	} finally {
					javaMailSender.send(cEmail);
				}
		}
}
