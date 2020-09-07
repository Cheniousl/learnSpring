package com.itranswarp.learnjava.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserService {

	private MailService mailService;
	private DataSource dataSource;

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private List<User> users = new ArrayList<>(List.of( // users:
			new User(1, "bob@example.com", "password", "Bob"), // bob
			new User(2, "alice@example.com", "password", "Alice"), // alice
			new User(3, "tom@example.com", "password", "Tom"))); // tom

	public User login(String email, String password) {

		try {
			Connection con = dataSource.getConnection();
			PreparedStatement ps = con.prepareStatement("select * from user");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Long dbID = rs.getLong("id");
				String dbEmail = rs.getString("email");
				String dbPassword = rs.getString("password");
				String dbName = rs.getString("name");
				if (dbEmail.equalsIgnoreCase(email) && dbPassword.equals(password)) {
					User user = new User(dbID, dbEmail, dbPassword, dbName);
					mailService.sendLoginMail(user);
					return user;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		throw new RuntimeException("login failed.");
		/*for (User user : users) {
			if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
				mailService.sendLoginMail(user);
				return user;
			}
		}
		throw new RuntimeException("login failed.");*/
	}

	public User getUser(long id) {
		return this.users.stream().filter(user -> user.getId() == id).findFirst().orElseThrow();
	}

	public User register(String email, String password, String name) {
		User user=null;
		try{
			Connection con=dataSource.getConnection();
//			PreparedStatement ps=con.prepareStatement("insert into user(id,email,password,name) values (?,?,?,?)");
			PreparedStatement ifUserExist=con.prepareStatement("select email from user");
			ResultSet rs=ifUserExist.executeQuery();
			while (rs.next()){
				if(email.equalsIgnoreCase(rs.getString("email"))){
					throw new RuntimeException("email exist.");
				}
			}

			user = new User(users.stream().mapToLong(u -> u.getId()).max().getAsLong()+1, email, password, name);
			PreparedStatement ps=con.prepareStatement("insert into user(id,email,password,name) values (?,?,?,?)");
			ps.setLong(1,user.getId());
			ps.setString(2,user.getEmail());
			ps.setString(3,user.getPassword());
			ps.setString(4,user.getName());
			int result=ps.executeUpdate();
			mailService.sendRegistrationMail(user);

		}catch (Exception e){
			e.printStackTrace();
		}
		return user;



		/*users.forEach((user) -> {
			if (user.getEmail().equalsIgnoreCase(email)) {
				throw new RuntimeException("email exist.");
			}
		});
		User user = new User(users.stream().mapToLong(u -> u.getId()).max().getAsLong(), email, password, name);
		users.add(user);
		mailService.sendRegistrationMail(user);
		return user;*/
	}
}
