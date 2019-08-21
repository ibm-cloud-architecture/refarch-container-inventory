package ut;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Optional;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ibm.gse.kc.dao.ContainerDAO;
import ibm.gse.kc.dao.ContainerDAOMockup;
import ibm.gse.kc.model.Container;
import ibm.gse.service.ApplicationException;
import ibm.gse.service.ContainerServiceImpl;

public class TestContainerService {
	
	private static ContainerDAO dao = new ContainerDAOMockup();
	private static ContainerServiceImpl serv = new ContainerServiceImpl(dao);
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Test
	public void shouldHaveContainerInInventoryAfterAddingIt() throws ApplicationException {
		Container c = new Container("C034","Iron10","IronContainer",100);
		Container r = serv.saveContainer(c);
		Assert.assertNotNull(r);
		Assert.assertTrue("IronContainer".equals(r.getType()));
		
		Optional<Container> cOut  = serv.getById(c.getContainerID());
		Assert.assertNotNull(cOut);
		Assert.assertTrue(cOut.get().getContainerID().equals(c.getContainerID()));
	}

	@Test
	public void shouldNotHaveContainerInInventory() {
		Optional<Container> response  = serv.getById("NoID");
		Assert.assertNotNull(response);
		Assert.assertTrue(!response.isPresent());
	}
	
	@Test
	public void shouldNotSaveSameContainer() {
		Container c = new Container("C033","Iron10","IronContainer",100);
		Container r = null;
		try {
			r = serv.saveContainer(c);
		} catch(ApplicationException e) {
			fail("Should not get an exception");
		}
		Assert.assertNotNull(r);
		c = new Container("C033","Iron11","IronContainer",100);
		try {
			r = serv.saveContainer(c);
		} catch(ApplicationException e) {
			// success
			return ;
		}
		fail("Should get an exception");

	}

	@Test
	public void shouldHaveContainers() {
		Container c = new Container("C010","Iron10","IronContainer",100);
		try {
			serv.saveContainer(c);
			c = new Container("C011","Iron10","IronContainer",100);
			serv.saveContainer(c);
			c = new Container("C012","Iron10","IronContainer",100);
			serv.saveContainer(c);
			Collection<Container> l = serv.getAllContainers();
			Assert.assertNotNull(l);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail("Should not get an exception");
		}
	}
	
	@Test
	public void clearAll() {
		serv.getDAO().stop();
	}
}
