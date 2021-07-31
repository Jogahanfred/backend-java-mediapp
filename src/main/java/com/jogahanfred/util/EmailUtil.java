package com.jogahanfred.util;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Component
public class EmailUtil {
	//Autowired para enviar correo electronico.
	@Autowired
	private JavaMailSender emailSender;
	
	//Encargado de definir una plantilla que llegara dentro del correo electronico- THYMELEAF
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	public void enviarMail(Mail mail) throws MessagingException {
		//creamos la instancia de correo electronico
		MimeMessage message = emailSender.createMimeMessage();
		//configuracion de envio
		MimeMessageHelper helper = new MimeMessageHelper(message,
													     MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, 
													     StandardCharsets.UTF_8.name());
		
		//Establecemos el contenido que hay dentro de la plantilla
		Context context = new Context();
		//Con el Map que esta en el GETMODEL, NOS VA INDICAR cuales son los varoles que la plantilla dispondra
		context.setVariables(mail.getModel());		
		//La plantilla y le colocamos la direccion de donde se encuentra  - Propio de THYMELEAF
		String html = templateEngine.process("email/email-template", context);
		helper.setTo(mail.getTo());
		helper.setText(html, true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());
		//Acciona el mecanismo de envio de correo
		emailSender.send(message);
	}
}
