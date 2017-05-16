package com.mercadodecreditos.controller;

import java.util.List;

import com.mercadodecreditos.dao.CityDao;
import com.mercadodecreditos.dao.CityDaoImpl;
import com.mercadodecreditos.dao.StateDao;
import com.mercadodecreditos.dao.StateDaoImpl;
import com.mercadodecreditos.model.City;
import com.mercadodecreditos.model.State;

public class StateCityController {
		
	public City getCityById(int id) {
		CityDao city = new CityDaoImpl();		
		return city.getCityById(id);
	}
	
	public List<City> getCitiesByState(int id) {
		StateDao state = new StateDaoImpl();		
		List<City> cities = null;
		
		cities = state.getCitiesByState(state.getStateById(id));		
		
		return cities;
	}
	
	public List<State> getAllStates(){
		List<State> states = null;
		StateDao state = new StateDaoImpl();
		
		states = state.listAll();
		return states;
	}
	
	public State getStateById(int id){
		StateDao state = new StateDaoImpl();
		return state.getStateById(id);
	}
		
}
