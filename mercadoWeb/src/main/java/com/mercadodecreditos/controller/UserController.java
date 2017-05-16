package com.mercadodecreditos.controller;

import java.util.Date;

import org.hibernate.HibernateException;

import com.mercadodecreditos.dao.UserDao;
import com.mercadodecreditos.dao.UserDaoImpl;
import com.mercadodecreditos.model.Situation;
import com.mercadodecreditos.model.User;
import com.mercadodecreditos.model.UserSituation;
import com.mercadodecreditos.util.ApplicationException;

public class UserController {

	public void saveUser(User user) throws ApplicationException {
		try {
			new UserDaoImpl().save(user);
		} catch (HibernateException e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	public User loadUser(String login) {
		User user = new UserDaoImpl().getUserByLogin(login);
		return user;
	}

	public void updateUser(User user) throws ApplicationException{
		try {
			new UserDaoImpl().update(user);
		} catch (HibernateException e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	public void changeUserStatus(User user, int idSituation, String explanation) {
		Situation changedSituation = getSituation(idSituation);
		UserSituation userSituation = generateUserSituation(user,
				changedSituation, explanation);
		user.setLastSituation(changedSituation);
		UserDao userDao = new UserDaoImpl();
		userDao.changeUserSituation(user, userSituation);

	}

	private UserSituation generateUserSituation(User user, Situation situation,
			String explanation) {
		UserSituation userSituation = new UserSituation();
		userSituation.setDescription(explanation);
		userSituation.setSituation(situation);
		userSituation.setUser(user);
		userSituation.setUserSituationDate(new Date());

		return userSituation;
	}

	private Situation getSituation(int idSItuation) {
		SituationController situationController = new SituationController();
		return situationController.getSituationById(idSItuation);
	}

	public boolean isAdminUser(User user, int idSituation) {
		return new UserDaoImpl().isAdminUser(user);
	}

	public User searchUserAdmin(String login, String email, String cpf,
			String cnpj) {
		User user = null;
		UserDao userDao = new UserDaoImpl();

		if (!login.equals("")) {
			user = userDao.getUserByLogin(login);
		} else if (!email.equals("")) {
			user = userDao.getUserByEmail(email);
		} else if (!cpf.equals("")) {
			user = userDao.getUserByCpf(cpf);
		} else if (!cnpj.equals("")) {
			user = userDao.getUserByCnpj(cnpj);
		}

		return user;
	}

}
