package com.mercadodecreditos.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.mercadodecreditos.model.Situation;
import com.mercadodecreditos.util.DataSource;

public class SituationDaoImpl implements SituationDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<Situation> listAll() {
		Session session = DataSource.getSessionFactory().openSession();
		List <Situation> lista;		
		lista = session.createQuery("from Situation").list();	
		if (session != null && session.isOpen()) {
			session.close();
		}
		return lista;
	}

	@Override
	public Situation getSituationById(int id) {
		Situation situation = null;
		Session session= null;
		Transaction trans = null;
		Query qry = null;
		try { 
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();			
			qry = session.createQuery("from Situation as S where S.xidSituation = " + id);
			situation = (Situation) qry.uniqueResult();
			session.flush();
			trans.commit();			
			return situation;
		} catch (HibernateException e) {
			System.out.println("Erro ao obter situação do usuário: " + e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

}
