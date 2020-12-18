package id.seringiskering.simawar.service;

import static id.seringiskering.simawar.constant.EmailConstant.DEFAULT_PORT;
import static id.seringiskering.simawar.constant.EmailConstant.EMAIL_SUBJECT;
import static id.seringiskering.simawar.constant.EmailConstant.FROM_EMAIL;
import static id.seringiskering.simawar.constant.EmailConstant.GMAIL_SMTP_SERVER;
import static id.seringiskering.simawar.constant.EmailConstant.PASSWORD;
import static id.seringiskering.simawar.constant.EmailConstant.SIMPLE_MAIL_TRANSFER_PROTOCOL;
import static id.seringiskering.simawar.constant.EmailConstant.SMTP_AUTH;
import static id.seringiskering.simawar.constant.EmailConstant.SMTP_HOST;
import static id.seringiskering.simawar.constant.EmailConstant.SMTP_PORT;
import static id.seringiskering.simawar.constant.EmailConstant.SMTP_STARTTLS_ENABLE;
import static id.seringiskering.simawar.constant.EmailConstant.SMTP_STARTTLS_REQUIRED;
import static id.seringiskering.simawar.constant.EmailConstant.USERNAME;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.sun.mail.smtp.SMTPTransport;

import id.seringiskering.simawar.constant.EmailConstant;

@Service
public class EmailService {
	
	public void sendNewPasswordEmail(String firstName, String password, String email) throws MessagingException {
		Message message = createEmail(firstName, password, email);
		SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
		smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
		smtpTransport.sendMessage(message, message.getAllRecipients());
		smtpTransport.close();
	}
 	
	private Message createEmail(String firstName, String password, String email) throws MessagingException {
		Message message =  new MimeMessage(getEmailSession());
		message.setFrom(new InternetAddress(FROM_EMAIL));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email,false));
		message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(EmailConstant.CC_EMAIL, false));
		message.setSubject(EMAIL_SUBJECT);
		message.setText("Halo bos " + firstName + ", \n \n Password Anda adalah : " + password + "\n \n Team Mblendes");
		message.setSentDate(new Date());
		message.saveChanges();
		return message;
	}

	private Session getEmailSession() {
		Properties properties = System.getProperties();
		properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
		properties.put(SMTP_AUTH, true);
		properties.put(SMTP_PORT, DEFAULT_PORT);
		properties.put(SMTP_STARTTLS_ENABLE, true);
		properties.put(SMTP_STARTTLS_REQUIRED, true);
		return Session.getInstance(properties);
	}

}
