package com.mercadodecreditos.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.mercadodecreditos.model.City;
import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.model.DocumentFraction;
import com.mercadodecreditos.model.DocumentSituation;
import com.mercadodecreditos.model.DocumentStatus;
import com.mercadodecreditos.model.DocumentType;
import com.mercadodecreditos.model.State;
import com.mercadodecreditos.model.User;
import com.mercadodecreditos.util.ApplicationException;
import com.mercadodecreditos.util.DataSource;
import com.mercadodecreditos.util.SystemDomain;

public class DocumentDaoImpl implements DocumentDao {

	@Override
	public void save(Document document) throws ApplicationException {
		Session session = null;
		Transaction trans = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			session.save(document);
			session.save(generateFirstSituation(document));
			trans.commit();
		} catch (HibernateException e) {
			System.out.println("Erro ao cadastrar crédito: " + e.getMessage());
			System.out.println(document.toString());
			throw new ApplicationException(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	public void save(Document document, List<DocumentFraction> fracts)
			throws ApplicationException {
		Session session = null;
		Transaction trans = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			session.save(document);
			session.save(generateFirstSituation(document));
			for (DocumentFraction docs : fracts) {
				session.save(docs);
			}
			trans.commit();
		} catch (HibernateException e) {
			System.out.println("Erro ao cadastrar crédito fracionado: "
					+ e.getMessage());
			System.out.println(document.toString());
			throw new ApplicationException(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	private DocumentSituation generateFirstSituation(Document document) {
		DocumentSituation documentSituation = new DocumentSituation();
		documentSituation.setDescription("Pendente de aprovação");
		documentSituation.setDocument(document);
		documentSituation.setDocumentSituationDate(new Date());
		documentSituation.setDocumentStatus(getPendingStatus());

		return documentSituation;
	}

	private DocumentStatus getPendingStatus() {
		DocumentStatus documentStatus = null;
		DocumentStatusDao documentStatusDao = new DocumentStatusDaoImpl();
		documentStatus = documentStatusDao
				.getDocumentStatusById(SystemDomain.documentStatusPending);
		return documentStatus;
	}

	@Override
	public void update(Document document) {
		Session session = null;
		Transaction trans = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			session.update(document);
			trans.commit();
		} catch (HibernateException e) {
			System.out.println("Erro ao atualizar crédito: " + e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	@Override
	public Document getDocumentById(long id) {
		Document doc = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session
					.createQuery("from Document where xidDocument = " + id);
			doc = (Document) qry.uniqueResult();
			session.flush();
			trans.commit();
			return doc;
		} catch (HibernateException e) {
			System.out.println("Erro ao obter document por id: "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getDocumentByUser(User user) {
		String qryStr = "from Document as doc where doc.user = :user";
		List<Document> docs = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery(qryStr);
			qry.setParameter("user", user);
			docs = qry.list();
			session.flush();
			trans.commit();
			return docs;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar créditos por usuário : "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getDocumentByState(State state) {
		String qryStr = "from Document as doc where doc.state = :state";
		List<Document> docs = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery(qryStr);
			qry.setParameter("state", state);
			docs = qry.list();
			session.flush();
			trans.commit();
			return docs;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar créditos por estado : "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getDocumentByCity(City city) {
		String qryStr = "from Document as doc where doc.city = :city";
		List<Document> docs = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery(qryStr);
			qry.setParameter("city", city);
			docs = qry.list();
			session.flush();
			trans.commit();
			return docs;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar créditos por cidade : "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> searchDocument(DocumentType[] documentType,
			State state, City city, BigDecimal valIni, BigDecimal valEnd,
			Date deadLineIni, Date deadLineEnd) {
		List<Document> docs = null;
		Session session = null;
		Transaction trans = null;
		Criteria cr = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();

			cr = session.createCriteria(Document.class);

			cr.add(Restrictions
					.disjunction()
					.add(Restrictions.eq("lastDocumentStatus",
							getStatus(SystemDomain.documentStatusReady)))
					.add(Restrictions
							.eq("lastDocumentStatus",
									getStatus(SystemDomain.documentStatusInNegotiation))));

			if (documentType != null) {
				cr.add(Restrictions.in("documentType", documentType));
			}
			if (state != null) {
				cr.add(Restrictions.eq("state", state));
			}
			if (city != null) {
				cr.add(Restrictions.eq("city", city));
			}
			if (valIni != null && valEnd == null) {
				cr.add(Restrictions.ge("price", valIni));
			}
			if (valEnd != null && valIni == null) {
				cr.add(Restrictions.le("price", valEnd));
			}

			if (valIni != null && valEnd != null) {
				cr.add(Restrictions.between("price", valIni, valEnd));
			}
			if (deadLineIni != null && deadLineEnd == null) {
				cr.add(Restrictions.ge("issueDate", deadLineIni));
			}
			if (deadLineEnd != null && deadLineIni == null) {
				cr.add(Restrictions.le("issueDate", deadLineEnd));
			}
			if (deadLineIni != null && deadLineEnd != null) {
				cr.add(Restrictions.between("issueDate", deadLineIni,
						deadLineEnd));
			}

			docs = cr.list();
			trans.commit();

		} catch (HibernateException e) {
			System.out.println("Erro ao listar créditos por cidade : "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return docs;
	}

	@Override
	public void updateDocumentStatus(Document document,
			DocumentSituation documentSituation) throws ApplicationException {
		Session session = null;
		Transaction trans = null;

		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			session.update(document);
			if (document.isAllowFractionPrice()) {
				updateFractionStatus(document);
			}
			session.save(documentSituation);// Parei aqui-- Testar se status ja
											// existe antes
			trans.commit();
		} catch (HibernateException e) {
			System.out.println("Erro ao atualizar status do crédito: "
					+ e.getMessage());
			System.out.println(document.toString());
			throw new ApplicationException(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	private void updateFractionStatus(Document mainDocument)
			throws HibernateException {
		String hql = "UPDATE DocumentFraction set status = :status "
				+ "WHERE mainDocument = :mainDocument";
		Session session = null;
		Transaction trans = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();

			Query query = session.createQuery(hql);
			query.setParameter("status", mainDocument.getLastDocumentStatus());
			query.setParameter("mainDocument", mainDocument);
			query.executeUpdate();
			trans.commit();
		} catch (HibernateException e) {
			System.out.println("Erro ao atualizar status das frações: "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	private void updateFractionStatus(List<DocumentFraction> fractions,
			DocumentStatus status) throws HibernateException {

		Session session = null;
		Transaction trans = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			for (DocumentFraction fraction : fractions) {
				fraction.setStatus(status);
				session.update(fraction);
				//Update all bid given to these fractions, in order to can be avaiable again.
				if (status.getXidDocumentStatus() == SystemDomain.documentStatusSold) {
					updateFracsSoldDocumentQuestion(session,fraction);
				}
			}

			trans.commit();
		} catch (HibernateException e) {
			System.out
					.println("Erro ao atualizar status das frações vendidas: "
							+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	private void updateFracsSoldDocumentQuestion(Session session,
			DocumentFraction fraction) throws HibernateException {
		String QueryStr = "update DocumentQuestion set raiseBidRequest = 'S' "
				+ "where xidDocumentQuestion in "
				+ "(select idDocumentQuestion from DocumentFractionQuestion "
				+ "where idDocumentFraction = " + fraction.getXidFractionDocument() + ")";
		Query query;
		
		query = session.createSQLQuery(QueryStr);
		query.executeUpdate();
	}

	private DocumentStatus getStatus(int statusId) {
		DocumentStatus readyToSell = null;
		DocumentStatusDao documentStatusDao = new DocumentStatusDaoImpl();
		readyToSell = documentStatusDao.getDocumentStatusById(statusId);

		return readyToSell;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> searchDocumentAdmin(User user,
			DocumentType[] documentType, DocumentStatus documentStatus) {
		List<Document> docs = null;
		Session session = null;
		Transaction trans = null;
		Criteria cr = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();

			cr = session.createCriteria(Document.class);

			if (user != null) {
				cr.add(Restrictions.eq("user", user));
			}
			if (documentType != null) {
				cr.add(Restrictions.in("documentType", documentType));
			}
			if (documentStatus != null) {
				cr.add(Restrictions.eq("lastDocumentStatus", documentStatus));
			}

			docs = cr.list();
			trans.commit();

		} catch (HibernateException e) {
			System.out.println("Erro ao buscar créditos para o admin :"
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return docs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getUserDocuments(User user,
			DocumentStatus statusDocument) {
		List<Document> docs = null;
		Session session = null;
		Transaction trans = null;
		Criteria cr = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();

			cr = session.createCriteria(Document.class);

			if (user != null) {
				cr.add(Restrictions.eq("user", user));
			}

			if (statusDocument != null) {
				cr.add(Restrictions.eq("lastDocumentStatus", statusDocument));
			}

			docs = cr.list();
			trans.commit();

		} catch (HibernateException e) {
			System.out.println("Erro ao listar créditos do usuário "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return docs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentFraction> getFractionDocs(Document mainDocument) {
		List<DocumentFraction> docs = null;
		Session session = null;
		Transaction trans = null;
		Criteria cr = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();

			cr = session.createCriteria(DocumentFraction.class);
			cr.add(Restrictions.eq("mainDocument", mainDocument));
			docs = cr.list();

			trans.commit();

		} catch (HibernateException e) {
			System.out.println("Erro ao listar créditos do usuário "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return docs;
	}

	@Override
	public DocumentFraction getDocumentFractionById(long id) {
		DocumentFraction doc = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session
					.createQuery("from DocumentFraction where xidFractionDocument = "
							+ id);
			doc = (DocumentFraction) qry.uniqueResult();
			session.flush();
			trans.commit();
			return doc;
		} catch (HibernateException e) {
			System.out.println("Erro ao obter fração do documento por id: "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentFraction> getFractionDocsAvailable(Document mainDocument) {
		List<DocumentFraction> docs = null;
		Session session = null;
		Transaction trans = null;
		Criteria cr = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();

			cr = session.createCriteria(DocumentFraction.class);
			cr.add(Restrictions.eq("mainDocument", mainDocument));

			cr.add(Restrictions
					.disjunction()
					.add(Restrictions.eq("status",
							getStatus(SystemDomain.documentStatusReady)))
					.add(Restrictions
							.eq("status",
									getStatus(SystemDomain.documentStatusInNegotiation))));

			docs = cr.list();

			trans.commit();

		} catch (HibernateException e) {
			System.out.println("Erro ao listar créditos do usuário "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return docs;
	}

	@Override
	public void updateDocumentStatus(Document document,
			DocumentSituation documentSituation,
			List<DocumentFraction> fractions) throws ApplicationException {

		Session session = null;
		Transaction trans = null;

		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();

			if (document != null) {
				session.update(document);
				session.save(documentSituation);
			}

			if (fractions != null) {
				this.updateFractionStatus(fractions,
						documentSituation.getDocumentStatus());
			}

			trans.commit();
		} catch (HibernateException e) {
			System.out.println("Erro ao atualizar frações vendidas: "
					+ e.getMessage());			
			throw new ApplicationException(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}
}
