package com.ironflow.notificationservice.client;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

@Component
public class MemberClient {

    private final RestTemplate restTemplate;
    private final String memberServiceUrl;

    public MemberClient(@Value("${services.member.base-url:http://localhost:8081/member-app}") String memberServiceUrl) {
        this.restTemplate = new RestTemplate();
        this.memberServiceUrl = memberServiceUrl;
    }

    public Object getMemberById(Long id) {
        String url = memberServiceUrl + "/api/members/" + id;
        return restTemplate.getForObject(url, Object.class);
    }
}
