package controllers;

import models.User;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.Required;
import play.mvc.Controller;

//import controllers.securesocial.SecureSocial;
//import com.*;
//import com.google.gdata.data.youtube.VideoEntry;

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
			user = new User(email, "randomPassword", displayName, id);		
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

	public static void add_video() {
		int tt = 10;

		render(tt);
	}

	// checking if email and password are OK
	public static void authenticate(String email, String password) {
		User user = User.findByEmail(email);
		if (user == null || !user.checkPassword(password)) {
			flash.error("Bad email or bad password");
			flash.put("email", email);
			// redirect somewhere
		}
		connect(user);
		flash.success("Welcome %s !", user.name);
		// redirect somewhere
	}

	// register ( we have two password fields password and verify password
	public static void register(@Required String name,
			@Required @Email String email, @Required String password,
			@Equals("password") String password2) {
		if (validation.hasErrors() || User.findByEmail(email) != null) {
			validation.keep();
			params.flash();
			String error = "";
			if (User.findByEmail(email) != null) {
				flash.error("This email allready exist in our database");
			} else {
				flash.error("passwords does not match");
			}
		} else {
			User user = new User(email, password2, name);
			user.save();
			// redirect somewhere

		}

	}

	public static void timeline() {
		render();
	}
}