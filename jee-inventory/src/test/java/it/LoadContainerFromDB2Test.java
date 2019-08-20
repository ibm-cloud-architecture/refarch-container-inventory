package it;

import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ibm.gse.kc.dao.ContainerDAO;
import ibm.gse.kc.dao.ContainerRepository;
import ibm.gse.kc.model.Container;
import ibm.gse.service.ApplicationException;

public class LoadContainerFromDB2Test {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	ContainerDAO dao;

	@Test
	public void shouldGetTheContainers() {
		try
	    {
		  dao = new ContainerRepository();
		  Collection<Container> l = dao.getContainers();
		  Assert.assertNotNull(l);
		  Assert.assertTrue(l.size() > 8);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	fail("Exception " );
	    } finally {
	    	dao.stop();
	    }
	
	}
	

   @Test
   public void shouldDeleteTestData() {
	   dao = new ContainerRepository();
	   Container cOut = dao.getById("test01");
	   if(cOut != null) dao.delete("test01");
   }
   
   @Test
   public void shouldSaveNewContainer() {
	   Container c = new Container();
	   c.setContainerID("test01");
	   c.setBrand("testbrand");
	   c.setCapacity(10);
	   c.setType("Reefer");
	   dao = new ContainerRepository();
	   try {
		Container cOut = dao.save(c);
		Assert.assertNotNull(cOut);
		Assert.assertNotNull(cOut.getContainerID());
		Container cOut2 = dao.getById(cOut.getContainerID());
		Assert.assertTrue("testbrand".equals(cOut2.getBrand()));
		dao.delete("test01");
	} catch (ApplicationException e) {
		e.printStackTrace();
		fail("Exception " );
	} finally {
    	dao.stop();
    }
	   
	   
   }
}
