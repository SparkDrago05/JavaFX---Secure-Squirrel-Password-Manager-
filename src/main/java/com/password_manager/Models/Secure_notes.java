package com.password_manager.Models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Secure_notes")

public class Secure_notes {
	 @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private int id;
	   private String title;
	   private String Text;
	   
	   
	   
	   
	public Secure_notes(int id, String title, String text) {
		super();
		this.id = id;
		this.title = title;
		Text = text;
	}
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getText() {
		return Text;
	}
	
	public void setText(String text) {
		Text = text;
	}

}


