package controllers;

import models.User;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.Required;
import play.mvc.Controller;

public class Application extends Controller {

	public static  boolean check()
	{
		return true;
	}
	// creating a session
	public static void connect(User user) {
		session.put("logged", user.googleUserId);
	}

	public static void login(String email, String id, String displayName) {
		User user = User.findByEmail(email);
		if (user == null){
			user = new User(email,displayName, id);	
			user.save();
		}
		session.put("googleUserId", user.googleUserId);
		session.put("userEmail", user.email);
		session.put("userName", user.name);
	}

	public static void loginToken(String token) {
		session.put("token", token);
	}

	// get the user which is logged in
	static User connectedUser() {
		String userId = session.get("logged");
		return userId == null ? null : (User) User.findById(Long.parseLong(userId));
	}

	
	public static void logout() {
		flash.success("You've been logged out");
		session.remove("googleUserId");
		session.remove("userEmail");
	   	session.remove("userName");
	}

	public static int exist() {
		if (session.get("googleUserId") == null)
			return 0;
		else
			return 1;
	}

	public static void disconnectAccount() {
		User usr = User.findByEmail(session.get("userEmail"));
	}

	public static void frontpage() {
		int isLoged = 3;
		if (session.get("googleUserId") != null)
			isLoged = 6;
		render(isLoged);
	}

	
	
	
	public static void timeline() {
		render();
	}
}