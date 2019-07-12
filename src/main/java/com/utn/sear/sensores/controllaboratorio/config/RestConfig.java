package com.utn.sear.sensores.controllaboratorio.config;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {
    
    

    @Bean
    public RestTemplate restTemplate() {
        MappingJackson2HttpMessageConverter javascriptConverter = new MappingJackson2HttpMessageConverter();
        javascriptConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("application", "javascript")));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(BigDecimal.ZERO.intValue(), new StringHttpMessageConverter(Charset.defaultCharset()));
        restTemplate.getMessageConverters().add(javascriptConverter);
        return restTemplate;
    }

}
