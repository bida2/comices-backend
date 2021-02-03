package com.vuetify.services;

import java.io.IOException;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

public class EmailService {

	public void sendEmail(String fromWho, String toWho, String finalMessage) throws IOException {
		Email from = new Email(fromWho);
	    String subject = "Response To Your Inquiry on comices.com";
	    Email to = new Email(toWho);
	    Content content = new Content("text/html", finalMessage);
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      sg.api(request);
	     // Response response = sg.api(request);
	     // System.out.println(response.getStatusCode());
	     // System.out.println(response.getBody());
	     // System.out.println(response.getHeaders());
	    } catch (IOException ex) {
	      throw ex;
	    }
	}
}
