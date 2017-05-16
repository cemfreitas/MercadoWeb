package com.mercadodecreditos.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mercadodecreditos.model.City;
import com.mercadodecreditos.model.State;
import com.mercadodecreditos.util.DataSource;

public class StateDaoImpl implements StateDao {

	public State getStateById(int id) {
		State s = null;
		Session session= null;
		Transaction trans = null;
		Query qry = null;
		try { 
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();			
			qry = session.createQuery("from State where xidState = " + id);
			s = (State) qry.uniqueResult();
			session.flush();
			trans.commit();			
			return s;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar Estado : " + e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")	
	public List<State> listAll() {
		Session session = DataSource.getSessionFactory().openSession();
		List <State> lista;		
		lista = session.createQuery("from State").list();	
		if (session != null && session.isOpen()) {
			session.close();
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<City> getCitiesByState(State state) {
		String qryStr="from City as c where c.state = :state";
		List<City> cities=null;
		Session session= null;
		Transaction trans = null;
		Query qry = null;
		try { 
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();			
			qry = session.createQuery(qryStr);
			qry.setParameter("state", state);
			cities = qry.list();
			session.flush();
			trans.commit();			
			return cities;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar Estado : " + e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	
	public State getStateByDesc(String desc) {
		State state;
		String qryStr="from State as s where s.description = :state";		
		Session session= null;
		Transaction trans = null;
		Query qry = null;
		try { 
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();			
			qry = session.createQuery(qryStr);
			qry.setParameter("state", desc);
			state = (State) qry.uniqueResult();
			session.flush();
			trans.commit();			
			return state;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar Estado : " + e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

}
