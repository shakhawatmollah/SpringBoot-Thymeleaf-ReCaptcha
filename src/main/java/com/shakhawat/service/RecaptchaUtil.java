package com.shakhawat.service;

import java.util.HashMap;
import java.util.Map;

public class RecaptchaUtil {

	public static final Map<String, String> RECAPTCHA_ERROR_CODE = new HashMap<>();
	
    static {
    	RECAPTCHA_ERROR_CODE.put("missing-input-secret", 
    			"The CAPTCHA secret parameter is missing");
    	RECAPTCHA_ERROR_CODE.put("invalid-input-secret", 
    			"The CAPTCHA secret parameter is invalid or malformed");
    	RECAPTCHA_ERROR_CODE.put("missing-input-response", 
    			"The CAPTCHA response parameter is missing");
    	RECAPTCHA_ERROR_CODE.put("invalid-input-response", 
    			"The CAPTCHA response parameter is invalid or malformed");
    	RECAPTCHA_ERROR_CODE.put("bad-request", 
    			"The request is invalid or malformed");
    	RECAPTCHA_ERROR_CODE.put("timeout-or-duplicate",
    			"The response is no longer valid: either is too old or has been used previously.");
    }
}
