package com.mercadodecreditos.controller;

import java.util.List;

import com.mercadodecreditos.dao.UserTypeDao;
import com.mercadodecreditos.dao.UserTypeDaoImpl;
import com.mercadodecreditos.model.UserType;

public class UserTypeController {
	
	public List<UserType> getAll(){
		List<UserType> userTypes = null;
		UserTypeDao userType = new UserTypeDaoImpl();
		
		userTypes = userType.listAll();
		return userTypes;
	}
	
	public UserType getUserTypeById(int id) {		
		UserTypeDao userType = new UserTypeDaoImpl();
		return userType.getUserTypeById(id);
	}
	
	public UserType getUserTypeByDesc(String desc) {
		UserTypeDao userType = new UserTypeDaoImpl();
		return userType.getUserTypeByDesc(desc);
	}

}
