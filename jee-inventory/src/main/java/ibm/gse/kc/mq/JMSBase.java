package ibm.gse.kc.mq;

import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class JMSBase {
	protected MQConfiguration config = MQConfiguration.getInstance();
	protected JMSContext context;
	protected Destination destination;
	
	public JMSContext buildJMSContext(String appName) throws JMSException {
    	// Create a connection factory
		JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
		JmsConnectionFactory cf = ff.createConnectionFactory();
		cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, getConfig().getProperties().getProperty(MQConfiguration.MQ_HOST));
		cf.setIntProperty(WMQConstants.WMQ_PORT, Integer.parseInt(getConfig().getProperties().getProperty(MQConfiguration.MQ_PORT)));
		cf.setStringProperty(WMQConstants.WMQ_CHANNEL,getConfig().getProperties().getProperty(MQConfiguration.MQ_CHANNEL));
		cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
		cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, getConfig().getProperties().getProperty(MQConfiguration.MQ_QUEUEMANAGER));
		cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, appName);
		cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
		cf.setStringProperty(WMQConstants.USERID, getConfig().getProperties().getProperty(MQConfiguration.MQ_USERID));
		cf.setStringProperty(WMQConstants.PASSWORD, getConfig().getProperties().getProperty(MQConfiguration.MQ_PASSWORD));

		// Create JMS objects
		JMSContext context = cf.createContext();
		return context;
    }
	
	public MQConfiguration getConfig() {
		return config;
	}
}
