package ibm.gse.kc.dao;

import java.util.Collection;

import ibm.gse.kc.model.Container;
import ibm.gse.service.ApplicationException;

public interface ContainerDAO {

	public Collection<Container> getContainers();
	
	public Container save(Container c) throws ApplicationException ;

	public Container update(Container c) throws ApplicationException ;

	public Container getById(String containerID);
	
	public void stop();
	
	public void delete(String cid);
}
