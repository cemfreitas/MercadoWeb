package com.mercadodecreditos.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DDDList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8204663950518020521L;
	@Id 	
	private String ddd;

	public String getDdd() {
		return ddd;
	}

}
