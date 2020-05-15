package com.shakhawat.controller;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shakhawat.dao.ContactDao;
import com.shakhawat.entity.Contact;
import com.shakhawat.service.RecaptchaService;


@Controller
public class ContactController {
	
	@Autowired
	private ContactDao contactDao;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired 
	RecaptchaService captchaService;
	
	@GetMapping("/contact-us")
	public ModelAndView contactForm(Model m) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("contactInfo", new Contact());
		mav.setViewName("contact/form");
		return mav;
	}
	
	@PostMapping("/contact-us")
	public String contactForm(@Valid @ModelAttribute("contactInfo") Contact contactInfo,  BindingResult errors, 
			HttpServletRequest request, SessionStatus sessionStatus, RedirectAttributes redirectAttributes, Model m) throws MessagingException {
		
		// Captcha Validation
		  String captchaVerifyMessage =  captchaService.verifyRecaptcha(request);

		  if (StringUtils.isNotEmpty(captchaVerifyMessage)) {		
			m.addAttribute("contactInfo", contactInfo);
			m.addAttribute("msg", captchaVerifyMessage);
			m.addAttribute("error", Boolean.TRUE);
            return "contact/form";
		  }
		// End Captcha Validation
		
		// Store Contact Information
		if (errors.hasErrors()) {
			m.addAttribute("contactInfo", contactInfo);
			m.addAttribute("error", Boolean.TRUE);
            return "contact/form";
        }
		contactInfo.setCreatedAt(new Date());
		contactDao.save(contactInfo);
		// End
		
		String personName = contactInfo.getPersonName();
		String email = contactInfo.getPersonEmail();
		String mobile = contactInfo.getPersonMobile();
		String sbuject = contactInfo.getContactSubject();
		String message = contactInfo.getMessage();
		
		String mailBody = "<!DOCTYPE html><html><body>Hello Sir/Madam,<br><br> A message from "+personName+ 
				"<br>Email: "+email+
				"<br>Mobile: "+mobile+
				"<br><br>Subject:"+sbuject+ 
				"<br><br>Message:<br>"+message+"</body></html>";
		
		sendEmail("sh.sumon99@gmail.com", email, "Contact Information", mailBody);
		
		sessionStatus.setComplete();
		
		m.addAttribute("error", Boolean.FALSE);
		redirectAttributes.addFlashAttribute("msg", "Contact information has been submitted successfully.");
		
		return "redirect:/contact-us";
	}
	
	/**
	 * 
	 * @param to Mail-To
	 * @param subject Mail-Subject
	 * @param mailBody Email-Body
	 * @throws MessagingException
	 */
	void sendEmail(String to, String from, String subject, String mailBody) throws MessagingException {

        MimeMessage msg = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(to);
        helper.setFrom(new InternetAddress(from));
        helper.setSubject(subject);

        helper.setText(mailBody, true);

        mailSender.send(msg);

    }

}
