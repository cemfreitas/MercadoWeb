package com.mercadodecreditos.dao;

import java.util.List;

import com.mercadodecreditos.model.City;

public interface CityDao {	
	public City getCityById(int id);
	public List<City> listAll();		
}
