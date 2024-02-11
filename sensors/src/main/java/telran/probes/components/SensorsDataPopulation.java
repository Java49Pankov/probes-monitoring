package telran.probes.components;

import java.util.*;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import telran.probes.configuration.*;
import telran.probes.model.*;
import telran.probes.repo.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class SensorsDataPopulation {
	final SensorEmailsRepo sensorEmailsRepo;
	final SensorRangesRepo sensorRangeRepo;
	final SensorsConfiguration sensorsConfiguration;

	@PostConstruct
	void dbPopulation() {
		Map<Long, SensorData> sensorDataMap = sensorsConfiguration.getSensorsDataMap();
		List<SensorEmailsDoc> sensorsEmails = sensorEmailsRepo.findAll();
		List<SensorRangeDoc> sensorsRanges = sensorRangeRepo.findAll();
		if (sensorsEmails.isEmpty()) {
			populateEmails(sensorDataMap);
		} else {
			log.debug("Collection  sensors-emails already contains {} documents", sensorsEmails.size());
		}
		if (sensorsRanges.isEmpty()) {
			populateRanges(sensorDataMap);
		} else {
			log.debug("Collection  sensors-ranges already contains {} documents", sensorsRanges.size());
		}

	}

	private void populateRanges(Map<Long, SensorData> sensorDataMap) {
		List<SensorRangeDoc> documents = sensorDataMap.entrySet().stream()
				.map(e -> new SensorRangeDoc(e.getKey(), e.getValue().minValue(), e.getValue().maxValue()))
				.toList();
		sensorRangeRepo.saveAll(documents);
		log.debug("{} has been saved", documents.size());
	}

	private void populateEmails(Map<Long, SensorData> sensorDataMap) {
		List<SensorEmailsDoc> documents = sensorDataMap.entrySet().stream()
				.map(e -> new SensorEmailsDoc(e.getKey(), e.getValue().emails()))
				.toList();
		sensorEmailsRepo.saveAll(documents);
		log.debug("{} has been saved", documents.size());

	}
}