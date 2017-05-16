package com.mercadodecreditos.dao;

import java.util.List;
import com.mercadodecreditos.model.User;
import com.mercadodecreditos.model.UserSituation;

public interface UserSituationDao {
	public void save(UserSituation userSituation);
	public List<UserSituation> getAllUserSituationByUser(User user);
	public UserSituation getLastUserSituationByUser(User user);

}
