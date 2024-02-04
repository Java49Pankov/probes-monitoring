package telran.probes.service;

import java.util.*;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.SensorRange;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorRangeProviderServiceImpl implements SensorRangeProviderService {
	@Value("${app.update.message.delimeter:#}")
	String delimeter;
	@Value("${app.update.token.range:range-update}")
	String rangeUpdateToken;

	final SensorRangeProviderConfiguration providerConfiguration;
	final RestTemplate restTemplate;

	@Getter
	HashMap<Long, SensorRange> mapRanges = new HashMap<>();

	@Override
	public SensorRange getSensorRange(long sensorId) {
		SensorRange range = mapRanges.get(sensorId);
		return range == null ? getRangeFromService(sensorId) : range;
	}

	@Bean
	Consumer<String> configChangeConsumer() {
		return this::checkConfigurationUpdate;
	}

	void checkConfigurationUpdate(String message) {
		String[] tokens = message.split(delimeter);
		if (tokens[0].equals(rangeUpdateToken)) {
			updateMapRanges(tokens[1]);
		}
	}

	private void updateMapRanges(String sensorIdStr) {
		long id = Long.parseLong(sensorIdStr);
		if (mapRanges.containsKey(id)) {
			mapRanges.put(id, getRangeFromService(id));
		}
	}

	private SensorRange getRangeFromService(long sensorId) {
		SensorRange result = null;
		try {
			ResponseEntity<SensorRange> responseEntity = restTemplate
					.exchange(getFullUrl(sensorId), HttpMethod.GET, null, SensorRange.class);
			result = responseEntity.getBody();
			mapRanges.put(sensorId, result);
		} catch (Exception e) {
			log.error("no sensor range provided for sensor {} reason: {}", sensorId, e.getMessage());
			result = getDefaultRange();
			log.warn("Taken default range {} - {}", result.minValue(), result.maxValue());
		}
		log.debug("Range for sensor {} is {}", sensorId, result);
		return result;
	}

	private String getFullUrl(long id) {
		String result = String.format("http://%s:%d%s/%d",
				providerConfiguration.getHost(),
				providerConfiguration.getPort(),
				providerConfiguration.getUrl(),
				id);
		log.debug("url: {}", result);
		return result;
	}

	private SensorRange getDefaultRange() {
		return new SensorRange(providerConfiguration.getMinDefaultValue(), providerConfiguration.getMaxDefaultValue());
	}
}
