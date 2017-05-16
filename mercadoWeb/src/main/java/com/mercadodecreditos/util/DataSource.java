package com.mercadodecreditos.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class DataSource {
	private static final SessionFactory sessionFactory = configureSessionFactory();
	private static ServiceRegistry serviceRegistry;

	private static SessionFactory configureSessionFactory() {

		Configuration configuration = new Configuration();
		configuration.configure();
		// configuration.configure("hibernate.cfg.xml");

		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
				configuration.getProperties()).build();

		return configuration.buildSessionFactory(serviceRegistry);
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
