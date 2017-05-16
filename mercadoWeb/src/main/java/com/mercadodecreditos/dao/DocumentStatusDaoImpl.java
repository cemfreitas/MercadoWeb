package com.mercadodecreditos.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.mercadodecreditos.model.DocumentStatus;
import com.mercadodecreditos.util.DataSource;

public class DocumentStatusDaoImpl implements DocumentStatusDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentStatus> listAll() {
		Session session = DataSource.getSessionFactory().openSession();
		List <DocumentStatus> lista;		
		lista = session.createQuery("from DocumentStatus").list();	
		if (session != null && session.isOpen()) {
			session.close();
		}
		return lista;
	}

	@Override
	public DocumentStatus getDocumentStatusById(int id) {
		DocumentStatus docStatus = null;
		Session session= null;
		Transaction trans = null;
		Query qry = null;
		try { 
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();			
			qry = session.createQuery("from DocumentStatus as d where d.xidDocumentStatus = " + id);
			docStatus = (DocumentStatus) qry.uniqueResult();
			session.flush();
			trans.commit();			
			return docStatus;
		} catch (HibernateException e) {
			System.out.println("Erro ao obter status do crédito: " + e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public DocumentStatus getDocumentStatusByDesc(String desc) {
		DocumentStatus docStatus = null;
		Session session= null;
		Transaction trans = null;
		Query qry = null;
		try { 
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();			
			qry = session.createQuery("from DocumentStatus where description = " + desc);
			docStatus = (DocumentStatus) qry.uniqueResult();
			session.flush();
			trans.commit();			
			return docStatus;
		} catch (HibernateException e) {
			System.out.println("Erro ao obter status do crédito: " + e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

}
