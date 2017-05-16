package com.mercadodecreditos.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.model.DocumentFraction;
import com.mercadodecreditos.model.DocumentFractionQuestion;
import com.mercadodecreditos.model.DocumentQuestion;
import com.mercadodecreditos.model.DocumentStatus;
import com.mercadodecreditos.model.Message;
import com.mercadodecreditos.model.User;
import com.mercadodecreditos.util.DataSource;
import com.mercadodecreditos.util.SystemDomain;
import com.mercadodecreditos.web.MessageBean;

public class DocumentQuestionDaoImpl implements DocumentQuestionDao {

	@Override
	public void save(DocumentQuestion docQuestion) {
		Session session = null;
		Transaction trans = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			session.save(docQuestion);
			trans.commit();
		} catch (HibernateException e) {
			System.out.println("Erro ao cadastrar lance do usuário: "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public void save(DocumentQuestion docQuestion,
			DocumentFractionQuestion[] fractions) {
		Session session = null;
		Transaction trans = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			session.save(docQuestion);
			for (DocumentFractionQuestion fraction : fractions) {
				session.save(fraction);
			}
			trans.commit();
		} catch (HibernateException e) {
			System.out.println("Erro ao cadastrar lance do usuário: "
					+ e.getMessage());
			trans.rollback();
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	@Override
	public void update(DocumentQuestion docQuestion) {
		Session session = null;
		Transaction trans = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			session.update(docQuestion);
			trans.commit();
		} catch (HibernateException e) {
			System.out.println("Erro ao cadastrar lance do usuário: "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	@Override
	public DocumentQuestion getDocumentQuestionById(long id) {
		DocumentQuestion docQuestion = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session
					.createQuery("from DocumentQuestion where xidDocumentQuestion = "
							+ id);
			docQuestion = (DocumentQuestion) qry.uniqueResult();
			session.flush();
			trans.commit();
			return docQuestion;
		} catch (HibernateException e) {
			System.out.println("Erro ao obter lance por id: " + e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentQuestion> getDocumentQuestionByUserBid(User user) {
		String qryStr = "from DocumentQuestion as docQ where docQ.userBid = :user";
		List<DocumentQuestion> docsQ = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery(qryStr);
			qry.setParameter("user", user);
			docsQ = qry.list();
			session.flush();
			trans.commit();
			return docsQ;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar lances por usuário : "
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
	public List<DocumentQuestion> getDocumentQuestionByUserDoc(Document doc) {
		String qryStr = "from DocumentQuestion as docQ where docQ.document = :doc";
		List<DocumentQuestion> docsQ = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery(qryStr);
			qry.setParameter("doc", doc);
			docsQ = qry.list();
			session.flush();
			trans.commit();
			return docsQ;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar lances do crédito : "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public DocumentQuestion getLastDocumentQuestion(Document doc, User user) {
		Session session = null;
		Transaction trans = null;
		Query qry, qry2 = null;
		DocumentQuestion documentQuestion = null;
		List<DocumentFractionQuestion> docFracList = null;

		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session
					.createQuery("from DocumentQuestion where idDocument = "
							+ doc.getXidDocument()
							+ " and idUserBid = "
							+ user.getXidUser()
							+ " and document.lastDocumentStatus.xidDocumentStatus in (2,3)"
							+ " order by documentQuestionDate desc");

			qry.setFirstResult(0);
			qry.setMaxResults(1);
			documentQuestion = (DocumentQuestion) qry.uniqueResult();
			session.flush();

			if (documentQuestion == null) {
				return null;
			} else {
				if (doc.isAllowFractionPrice()) {
					qry2 = session
							.createQuery("from DocumentFractionQuestion where documentQuestion.xidDocumentQuestion = "
									+ documentQuestion.getXidDocumentQuestion()
									+ " and documentFraction.status.xidDocumentStatus in (2,3)");
					docFracList = qry2.list();
					session.flush();
					trans.commit();
					if (docFracList != null && docFracList.size() > 0) {
						return documentQuestion;
					} else {
						return null;
					}
				}
				return documentQuestion;
			}

		} catch (HibernateException e) {
			System.out.println("Erro ao obter lance por id e usuário: "
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
	public List<User> getUsers(User user, String msgType, Document document,
			DocumentStatus status) {
		String qryStrReceived = "select dq.userBid from DocumentQuestion dq "
				+ "join dq.document as doc where doc.user = :user ";
		String qryStrSent = "select d.user from Document d "
				+ "join d.documentQuestion as dq where dq.userBid = :user ";
		if (document != null) {
			qryStrReceived += "and dq.document = :doc ";
			qryStrSent += "and dq.document = :doc ";
		}
		if (status != null) {
			qryStrReceived += "and doc.lastDocumentStatus = :status ";
			qryStrSent += "and d.lastDocumentStatus = :status ";
		}
		qryStrReceived += "group by dq.userBid";
		qryStrSent += "group by d.user";

		List<User> users = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			if (msgType.equals(MessageBean.MESSAGE_TYPE_RECEIVED)) {
				qry = session.createQuery(qryStrReceived);
				if (document != null) {
					qry.setParameter("doc", document);
				}
				if (status != null) {
					qry.setParameter("status", status);
				}
			} else {
				qry = session.createQuery(qryStrSent);
				if (document != null) {
					qry.setParameter("doc", document);
				}
				if (status != null) {
					qry.setParameter("status", status);
				}
			}
			qry.setParameter("user", user);
			users = qry.list();
			session.flush();
			trans.commit();
			return users;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar usuários das mensagens: "
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
	public List<DocumentQuestion> searchMessages(User loggedUser,
			String msgType, List<Document> documents, User userMsg,
			Document document) {
		List<DocumentQuestion> msgs = null;
		Session session = null;
		Transaction trans = null;
		Criteria cr = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			cr = session.createCriteria(DocumentQuestion.class);
			if (msgType.equals(MessageBean.MESSAGE_TYPE_SENT)) {
				if (loggedUser != null) {
					cr.add(Restrictions.eq("userBid", loggedUser));
				}
				if (document != null) {
					cr.add(Restrictions.eq("document", document));
				} else if (userMsg != null) {
					cr.add(Restrictions.in("document",
							this.getDocumentsByUSer(userMsg)));
				} else if (documents != null && documents.size() > 0) {
					cr.add(Restrictions.in("document", documents));
				}
			} else {
				if (userMsg != null) {
					cr.add(Restrictions.eq("userBid", userMsg));
				}
				if (document != null) {
					cr.add(Restrictions.eq("document", document));
				} else if (documents == null || documents.size() == 0) {
					return null;
				} else if (documents.size() > 0) {
					cr.add(Restrictions.in("document", documents));
				}
			}
			cr.addOrder(Order.desc("documentQuestionDate"));
			msgs = cr.list();
			trans.commit();
		} catch (HibernateException e) {
			System.out.println("Erro ao buscar créditos : " + e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return msgs;
	}

	private List<Document> getDocumentsByUSer(User user) {
		List<Document> docs = null;
		DocumentDaoImpl documentDaoImpl = new DocumentDaoImpl();
		docs = documentDaoImpl.getDocumentByUser(user);
		return docs;
	}

	@SuppressWarnings("unchecked")
	// @Override
	public List<Object> searchMessages2(User loggedUser, String msgType,
			String msgStatus, User userMsg) {
		String qryMsgReceived = "select dq.userBid,dq.bid,dq.documentQuestionDate,doc.lastDocumentStatus,"
				+ "doc.documentNumber,doc.price from DocumentQuestion dq "
				+ "join dq.document as doc where doc.user = :user ";
		String qryMsgSent = "select doc.user,dq.bid,dq.documentQuestionDate,doc.lastDocumentStatus,"
				+ "doc.documentNumber,doc.price from Document doc "
				+ "join doc.documentQuestion as dq where dq.userBid = :user ";
		String qryMsgReceivedByUser = "select dq.userBid,dq.bid,dq.documentQuestionDate,doc.lastDocumentStatus,"
				+ "doc.documentNumber,doc.price from DocumentQuestion dq "
				+ "join dq.document as doc where doc.user = :user and dq.userBid = :user2";
		String qryMsgSentByUSer = "select doc.user,dq.bid,dq.documentQuestionDate,doc.lastDocumentStatus,"
				+ "doc.documentNumber,doc.price from Document doc "
				+ "join doc.documentQuestion as dq where dq.userBid = :user and doc.user = :user2";

		List<Object> messages = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;

		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			if (msgType.equals(MessageBean.MESSAGE_TYPE_RECEIVED)
					&& userMsg == null) {
				qry = session.createQuery(qryMsgReceived);
			} else if (msgType.equals(MessageBean.MESSAGE_TYPE_RECEIVED)
					&& userMsg != null) {
				qry = session.createQuery(qryMsgReceivedByUser);
			} else if (msgType.equals(MessageBean.MESSAGE_TYPE_SENT)
					&& userMsg == null) {
				qry = session.createQuery(qryMsgSent);
			} else if (msgType.equals(MessageBean.MESSAGE_TYPE_SENT)
					&& userMsg != null) {
				qry = session.createQuery(qryMsgSentByUSer);
			}
			qry.setParameter("user", loggedUser);
			if (userMsg != null) {
				qry.setParameter("user", loggedUser);
				qry.setParameter("user2", userMsg);
			} else {
				qry.setParameter("user", loggedUser);
			}
			messages = qry.list();
			session.flush();
			trans.commit();

			return messages;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar usuários das mensagens"
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
	public List<Document> getDocuments(User loggedUser, String msgType,
			DocumentStatus status) {
		String qryStrReceived = "select dq.document from DocumentQuestion dq "
				+ "join dq.document as doc where doc.user = :user "
				+ "group by dq.document";
		String qryStrSent = "select d from Document d "
				+ "join d.documentQuestion as dq where dq.userBid = :user "
				+ "group by d.xidDocument";
		String qryStrReceivedByStatus = "select dq.document from DocumentQuestion dq "
				+ "join dq.document as doc where doc.user = :user and doc.lastDocumentStatus = :status "
				+ "group by dq.document";
		String qryStrSentbyStatus = "select d from Document d "
				+ "join d.documentQuestion as dq where dq.userBid = :user and d.lastDocumentStatus = :status "
				+ "group by d.xidDocument";

		List<Document> docs = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			if (msgType.equals(MessageBean.MESSAGE_TYPE_RECEIVED)) {
				if (status != null) {
					qry = session.createQuery(qryStrReceivedByStatus);
					qry.setParameter("status", status);
				} else {
					qry = session.createQuery(qryStrReceived);
				}
			} else {
				if (status != null) {
					qry = session.createQuery(qryStrSentbyStatus);
					qry.setParameter("status", status);
				} else {
					qry = session.createQuery(qryStrSent);
				}
			}
			qry.setParameter("user", loggedUser);
			docs = qry.list();
			session.flush();
			trans.commit();
			return docs;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar créditos das mensagens"
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
	public List<DocumentFraction> getDocumentFraction(
			DocumentQuestion documentQuestion) {
		String qryStr = "select dfq.documentFraction from DocumentFractionQuestion as dfq where dfq.documentQuestion = :docQ";
		List<DocumentFraction> fractions = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery(qryStr);
			qry.setParameter("docQ", documentQuestion);
			fractions = qry.list();
			session.flush();
			trans.commit();
			return fractions;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar frações do lance : "
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
	public List<DocumentFraction> getSelectedDocumentFraction(
			DocumentQuestion documentQuestion) {
		String qryStr = "select dfq.documentFraction from DocumentFractionQuestion as dfq where dfq.documentQuestion = :docQ and dfq.documentFraction.status.xidDocumentStatus in (2,3)";
		List<DocumentFraction> fractions = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery(qryStr);
			qry.setParameter("docQ", documentQuestion);
			fractions = qry.list();
			session.flush();
			trans.commit();
			return fractions;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar frações do lance : "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public boolean isAnyFractionSold(DocumentQuestion documentQuestion) {
		List<DocumentFraction> docsFraction;
		docsFraction = this.getDocumentFraction(documentQuestion);

		for (DocumentFraction fraction : docsFraction) {
			if (fraction.getStatus().getXidDocumentStatus() == SystemDomain.documentStatusSold) {
				return true;
			}
		}
		return false;
	}

	@Override
	public DocumentQuestion getLastDocumentQuestionByUserBid(User userBid,
			Document document) {
		DocumentQuestion docQuestion = null;
		Long id;

		id = this.getLastIDDocumentQuestionByUserBid(userBid, document);
		if (id == -1) {
			return null;
		}

		docQuestion = this.getDocumentQuestionById(id);

		return docQuestion;
	}

	private long getLastIDDocumentQuestionByUserBid(User userBid,
			Document document) {
		Long id;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session
					.createQuery("select max(xidDocumentQuestion) from DocumentQuestion where userBid = :user and document = :doc and document.lastDocumentStatus.xidDocumentStatus in (2,3)");
			qry.setParameter("user", userBid);
			qry.setParameter("doc", document);
			id = (Long) qry.uniqueResult();
			if (id == null) {
				return -1;
			}
			session.flush();
			trans.commit();

		} catch (HibernateException e) {
			System.out
					.println("Erro ao ultimo xidDocumentQuestion por usuario "
							+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getMessagesReceived(Document document) {
		String qryStr = "select DocQ.document,DocQ.userBid from DocumentQuestion DocQ where DocQ.document = :doc group by DocQ.document,DocQ.userBid";
		List<Object> docsQ = null;
		List<Message> messages = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery(qryStr);
			qry.setParameter("doc", document);
			docsQ = qry.list();
			session.flush();
			trans.commit();
			if (docsQ != null && docsQ.size() > 0) {
				messages = new ArrayList<Message>();
				for (int i = 0; i < docsQ.size(); i++) {
					Object[] obj = (Object[]) docsQ.get(i);
					Document doc = (Document) obj[0];
					User user = (User) obj[1];
					Message message = new Message();
					DocumentQuestion docQ;
					message.setFakeUser("Usuário -"
							+ replicate("0", (5 - String.valueOf(i + 1)
									.length())) + String.valueOf(i + 1));
					message.setDocument(doc);
					message.setUserBid(user);
					docQ = getLastDocumentQuestion(doc, user);					
					if (docQ == null) {						
						message.setRaiseBidRequest("S");
					} else {						
						message.setRaiseBidRequest(docQ.getRaiseBidRequest());
					}					
					messages.add(message);
				}				
			}
			return messages;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar lances recebidos : "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	private String replicate(String strRepl, long qtd) {
		StringBuffer str = new StringBuffer();

		if (qtd < 0) {
			qtd = qtd * -1;
		}

		for (int i = 0; i < qtd; i++) {

			str.append(strRepl);
		}

		return str.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BigDecimal> getBidsReceived(Document document, User user) {
		String qryStr = "select DocQ.bid from DocumentQuestion DocQ where DocQ.document = :doc and DocQ.userBid = :user";
		List<BigDecimal> bids = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery(qryStr);
			qry.setParameter("doc", document);
			qry.setParameter("user", user);
			bids = qry.list();
			session.flush();
			trans.commit();

			return bids;
		} catch (HibernateException e) {
			System.out.println("Erro ao listar lances recebidos : "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}
}
