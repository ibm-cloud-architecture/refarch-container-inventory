package ibm.gse.kc.model;

public class Container {
	protected String containerID;
	protected String type;
	protected String status;
	protected String brand;
	protected int capacity;
	
	public Container(String cid, String brand, String type, int capacity) {
		this.containerID = cid;
		this.type = type;
		this.brand = brand;
		this.capacity = capacity;
	}
	
	public Container() {} // needed for json deserialization
	
	public String toString(){
		return "Container= " + getContainerID()
			+ " brand= " + getBrand()
			+ " type= " + getType()
		 	+ " capacity= " + getCapacity()
		 	+ " status= " + getStatus();
	}

	public String getContainerID() {
		return containerID;
	}

	public void setContainerID(String containerID) {
		this.containerID = containerID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
