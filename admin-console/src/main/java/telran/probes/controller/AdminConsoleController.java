package telran.probes.controller;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import telran.probes.api.*;
import telran.probes.dto.*;
import telran.probes.service.AdminConsoleService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminConsoleController {
	final AdminConsoleService adminConsoleService;

	@PutMapping(UrlConstants.UPDATE_RANGE)
	SensorRangeDto updateSensorRange(@RequestBody @Valid SensorRangeDto sensorRangeDto) {
		log.debug("controller: updateSensorRange received {}", sensorRangeDto);
		SensorRangeDto res = adminConsoleService.updateSensorRange(sensorRangeDto);
		return res;
	}

	@PutMapping(UrlConstants.UPDATE_EMAILS)
	SensorEmailsDto updateSensorEmails(@RequestBody @Valid SensorEmailsDto sensorEmailsDto) {
		log.debug("controller: updateSensorEmails received {}", sensorEmailsDto);
		SensorEmailsDto res = adminConsoleService.updateSensorEmails(sensorEmailsDto);
		return res;
	}
}