package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.probes.api.UrlConstants;
import telran.probes.dto.*;
import telran.probes.exceptions.SensorNotFoundException;
import telran.probes.service.AdminConsoleService;
import static telran.probes.dto.ErrorMessages.*;

@WebMvcTest
class AdminConsoleControllerTest {
	private static final Long SENSOR_ID_NORMAL = 123l;
	private static final SensorRange SENSOR_RANGE_NORMAL = new SensorRange(10f, 100f);
	private static final SensorRange SENSOR_RANGE_WRONG = new SensorRange(100f, 10f);
	private static final String URL_UPDATE_RANGE = "http://localhost:8080" + UrlConstants.UPDATE_RANGE;
	private static final String URL_UPDATE_EMAILS = "http://localhost:8080" + UrlConstants.UPDATE_EMAILS;
	private static final Long SENSOR_ID_NOT_FOUND = 124l;
	@Autowired
	MockMvc mockMvc;
	@MockBean
	AdminConsoleService adminService;
	@Autowired
	ObjectMapper mapper;
	SensorRangeDto sensorRangeDtoNormal = new SensorRangeDto(SENSOR_ID_NORMAL, SENSOR_RANGE_NORMAL);
	String[] missingFieldsRangeMessages = { MISSING_SENSOR_ID, MISSING_SENSOR_RANGE };
	String[] missingFieldsEmailsMessages = { MISSING_SENSOR_ID, EMPTY_EMAILS };
	SensorRangeDto sensorRangeDtoMissingFields = new SensorRangeDto(null, null);
	SensorEmailsDto sensorEmailsDtoMissingFields = new SensorEmailsDto(null, null);
	SensorRangeDto sensorRangeWrongRange = new SensorRangeDto(SENSOR_ID_NORMAL, SENSOR_RANGE_WRONG);
	SensorEmailsDto sensorEmailsDtoNormal = new SensorEmailsDto(SENSOR_ID_NORMAL, new String[] { "kuku@gmail.com" });
	SensorEmailsDto sensorEmailsNotFound = new SensorEmailsDto(SENSOR_ID_NOT_FOUND, new String[] { "kuku@gmail.com" });
	private SensorRangeDto sensorRangeNotFound = new SensorRangeDto(SENSOR_ID_NOT_FOUND, SENSOR_RANGE_NORMAL);

	@Test
	@DisplayName(TestDisplayNames.UPDATE_RANGE_NORMAL)
	void updateRangeNormalFlow() throws Exception {
		when(adminService.updateSensorRange(sensorRangeDtoNormal)).thenReturn(sensorRangeDtoNormal);
		String sensorJSON = mapper.writeValueAsString(sensorRangeDtoNormal);
		String response = getResponseOk(sensorJSON, URL_UPDATE_RANGE);
		assertEquals(sensorJSON, response);
	}

	@Test
	@DisplayName(TestDisplayNames.UPDATE_EMAIL_NORMAL)
	void updateEmailNormalFlow() throws Exception {
		when(adminService.updateSensorEmails(sensorEmailsDtoNormal)).thenReturn(sensorEmailsDtoNormal);
		String sensorEmailsJSON = mapper.writeValueAsString(sensorEmailsDtoNormal);
		String response = getResponseOk(sensorEmailsJSON, URL_UPDATE_EMAILS);
		assertEquals(sensorEmailsJSON, response);
	}

	@Test
	@DisplayName(TestDisplayNames.UPDATE_EMAIL_MISSING_FIELDS)
	void updateEmailsMissingFields() throws Exception {
		String sensorEmailsJSON = mapper.writeValueAsString(sensorEmailsDtoMissingFields);
		String response = getResponseBadRequest(sensorEmailsJSON, URL_UPDATE_EMAILS);
		assertValidationError(missingFieldsEmailsMessages, response);
	}

	@Test
	@DisplayName(TestDisplayNames.UPDATE_RANGE_MISSING_FIELDS)
	void updateRangeMissingFields() throws Exception {
		String sensorRangeJSON = mapper.writeValueAsString(sensorRangeDtoMissingFields);
		String response = getResponseBadRequest(sensorRangeJSON, URL_UPDATE_RANGE);
		assertValidationError(missingFieldsRangeMessages, response);
	}

	@Test
	@DisplayName(TestDisplayNames.UPDATE_EMAIL_SENSOR_NOT_FOUND)
	void updateEmailsSensorNotFound() throws Exception {
		when(adminService.updateSensorEmails(sensorEmailsNotFound)).thenThrow(new SensorNotFoundException());
		String sensorRangeJSON = mapper.writeValueAsString(sensorEmailsNotFound);
		String response = getResponseNotFound(sensorRangeJSON, URL_UPDATE_EMAILS);
		assertEquals(SENSOR_NOT_FOUND, response);
	}

	@Test
	@DisplayName(TestDisplayNames.UPDATE_RANGE_SENSOR_NOT_FOUND)
	void updateRangeSensorNotFound() throws Exception {
		when(adminService.updateSensorRange(sensorRangeNotFound)).thenThrow(new SensorNotFoundException());
		String sensorRangeJSON = mapper.writeValueAsString(sensorRangeNotFound);
		String response = getResponseNotFound(sensorRangeJSON, URL_UPDATE_RANGE);
		assertEquals(SENSOR_NOT_FOUND, response);
	}

	private String getResponseNotFound(String content, String url) throws UnsupportedEncodingException, Exception {
		return mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(content)).andDo(print())
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
	}

	private String getResponseBadRequest(String sensorEmailsJSON, String url)
			throws UnsupportedEncodingException, Exception {
		return mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(sensorEmailsJSON))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
	}

	private void assertValidationError(String[] errorMessagesExpected, String response) {
		String[] responseTokens = response.split(";");
		Arrays.sort(errorMessagesExpected);
		Arrays.sort(responseTokens);
		assertArrayEquals(errorMessagesExpected, responseTokens);

	}

	private String getResponseOk(String sensorJSON, String url) throws UnsupportedEncodingException, Exception {
		return mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(sensorJSON)).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}

}