package mercadoWeb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.mercadodecreditos.model.User;
import com.mercadodecreditos.util.DataSource;
import com.mercadodecreditos.web.UserBean;


public class TesteState {
	
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		DataSource.getSessionFactory().openSession();
		UserBean userBean = new UserBean();
		
		userBean.newUser();
		
		User user = new User();
		user.setName("Carlos Eduardo");
		user.setLogin("cfreitas");
		user.setPassword("1234");
		userBean.setPasswordConfirmed("1234");
		user.setAddress("Geminiano Goes 1300 apto 1402");
		user.setNeighborhood("Freguesia");
		userBean.setIdCity(3658);
		userBean.setIdState(19);
		user.setZipCode("22743-670");
		date = fmt.parse("18/12/1973");
		user.setBirthDate(date);
		user.setResPhone("21-2446-2910");
		user.setCommPhone("21-3035-7375");
		user.setCelPhone("21-98272-4829");
		user.setCPF("02919890719");
		user.setEmail("cemfreitas@outlook.com");
		userBean.setIdUserType(1);
		
		userBean.setUser(user);
		userBean.save("insert");
		System.out.println("Usuário inserido");
	}
}
