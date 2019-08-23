package it;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import ibm.gse.kc.model.Container;
import ibm.gse.kc.mq.JMSContainerProducer;
import ibm.gse.kc.mq.MQConfiguration;

public class ProducerContainerMessageTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void shouldHaveUseEnvVar() {
		 Map<String, String> env = System.getenv();
		 MQConfiguration config = MQConfiguration.instance();
		 Assert.assertTrue(config.getProperties().getProperty(MQConfiguration.MQ_HOST).equals("localhost"));
	}
	
	
	@Test
	public void shouldSendAMessageToMQ() {
		Container c = new Container("C034","Iron10","IronContainer",100);
		Gson parser = new Gson();
		JMSContainerProducer producer = new JMSContainerProducer();
		producer.sendMessage(parser.toJson(c));
	}

}
