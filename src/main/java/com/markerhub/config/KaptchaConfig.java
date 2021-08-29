package com.markerhub.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.parameters.P;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
	//自定义 验证码

	@Bean
	DefaultKaptcha producer() {
		Properties properties = new Properties();
		properties.put("kaptcha.border", "no");
		properties.put("kaptcha.textproducer.font.color", "black");
		properties.put("kaptcha.textproducer.char.space", "4");
		properties.put("kaptcha.image.height", "40");
		properties.put("kaptcha.image.width", "100");
		properties.put("kaptcha.textproducer.font.size", "30");

		Config config = new Config(properties);
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		defaultKaptcha.setConfig(config);

		return defaultKaptcha;
	}

}
