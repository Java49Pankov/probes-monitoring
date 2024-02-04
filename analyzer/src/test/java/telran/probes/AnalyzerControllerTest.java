package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.probes.dto.*;

import telran.probes.service.SensorRangeProviderService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class AnalyzerControllerTest {
	private static final long SENSOR_ID = 123l;
	private static final int MIN_VALUE_NO_DEVIATION = 10;
	private static final int MAX_VALUE_NO_DEVIATION = 100;
	private static final int MIN_VALUE_DEVIATION = 60;
	private static final int MAX_VALUE_DEVIATION = 40;
	private static final float VALUE = 50f;

	private static final SensorRange SENSOR_RANGE_NO_DEVIATION = new SensorRange(MIN_VALUE_NO_DEVIATION,
			MAX_VALUE_NO_DEVIATION);
	private static final SensorRange SENSOR_RANGE_MIN_DEVIATION = new SensorRange(MIN_VALUE_DEVIATION,
			MAX_VALUE_NO_DEVIATION);
	private static final SensorRange SENSOR_RANGE_MAX_DEVIATION = new SensorRange(MIN_VALUE_NO_DEVIATION,
			MAX_VALUE_DEVIATION);
	private static final ProbeData probeData = new ProbeData(SENSOR_ID, VALUE, 0);
	private static final ProbeDataDeviation dataMinDeviation = new ProbeDataDeviation(SENSOR_ID, VALUE,
			VALUE - MIN_VALUE_DEVIATION, 0);
	private static final ProbeDataDeviation dataMaxDeviation = new ProbeDataDeviation(SENSOR_ID, VALUE,
			VALUE - MAX_VALUE_DEVIATION, 0);
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;

	String bindingNameProducer = "deviation-out-0";
	String bindingNameConsumer = "consumerProbeData-in-0";
	
	@MockBean
	Consumer<String> configChangeConsumer;
	@MockBean
	SensorRangeProviderService providerService;

	ObjectMapper mapper = new ObjectMapper();

	@Test
	void noDeviationTest() {
		when(providerService.getSensorRange(SENSOR_ID))
				.thenReturn(SENSOR_RANGE_NO_DEVIATION);
		producer.send(new GenericMessage<ProbeData>(probeData), bindingNameConsumer);

		Message<byte[]> message = consumer.receive(10, bindingNameProducer);
		assertNull(message);
	}

	@Test
	void minDeviationTest() throws Exception {
		when(providerService.getSensorRange(SENSOR_ID))
				.thenReturn(SENSOR_RANGE_MIN_DEVIATION);
		producer.send(new GenericMessage<ProbeData>(probeData), bindingNameConsumer);

		Message<byte[]> message = consumer.receive(10, bindingNameProducer);
		assertNotNull(message);

		ProbeDataDeviation actual = mapper.readValue(message.getPayload(), ProbeDataDeviation.class);
		assertEquals(dataMinDeviation, actual);
	}
	
	@Test
	void maxDeviationTest() throws Exception {
		when(providerService.getSensorRange(SENSOR_ID))
				.thenReturn(SENSOR_RANGE_MAX_DEVIATION);
		producer.send(new GenericMessage<ProbeData>(probeData), bindingNameConsumer);

		Message<byte[]> message = consumer.receive(10, bindingNameProducer);
		assertNotNull(message);

		ProbeDataDeviation actual = mapper.readValue(message.getPayload(), ProbeDataDeviation.class);
		assertEquals(dataMaxDeviation, actual);
	}
}
