package ibm.gse.kc.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ibm.gse.kc.model.Container;
import ibm.gse.service.ApplicationException;

public class ContainerDAOMockup implements ContainerDAO {
	private static final Logger logger = LoggerFactory.getLogger(ContainerDAOMockup.class);
	private static Map<String,Container> inventory;
	
    public ContainerDAOMockup(){
    	inventory =  new ConcurrentHashMap<String,Container>(); 
    }

	@Override
	public Collection<Container> getContainers() {
		return Collections.unmodifiableCollection(inventory.values());
	}

	@Override
	public Container save(Container c) throws ApplicationException {
		logger.info("Adding Container id " + c.getContainerID());
        if (inventory.putIfAbsent(c.getContainerID(), c) != null) {
            throw new ApplicationException("Container already exists " + c.getContainerID());
        }
		return c;
	}
	
	@Override
	public Container update(Container c) throws ApplicationException{
		if (inventory.replace(c.getContainerID(), c) == null) {
	        throw new ApplicationException("container does not already exist " + c.getContainerID());
	    }
		return c;
	}

	@Override
	public Container getById(String containerID) {
		return inventory.get(containerID);
	}

	@Override
	public void stop() {
		inventory.clear();
		
	}

	@Override
	public void delete(String cid) {
		inventory.remove(cid);
		
	}

}
