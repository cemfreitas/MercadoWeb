package com.mercadodecreditos.dao;

import java.util.List;

import com.mercadodecreditos.model.City;
import com.mercadodecreditos.model.State;

public interface StateDao {
	public State getStateById(int id);
	public State getStateByDesc(String desc);
	public List<State> listAll();
	public List<City> getCitiesByState(State state);
}
