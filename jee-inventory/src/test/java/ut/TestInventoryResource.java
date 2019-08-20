package ut;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ibm.gse.app.rest.InventoryResource;
import ibm.gse.kc.dao.ContainerDAO;
import ibm.gse.kc.dao.ContainerDAOMockup;
import ibm.gse.kc.model.Container;
import ibm.gse.service.ContainerService;

public class TestInventoryResource {
	
	private static ContainerDAO dao = ContainerDAOMockup.instance();
	private static ContainerService serv = new ContainerService(dao);
	private static InventoryResource res = new InventoryResource(serv);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Test
	public void shouldHaveContainerInInventoryAfterAddingIt() {
		Container c = new Container("C034","Iron10","IronContainer",100);
		Response r = res.postContainer(c);
		Assert.assertNotNull(r);
		Assert.assertTrue(r.getStatus() == 200);
		
		Response response  = res.getById(c.getContainerID());
		Assert.assertNotNull(response);
		Container out = response.readEntity(Container.class);
		Assert.assertNotNull(out);
		Assert.assertTrue(out.getContainerID().equals(c.getContainerID()));
	}

	@Test
	public void shouldNotHaveContainerInInventory() {
		Response response  = res.getById("NoID");
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getStatus() == 404);
		
	}
	
	@Test
	public void shouldNotSaveSameContainer() {
		Container c = new Container("C033","Iron10","IronContainer",100);
		Response r = res.postContainer(c);
		Assert.assertNotNull(r);
		Assert.assertTrue(r.getStatus() == 200);
		c = new Container("C033","Iron11","IronContainer",100);
		r = res.postContainer(c);
		Assert.assertNotNull(r);
		Assert.assertTrue(r.getStatus() == 409);

	}

	@Test
	public void shouldHaveContainers() {
		Container c = new Container("C010","Iron10","IronContainer",100);
		res.postContainer(c);
		c = new Container("C011","Iron10","IronContainer",100);
		res.postContainer(c);
		c = new Container("C012","Iron10","IronContainer",100);
		res.postContainer(c);
		Response r = res.getContainers();
		Assert.assertNotNull(r);
		Assert.assertTrue(r.getStatus() == 200);

	}
}
