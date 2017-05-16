package com.mercadodecreditos.dao;

import java.util.List;

import com.mercadodecreditos.model.UserType;

public interface UserTypeDao {
	public List<UserType> listAll();
	public UserType getUserTypeById(int id);
	public UserType getUserTypeByDesc(String desc);
}
