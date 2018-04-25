package org.patterns.mechanical;

import org.patterns.mechanical.dao.UserDaoImpl;
import org.patterns.mechanical.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class MechanicalApplication {
	private UserDaoImpl userDao;

	public MechanicalApplication(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	@PostConstruct
	public void seed() {
		User client = new User();
		client.setLogin("client");
		client.setPassword("client");
		client.setRole("CLIENT");
		userDao.save(client);

		User callCenter = new User();
		callCenter.setLogin("callcenter");
		callCenter.setPassword("callcenter");
		callCenter.setRole("CALL_CENTER");
		userDao.save(callCenter);

		User support = new User();
		support.setLogin("support");
		support.setPassword("support");
		support.setRole("SUPPORT");
		userDao.save(support);
	}

	public static void main(String[] args) {
		SpringApplication.run(MechanicalApplication.class, args);
	}
}
