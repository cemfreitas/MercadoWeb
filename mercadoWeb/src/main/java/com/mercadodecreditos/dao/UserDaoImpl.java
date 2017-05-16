package com.mercadodecreditos.dao;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.mercadodecreditos.model.Situation;
import com.mercadodecreditos.model.User;
import com.mercadodecreditos.model.UserSituation;
import com.mercadodecreditos.util.DataSource;
import com.mercadodecreditos.util.SystemDomain;

public class UserDaoImpl implements UserDao {

	@Override
	public void save(User user) {
		Session session = null;
		Transaction trans = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			user.setLastSituation(getPendingSituation());
			session.save(user);
			session.save(generateFirstSituation(user));
			trans.commit();
		} catch (ConstraintViolationException e) {
			String constraint = "";
			if (e.getCause().getLocalizedMessage().contains("login")) {
				constraint = "Login";
			} else if (e.getCause().getLocalizedMessage().contains("CPF")) {
				constraint = "CPF";
			} else if (e.getCause().getLocalizedMessage().contains("CNPJ")) {
				constraint = "CNPJ";
			} else if (e.getCause().getLocalizedMessage().contains("email")) {
				constraint = "Email";
			}
			throw new HibernateException(constraint + " já cadastrado");

		} catch (HibernateException e) {
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public void update(User user) {
		Session session = null;
		Transaction trans = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			session.update(user);
			trans.commit();
		} catch (ConstraintViolationException e) {
			String constraint = "";
			if (e.getCause().getLocalizedMessage().contains("login")) {
				constraint = "Login";
			} else if (e.getCause().getLocalizedMessage().contains("CPF")) {
				constraint = "CPF";
			} else if (e.getCause().getLocalizedMessage().contains("CNPJ")) {
				constraint = "CNPJ";
			} else if (e.getCause().getLocalizedMessage().contains("email")) {
				constraint = "Email";
			}
			throw new HibernateException(constraint + " já cadastrado");

		} catch (HibernateException e) {
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	private UserSituation generateFirstSituation(User user) {
		UserSituation firstSituation = new UserSituation();
		firstSituation.setDescription("Email enviado para confirmação");
		firstSituation.setSituation(getPendingSituation());
		firstSituation.setUser(user);
		firstSituation.setUserSituationDate(new Date());

		return firstSituation;
	}

	private Situation getPendingSituation() {
		Situation pendingSituation = null;
		SituationDao situationDao = new SituationDaoImpl();
		pendingSituation = situationDao
				.getSituationById(SystemDomain.userSituationReady);

		/*
		 * pendingSituation = situationDao
		 * .getSituationById(SystemDomain.userSituationPending);
		 */
		// TODO Descomentar quando implementar envio de email para confirmação

		return pendingSituation;
	}

	@Override
	public User getUserById(long id) {
		User u = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery("from User where xidUser = " + id);
			u = (User) qry.uniqueResult();
			session.flush();
			trans.commit();
			return u;
		} catch (HibernateException e) {
			System.out.println("Erro ao obter usuário por id: "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public User getUserByLogin(String login) {
		String qryStr = "from User as u where u.login = :login";
		User u = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery(qryStr);
			qry.setParameter("login", login);
			u = (User) qry.uniqueResult();
			session.flush();
			trans.commit();
			return u;
		} catch (HibernateException e) {
			System.out.println("Erro ao obter usuário por login: "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public User getUserByCpf(String cpf) {
		User u = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery("from User where cpf = :cpf");
			qry.setParameter("cpf", cpf);
			u = (User) qry.uniqueResult();
			session.flush();
			trans.commit();
			return u;
		} catch (HibernateException e) {
			System.out.println("Erro ao obter usuário por cpf: "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public User getUserByCnpj(String cnpj) {
		User u = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery("from User where cnpj = :cnpj");
			qry.setParameter("cnpj", cnpj);
			u = (User) qry.uniqueResult();
			session.flush();
			trans.commit();
			return u;
		} catch (HibernateException e) {
			System.out.println("Erro ao obter usuário por cnpj: "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public User getUserByEmail(String email) {
		User u = null;
		Session session = null;
		Transaction trans = null;
		Query qry = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			qry = session.createQuery("from User where email = :email");
			qry.setParameter("email", email);
			u = (User) qry.uniqueResult();
			session.flush();
			trans.commit();
			return u;
		} catch (HibernateException e) {
			System.out.println("Erro ao obter usuário por email: "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public boolean isAdminUser(User user) {
		User u = null;
		Session session = null;
		Transaction trans = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			u = (User) session.load(User.class, user.getXidUser());
			session.flush();
			trans.commit();
			if (u.isAdmin()) {
				return true;
			} else {
				return false;
			}

		} catch (HibernateException e) {
			System.out.println("Erro ao testar usuário admin: "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public void changeUserSituation(User user, UserSituation situation) {
		Session session = null;
		Transaction trans = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			session.update(user);
			session.save(situation);
			trans.commit();
		} catch (HibernateException e) {
			System.out.println("Erro ao mudar status do usuário: "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

}
