package telran.probes.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.mvc.ProxyExchange;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GatewayServiceImpl implements GatewayService {
	@Value("#{${app.map.hosts.ports}}")
	Map<String, String> hostsPortsMap;

	@Override
	public ResponseEntity<byte[]> proxyRouting(ProxyExchange<byte[]> proxyExchange, HttpServletRequest request,
			String httpMethod) {
		String url = getUrl(request);
		ResponseEntity<byte[]> result = switch (httpMethod) {
		case "GET" -> proxyExchange.uri(url).get();
		case "PUT" -> proxyExchange.uri(url).put();
		case "POST" -> proxyExchange.uri(url).post();
		case "DELETE" -> proxyExchange.uri(url).delete();
		default -> throw new IllegalArgumentException("Wrong HTTP method");
		};
		return result;
	}

	private String getUrl(HttpServletRequest request) {
		String uri = request.getRequestURI(); // /accounts/admin@tel-ran.co.il
		log.debug("recieved uri: {}", uri);
		String firstURN = uri.split("/+")[1];
		log.debug("recieved first URN: {}", firstURN);
		String hostPort = hostsPortsMap.get(firstURN);
		log.debug("recieved hostPort {}", hostPort);
		String result = String.format("%s%s", hostPort, uri);
		log.debug("result URL is {}", result);
		return result;
	}

	@PostConstruct
	void logMap() {
		log.debug("hosts-ports map is {}", hostsPortsMap);
	}

}
