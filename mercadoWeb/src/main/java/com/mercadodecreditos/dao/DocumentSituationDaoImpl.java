package com.mercadodecreditos.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.model.DocumentSituation;
import com.mercadodecreditos.util.DataSource;

public class DocumentSituationDaoImpl implements DocumentSituationDao {

	@Override
	public void save(DocumentSituation documentSituation) {
		Session session = null;
		Transaction trans = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			session.save(documentSituation);
			trans.commit();
		} catch (HibernateException e) {
			System.out.println("Erro ao salvar status do crédito: "
					+ e.getMessage());
			System.out.println(documentSituation.toString());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	@Override
	public List<DocumentSituation> getAllDocSituationByDoc(Document document) {
		return null;
	}

	@Override
	public DocumentSituation getLastDocSituationByDoc(Document document) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentSituation> getDocSituationByFractionDoc(
			Document mainDocument) {
		List<DocumentSituation> docsSituation = null;
		String qryStr = "select docSit from DocumentSituation as docSit join docSit.document doc where doc.mainDocument = :document";
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery(qryStr);
			qry.setParameter("document", mainDocument);
			docsSituation = qry.list();
			session.flush();
			trans.commit();
			return docsSituation;
		} catch (HibernateException e) {
			System.out
					.println("Erro ao listar Status do crédito por doc fracionado"
							+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

}
