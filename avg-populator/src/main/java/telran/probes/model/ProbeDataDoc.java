package telran.probes.model;

import java.time.*;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;
import telran.probes.dto.ProbeData;

@Document(collection = "avg_values")
@ToString
@Getter
@NoArgsConstructor
public class ProbeDataDoc {
	long sensorID;
	LocalDateTime timestamp;
	Float value;

	public ProbeDataDoc(ProbeData probeData) {
		sensorID = probeData.sensorId();
		Instant instant = Instant.ofEpochMilli(probeData.timestamp());
		timestamp = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		value = probeData.value();
	}
}