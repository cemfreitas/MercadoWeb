package com.mercadodecreditos.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.mercadodecreditos.model.DocumentType;
import com.mercadodecreditos.util.DataSource;

public class DocumentTypeDaoImpl implements DocumentTypeDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentType> listAll() {
		Session session = DataSource.getSessionFactory().openSession();
		List <DocumentType> lista;		
		lista = session.createQuery("from DocumentType").list();	
		if (session != null && session.isOpen()) {
			session.close();
		}
		return lista;
	}

	@Override
	public DocumentType getDocumentTypeById(int id) {
		DocumentType docType = null;
		Session session= null;
		Transaction trans = null;
		Query qry = null;
		try { 
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();			
			qry = session.createQuery("from DocumentType where xidDocumentType = " + id);
			docType = (DocumentType) qry.uniqueResult();
			session.flush();
			trans.commit();			
			return docType;
		} catch (HibernateException e) {
			System.out.println("Erro ao obter tipo crédito: " + e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public DocumentType getDocumentTypeByDesc(String desc) {
		DocumentType docType = null;
		Session session= null;
		Transaction trans = null;
		Query qry = null;
		try { 
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();			
			qry = session.createQuery("from DocumentType where description = " + desc);
			docType = (DocumentType) qry.uniqueResult();
			session.flush();
			trans.commit();			
			return docType;
		} catch (HibernateException e) {
			System.out.println("Erro ao obter tipo crédito: " + e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	
}
