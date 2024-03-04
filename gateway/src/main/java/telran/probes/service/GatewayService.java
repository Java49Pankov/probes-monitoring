package telran.probes.service;

import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

public interface GatewayService {
	ResponseEntity<byte[]> proxyRouting(ProxyExchange<byte[]> proxyExchange, HttpServletRequest request,
			String httpMethod);
}
