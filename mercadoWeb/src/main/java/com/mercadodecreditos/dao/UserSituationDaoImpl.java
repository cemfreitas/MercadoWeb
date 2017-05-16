package com.mercadodecreditos.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mercadodecreditos.model.User;
import com.mercadodecreditos.model.UserSituation;
import com.mercadodecreditos.util.DataSource;

public class UserSituationDaoImpl implements UserSituationDao{

	@Override
	public void save(UserSituation userSituation) {
		Session session= null;
		Transaction trans = null;
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			session.save(userSituation);
			trans.commit();
		} catch (HibernateException e) {
			System.out.println("Erro ao salvar situação do usuário: " + e.getMessage());			
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public List<UserSituation> getAllUserSituationByUser(User user) {
		
		return null;
	}

	@Override
	public UserSituation getLastUserSituationByUser(User user) {
		
		return null;
	}

}
