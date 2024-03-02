package io.dmly.invoicer.resolver;

import jakarta.servlet.http.HttpServletRequest;

public interface ClientParametersResolver {
    String getIpAddress(HttpServletRequest request);
    String getDevice(HttpServletRequest request);
}
