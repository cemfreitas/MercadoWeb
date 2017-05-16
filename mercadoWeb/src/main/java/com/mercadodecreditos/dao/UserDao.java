package com.mercadodecreditos.dao;

import com.mercadodecreditos.model.User;
import com.mercadodecreditos.model.UserSituation;

public interface UserDao {
	public void save(User user);
	public void update(User user);
	public void changeUserSituation(User user,UserSituation situation);
	public User getUserById(long id);
	public User getUserByLogin(String login);
	public User getUserByCpf(String cpf);
	public User getUserByCnpj(String cnpj);
	public User getUserByEmail(String email);
	public boolean isAdminUser(User user);	

}
