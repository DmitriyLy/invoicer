package io.dmly.invoicer.resolver.impl;

import io.dmly.invoicer.resolver.ClientParametersResolver;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import static io.dmly.invoicer.constants.Constants.X_FORWARDED_FOR_HEADER;

@Component
public class ClientParametersResolverImpl implements ClientParametersResolver {
    private UserAgentAnalyzer userAgentAnalyzer;

    @PostConstruct
    public void init() {
        userAgentAnalyzer = UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(1000).build();
    }

    @Override
    public String getIpAddress(HttpServletRequest request) {
        String result = "<Unknown>";

        if (request != null) {
            String headerValue = request.getHeader(X_FORWARDED_FOR_HEADER);
            result = StringUtils.isEmpty(headerValue) ? request.getRemoteAddr() : headerValue;
        }

        return result;
    }

    @Override
    public String getDevice(HttpServletRequest request) {
        UserAgent userAgent = userAgentAnalyzer.parse(request.getHeader(HttpHeaders.USER_AGENT));
        return userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME) + " - "
                + userAgent.getValue(UserAgent.AGENT_NAME) + " - "
                + userAgent.getValue(UserAgent.DEVICE_NAME);
    }
}
