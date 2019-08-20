package ibm.gse.kc.mq;

import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSContainerProducer extends JMSBase{
	private static final Logger logger = LoggerFactory.getLogger(JMSContainerProducer.class);
	

	
	public JMSContainerProducer() {
		init();
		try {
			context = buildJMSContext("JMSContainerProducer");
			destination = context.createQueue("queue:///" + getConfig().getProperties().getProperty(MQConfiguration.MQ_TO_QUEUENAME));
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		logger.info("Platform:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_PLATFORM));
		logger.info("Queue Manager:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_QUEUEMANAGER));
		logger.info("To Kafka Queue Name:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_TO_QUEUENAME));
		logger.info("From Kafka Queue Name:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_FROM_QUEUENAME));
		
		logger.info("channel:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_CHANNEL));
		logger.info("hostname:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_HOST));
		logger.info("port:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_PORT));
		logger.info("userid:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_USERID));
		logger.info("password:" + this.getConfig().getProperties().getProperty(MQConfiguration.MQ_PASSWORD));
	}
	
	public void sendMessage(String s) {
		logger.info(s);
		TextMessage message = context.createTextMessage(s);
		JMSProducer producer = context.createProducer();
		producer.send(destination, message);
	}
		
	
}
