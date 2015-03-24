package data;

import java.io.Serializable;

/**
 * This class represents a paket that is send by nodes in the network
 * @author Thomas
 *
 */
public class Paket implements Serializable {
	
	private int id;
	private String payload;
	
	
	public Paket(int id, String payload) {
		super();
		this.id = id;
		this.payload = payload;
	}
	


	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the payload
	 */
	public String getPayload() {
		return payload;
	}
	/**
	 * @param payload the payload to set
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}

}
