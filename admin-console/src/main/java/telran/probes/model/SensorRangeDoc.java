package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.probes.dto.*;

@Document(collection = "sensor-ranges")
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SensorRangeDoc {
	@Id
	long sensorId;
	float minValue;
	float maxValue;

	public static SensorRangeDoc of(SensorRangeDto sensorRangeDto) {
		SensorRange sensorRange = sensorRangeDto.sensorRange();
		return new SensorRangeDoc(sensorRangeDto.id(), sensorRange.minValue(),
				sensorRange.maxValue());
	}

	public SensorRangeDto build() {
		return new SensorRangeDto(sensorId, new SensorRange(minValue, maxValue));
	}
}