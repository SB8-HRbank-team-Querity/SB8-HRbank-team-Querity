package com.sprint.mission.sb8hrbankteamquerity.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@RequiredArgsConstructor
public class IpUtil {

    private final HttpServletRequest request;

    public String getClientIp() {
        String[] headerCandidates = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA"
        };

        for (String header : headerCandidates) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
//        return request.getRemoteAddr();  // IPv6

        return normalizeIpv4(request.getRemoteAddr());  // IPv4
    }

    // IPv6 -> IPv4
    private String normalizeIpv4(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);

            if (address.isLoopbackAddress()) {
                return "127.0.0.1";
            }

            byte[] bytes = address.getAddress();
            if (bytes.length == 16) {
                return InetAddress.getByAddress(
                    new byte[] { bytes[12], bytes[13], bytes[14], bytes[15] }
                ).getHostAddress();
            }

            return address.getHostAddress();
        } catch (UnknownHostException e) {
            return ip;
        }
    }
}
