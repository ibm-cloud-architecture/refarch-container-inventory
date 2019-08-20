package ibm.gse.kc.mq;

import javax.jms.JMSConsumer;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSContainerConsumer extends JMSBase {
	private static final Logger logger = LoggerFactory.getLogger(JMSContainerConsumer.class);
	
	public JMSContainerConsumer(){
		try {
			context = buildJMSContext("JMSContainerConsumer");
			destination = context.createQueue("queue:///" + getConfig().getProperties().getProperty(MQConfiguration.MQ_FROM_QUEUENAME));
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
    		JMSConsumer consumer = context.createConsumer(destination); // autoclosable
    		while (true) {
				String receivedMessage = consumer.receiveBody(String.class, 15000); // in ms or 15 seconds
				if (receivedMessage != null) {
					System.out.println("\nReceived message:\n" + receivedMessage);
					// TODO do something with the container message
				}
				
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
	}
	
}
