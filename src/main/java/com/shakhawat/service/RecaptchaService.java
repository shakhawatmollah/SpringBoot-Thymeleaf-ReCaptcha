package com.shakhawat.service;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class RecaptchaService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(RecaptchaService.class);

	@Value("${google.recaptcha.key.secret}") 
	String recaptchaSecret;
	
	protected static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
	
	protected static final String RECAPTCHA_URL_TEMPLATE = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";
	
	@Autowired
    protected ReCaptchaAttemptService reCaptchaAttemptService;
	
	/**
	 * 
	 * @param request HttpServletRequest
	 * @return The method will return error message
	 */
	public String verifyRecaptcha(HttpServletRequest request){
		
		String clientIP = getClientIP(request);
		String recaptchaResponse = request.getParameter("g-recaptcha-response");
		
		LOGGER.debug("Google's response: {} ", recaptchaResponse);
		
		if (StringUtils.isEmpty(recaptchaResponse)) {
			return "Captcha response not found.";
		}

		if (StringUtils.isNotEmpty(recaptchaResponse)) {
			if (!responseSanityCheck(recaptchaResponse)) {
				reCaptchaAttemptService.reCaptchaFailed(clientIP);
				return "Captcha response contains invalid characters.";
			}
        }
		
		if (reCaptchaAttemptService.isBlocked(clientIP)) {
            return "Client exceeded maximum number of failed attempts of Captcha.";
        }
		
		final URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, recaptchaSecret, recaptchaResponse, clientIP));
		
		LOGGER.debug("Google's response URI: {} ", verifyUri);
		
		RestTemplate restTemplate = new RestTemplate();
		Map<?, ?> result = restTemplate.getForObject(verifyUri, Map.class);

		boolean captchaSucess = (Boolean)result.get("success");
 
		if (BooleanUtils.isFalse(captchaSucess)) {
				
			List<?> errorCodes = (List<?>) result.get("error-codes");
			
			reCaptchaAttemptService.reCaptchaFailed(clientIP);
			
			if(CollectionUtils.isEmpty(errorCodes)){
				return "Unknown Error";
			}

			String errorMessage = errorCodes.stream().map(s -> RecaptchaUtil.RECAPTCHA_ERROR_CODE.get(s)).collect(Collectors.joining(", "));
		 
			return StringUtils.isNotEmpty(errorMessage)? errorMessage: "Unknown Error";
		}else {
			reCaptchaAttemptService.reCaptchaSucceeded(clientIP);
			return StringUtils.EMPTY;
		}	
	}
	
	/**
	 * 
	 * @param response Recaptcha-Response
	 * @return Boolean Yes/No
	 */
	protected boolean responseSanityCheck(final String response) {
        return StringUtils.isNotEmpty(response) && RESPONSE_PATTERN.matcher(response).matches();
    }
	
	/**
	 * 
	 * @param request HttpServletRequest
	 * @return Client-IP
	 */
	protected String getClientIP(HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
	
}
