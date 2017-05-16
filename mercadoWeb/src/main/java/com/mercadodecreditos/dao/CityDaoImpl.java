package com.mercadodecreditos.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mercadodecreditos.util.DataSource;
import com.mercadodecreditos.model.City;

public class CityDaoImpl implements CityDao {	

	public City getCityById(int id) {
		City c = null;
		Session session= null;
		Transaction trans = null;
		Query qry = null;
		try { 
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();			
			qry = session.createQuery("from City where xidCity = " + id);
			c = (City) qry.uniqueResult();
			session.flush();
			trans.commit();			
			return c;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar Cidade : " + e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<City> listAll() {
		Session session = DataSource.getSessionFactory().openSession();
		List <City> lista;		
		lista = session.createQuery("from City").list();	
		if (session != null && session.isOpen()) {
			session.close();
		}
		return lista;
	}

}
