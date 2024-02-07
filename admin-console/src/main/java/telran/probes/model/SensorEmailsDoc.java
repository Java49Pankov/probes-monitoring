package telran.probes.model;

import java.util.Arrays;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.probes.dto.SensorEmailsDto;

@Document(collection = "sensor-emails")
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SensorEmailsDoc {
	@Id
	long sensorId;
	String[] emails;

	public static SensorEmailsDoc of(SensorEmailsDto sensorEmailsDto) {
		String[] emailsDto = sensorEmailsDto.emails();
		return new SensorEmailsDoc(sensorEmailsDto.id(), Arrays.copyOf(emailsDto, emailsDto.length));
	}

	public SensorEmailsDto build() {
		return new SensorEmailsDto(sensorId, emails);
	}
}