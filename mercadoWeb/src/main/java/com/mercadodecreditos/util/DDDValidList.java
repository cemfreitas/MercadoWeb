package com.mercadodecreditos.util;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mercadodecreditos.model.DDDList;

public class DDDValidList {

	@SuppressWarnings("unchecked")
	public static List<String> getValidDDDs() {
		List<String> dddsStr = new ArrayList<String>();
		List<DDDList> ddds = null;
		Session session = null;
		Transaction trans = null;
		
		try {
			session = DataSource.getSessionFactory().openSession();
			trans = session.beginTransaction();
			ddds = session.createCriteria(DDDList.class).list();
			for (DDDList ddd : ddds) {
				dddsStr.add(ddd.getDdd());
			}
			session.flush();
			trans.commit();
			
		} catch (HibernateException e) {
			System.out.println("Erro ao listar créditos por usuário : "
					+ e.getMessage());
			throw new HibernateException(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		
		
		return dddsStr;
	}
	
}
