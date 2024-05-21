package com.password_manager.Models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name ="Personal_info")

public class Personal_info {
	@Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private int id;
	   private String name;
	   private String gender;
	   private Date birthday;
	   private String email;
	   private String phone;
	   private String addresss;
	   private String city;
	   private String province;
	   private String country;
	   private String postalcode;
	   private String notes;
	   
	   
	   
	public Personal_info(int id, String name, String gender, Date birthday, String email, String phone, String addresss,
			String city, String province, String country, String postalcode, String notes) {
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.birthday = birthday;
		this.email = email;
		this.phone = phone;
		this.addresss = addresss;
		this.city = city;
		this.province = province;
		this.country = country;
		this.postalcode = postalcode;
		this.notes = notes;
	}
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public Date getBirthday() {
		return birthday;
	}
	
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getAddresss() {
		return addresss;
	}
	
	public void setAddresss(String addresss) {
		this.addresss = addresss;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getProvince() {
		return province;
	}
	
	public void setProvince(String province) {
		this.province = province;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getPostalcode() {
		return postalcode;
	}
	
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}


	   

	 

}
