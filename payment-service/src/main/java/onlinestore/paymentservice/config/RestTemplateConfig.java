package onlinestore.paymentservice.config;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateConfig extends RestTemplate {
    public RestTemplate RestTemplateService() {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}
