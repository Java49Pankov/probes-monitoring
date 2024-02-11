package telran.probes.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.*;
import telran.probes.model.*;
import telran.probes.repo.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class AvgValueServiceImpl implements AvgValueService {
	final ProbesListRepo probesListRepo;

	@Value("${app.average.reducing.size}")
	int reducingSize;

	@Override
	public Long getAvgValue(ProbeData probeData) {
		long sensorId = probeData.sensorId();
		Long result = null;
		ProbesList probesList = probesListRepo.findById(sensorId).orElse(null);
		if (probesList == null || probesList.getValues() == null) {
			probesList = new ProbesList(sensorId);
			log.debug("probesList for sensor {} doesn't exist or no values", sensorId);
		}
		List<Float> values = probesList.getValues();
		values.add(probeData.value());
		if (values.size() >= reducingSize) {
			log.debug("reducing for sensor {}", sensorId);
			result = (long) values
					.stream()
					.mapToLong(v -> v.longValue())
					.average()
					.orElse(0);
			values.clear();
		}
		probesListRepo.save(probesList);
		log.debug("saved probesList {}", probesList);
		return result;
	}

}
