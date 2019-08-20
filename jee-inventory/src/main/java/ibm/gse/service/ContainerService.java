package ibm.gse.service;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ibm.gse.kc.dao.ContainerDAO;
import ibm.gse.kc.dao.ContainerDAOMockup;
import ibm.gse.kc.model.Container;




public class ContainerService {
	private static final Logger logger = LoggerFactory.getLogger(ContainerService.class);
	private ContainerDAO dao;
	

	public ContainerService() {
		this.dao = ContainerDAOMockup.instance();
	}
	
    public ContainerService(ContainerDAO dao2) {
		this.dao = dao2;
	}


    public Collection<Container>  getAllContainers() {
       return dao.getContainers();
    }

  	public Container saveContainer(Container c) throws ApplicationException {
    	c.setStatus("New");
    	try {
    		dao.save(c);
    		// JMS producer call here
    	} catch (Exception e) {
    		 throw new ApplicationException("Save container error - conflict"); 
    	}
		return c;
	}
	
	
  	public Optional<Container> getById(String containerID)  {
		logger.info("ContainerService.getById(" + containerID + ")");
		return Optional.ofNullable(dao.getById(containerID));

	}

}
