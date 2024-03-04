package telran.probes.controller;

import org.springframework.cloud.gateway.mvc.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import telran.probes.service.GatewayService;

@RestController
@RequiredArgsConstructor
public class GatewayController {
	final GatewayService gatewayService;

	@GetMapping("/**")
	ResponseEntity<byte[]> getProxy(ProxyExchange<byte[]> exchange, HttpServletRequest request) {
		return gatewayService.proxyRouting(exchange, request, "GET");
	}
	
	@PutMapping("/**")
	ResponseEntity<byte[]> putProxy(ProxyExchange<byte[]> exchange, HttpServletRequest request) {
		return gatewayService.proxyRouting(exchange, request, "PUT");
	}
	
	@PostMapping("/**")
	ResponseEntity<byte[]> postProxy(ProxyExchange<byte[]> exchange, HttpServletRequest request) {
		return gatewayService.proxyRouting(exchange, request, "POST");
	}
	
	@DeleteMapping("/**")
	ResponseEntity<byte[]> deleteProxy(ProxyExchange<byte[]> exchange, HttpServletRequest request) {
		return gatewayService.proxyRouting(exchange, request, "DELETE");
	}
}
