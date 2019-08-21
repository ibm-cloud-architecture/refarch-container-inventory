package ibm.gse.service;

import java.util.Collection;
import java.util.Optional;

import ibm.gse.kc.dao.ContainerDAO;
import ibm.gse.kc.model.Container;

public interface ContainerService {

	public Container saveContainer(Container c) throws ApplicationException;
	
	public Collection<Container>  getAllContainers();
	
	public Optional<Container> getById(String containerID);

	public ContainerDAO getDAO();
}