package com.mercadodecreditos.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.mercadodecreditos.model.UserType;
import com.mercadodecreditos.util.DataSource;

public class UserTypeDaoImpl implements UserTypeDao {

	
	@SuppressWarnings("unchecked")
	public List<UserType> listAll() {
		Session session = DataSource.getSessionFactory().openSession();
		List <UserType> lista;		
		lista = session.createQuery("from UserType").list();	
		if (session != null && session.isOpen()) {
			session.close();
		}
		return lista;
	}

	
	public UserType getUserTypeById(int id) {
		UserType u = null;
		Session session= null;
		Transaction trans = null;
		Query qry = null;
		try { 
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();			
			qry = session.createQuery("from UserType where xidUserType = " + id);
			u = (UserType) qry.uniqueResult();
			session.flush();
			trans.commit();			
			return u;
		} catch (HibernateException e) {
			System.out.println("Erro ao obter tipo usuário: " + e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	
	public UserType getUserTypeByDesc(String desc) {
		UserType u = null;
		Session session= null;
		Transaction trans = null;
		Query qry = null;
		try { 
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();			
			qry = session.createQuery("from UserType where description = " + desc);
			u = (UserType) qry.uniqueResult();
			session.flush();
			trans.commit();			
			return u;
		} catch (HibernateException e) {
			System.out.println("Erro ao tipo usuário: " + e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}
}
