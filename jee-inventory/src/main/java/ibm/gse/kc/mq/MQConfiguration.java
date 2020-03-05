package ibm.gse.kc.mq;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

/**
 * Define properties loaded from config files.
 *
 * Define factory method to create Queue manager
 * @author jerome boyer
 *
 */
public class MQConfiguration {
	public static final String MQ_PLATFORM = "mq.platform";
	public static final String MQ_QUEUEMANAGER = "mq.queueManagerName";
	public static final String MQ_TO_QUEUENAME = "mq.to.kafka.queueName";
	public static final String MQ_FROM_QUEUENAME = "mq.from.kafka.queueName";
	public static final String MQ_HOST = "mq.hostname";
	public static final String MQ_PORT = "mq.listenerPort";
	public static final String MQ_CHANNEL = "mq.applicationChannelName";
	public static final String MQ_USERID = "mq.userid";
	public static final String MQ_PASSWORD = "mq.password";

	public static MQConfiguration instance;

	public static synchronized MQConfiguration instance() {
		if (instance == null) {
			instance = new MQConfiguration();
			instance.loadProperties();
		}
		return instance;
	}
	public Properties properties = new Properties();

	public void loadProperties() {
		loadPropertiesFromStream(getClass().getClassLoader().getResourceAsStream("config.properties"));
	}

	private void loadPropertiesFromStream(InputStream input){
		try {
			properties.load(input);
			Map<String, String> env = System.getenv();
			if (env.get("MQ_HOST") != null) {
				properties.setProperty(MQ_HOST,env.get("MQ_HOST"));
			}
			if (env.get("MQ_PORT") != null) {
				properties.setProperty(MQ_PORT,env.get("MQ_PORT"));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			setDefaults();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void loadProperties(String fn) {
		try {
			loadPropertiesFromStream(new FileInputStream(fn));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			setDefaults();
		}
	}

	private  void setDefaults() {
		properties.setProperty(MQ_PLATFORM,"IBM MQ");
		properties.setProperty(MQ_QUEUEMANAGER,"LQM1");
		properties.setProperty(MQ_TO_QUEUENAME,"TO.CONTAINERS");
		properties.setProperty(MQ_FROM_QUEUENAME,"FROM.CONTAINERS");
		properties.setProperty(MQ_HOST,"localhost");
		properties.setProperty(MQ_PORT,"1414");
		properties.setProperty(MQ_CHANNEL, "DEV.APP.SVRCONN");
		properties.setProperty(MQ_USERID, "app");
		properties.setProperty(MQ_PASSWORD, "admin01");
	}

	public Properties getProperties() {
		return properties;
	}

	public MQQueueManager createQueueManager() throws MQException {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
        props.put(MQConstants.CHANNEL_PROPERTY, getProperties().getProperty(MQConfiguration.MQ_CHANNEL));
      	int integerPortNumber = Integer.parseInt(getProperties().getProperty(MQConfiguration.MQ_PORT));
        props.put(MQConstants.PORT_PROPERTY, integerPortNumber);
      	props.put(MQConstants.HOST_NAME_PROPERTY, getProperties().getProperty(MQConfiguration.MQ_HOST));

        props.put(MQConstants.USER_ID_PROPERTY, getProperties().getProperty(MQConfiguration.MQ_USERID));
        props.put(MQConstants.PASSWORD_PROPERTY, getProperties().getProperty(MQConfiguration.MQ_PASSWORD));
        props.put(MQConstants.USE_MQCSP_AUTHENTICATION_PROPERTY, true);

        MQQueueManager qMgr = new MQQueueManager(getProperties().getProperty(MQConfiguration.MQ_QUEUEMANAGER), props);
        return qMgr;
	}
}
