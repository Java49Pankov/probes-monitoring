package telran.probes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.context.annotation.Import;

import telran.probes.dto.SensorEmailsDto;
import telran.probes.dto.SensorRange;
import telran.probes.model.SensorEmailsDoc;
import telran.probes.repo.SensorEmailsRepo;
import telran.probes.repo.SensorRangesRepo;
import telran.probes.service.AdminConsoleService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class AdminConsoleServiceTest {
	private static final Long SENSOR_ID_1 = 123l;
	private static final String[] EMAILS1 = {
			"service1@gmail.com"
	};
	private static final String[] EMAILS2 = {
			"service2@gmail.com"
	};
	static final SensorRange sensorRange1 = new SensorRange(10f, 100f);
	static final SensorRange sensorRange2 = new SensorRange(100f, 200f);
	@Autowired
	AdminConsoleService adminService;
	@Autowired
	SensorEmailsRepo sensorEmailsRepo;
	@Autowired
	SensorRangesRepo sensorRangesRepo;
	@Autowired
	InputDestination consumer;
	@Value("${app.sensors.update.binding.name:sensorsUpdate-out-0}")
	String bindingName;
	SensorEmailsDto sensorEmailDto2 = new SensorEmailsDto(SENSOR_ID_1, EMAILS2);
	SensorEmailsDoc emailsDoc1 = SensorEmailsDoc.of(new SensorEmailsDto(SENSOR_ID_1, EMAILS1));

	@BeforeEach
	void setUp() {
		sensorEmailsRepo.deleteAll();
		sensorEmailsRepo.save(emailsDoc1);
	}

	@Test
	@DisplayName(TestDisplayNames.UPDATE_EMAIL_NORMAL)
	void updateEmailsNormal() {
		SensorEmailsDto actual = adminService.updateSensorEmails(sensorEmailDto2);
		assertEquals(SENSOR_ID_1, actual.id());
		assertArrayEquals(EMAILS2, actual.emails());
	}

}