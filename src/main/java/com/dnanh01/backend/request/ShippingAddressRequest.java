package com.dnanh01.backend.request;

public class ShippingAddressRequest {

	private String firstName;

    private String lastName;
	
    private String streetAddress;

    private String city;

    private String huyen;
    
    private String mobile;

    private String xa;

	public ShippingAddressRequest() {

	}

	

	public ShippingAddressRequest(String firstName, String lastName, String streetAddress, String city, String huyen,
			String mobile, String xa) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.streetAddress = streetAddress;
		this.city = city;
		this.huyen = huyen;
		this.mobile = mobile;
		this.xa = xa;
	}



	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHuyen() {
		return huyen;
	}

	public void setHuyen(String huyen) {
		this.huyen = huyen;
	}

	public String getXa() {
		return xa;
	}

	public void setXa(String xa) {
		this.xa = xa;
	}

	
   

}
