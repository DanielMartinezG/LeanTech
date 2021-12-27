package com.leantech.hotel.producer.util;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class Messages {

	@Autowired
	private MessageSource messageSource;

	/**
	 * get message by key
	 * @param key
	 * @return
	 */
	public String getMessages(String key) {
		return getMessages(key, null);
	}

	/**
	 * get message by key and send params to the messageSource
	 * @param key
	 * @param params
	 * @return
	 */
	public String getMessages(String key, String[] params) {
		return messageSource.getMessage(key, params, Locale.getDefault());
	}

}
