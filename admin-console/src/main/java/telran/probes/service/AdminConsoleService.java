package telran.probes.service;

import telran.probes.dto.*;

public interface AdminConsoleService {
	SensorRangeDto updateSensorRange(SensorRangeDto sensorRangeDto);

	SensorEmailsDto updateSensorEmails(SensorEmailsDto sensorEmails);
}