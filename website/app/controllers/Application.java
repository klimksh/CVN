package controllers;

import models.User;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.Required;
import play.mvc.Controller;

public class Application extends Controller {
  String id;
    public static void index() {
         if(session.get("id")!=null)
    	 session.put("id","");
        renderTemplate("Application/frontpage.html");
    }

    // creating a session
    public static void connect(User user) {
        session.put("logged", user.id);
    }

    public static void login(String email, String id, String displayName) {
            User a;
        if (User.findByEmail(email) == null)
            a = new User(email, "randomPassword", displayName, id);
        else
            a = User.findByEmail(email);
        session.put("id", a.id);
        session.put("email", a.email);
    }

    public static void loginToken(String token) {
        session.put("token", token);
        System.out.println("i have this token" + session.get("token")+"    and id:"+session.get("id"));
    }
    //logout
    public static void logout() {

        flash.success("You've been logged out");
        session.put("id",0);
        session.put("email","");
    }
    public static int exist()
    {
    	if(session.get("id")==null)
    		return 0;
    	else
    		return 1;
    }

    public static void disconnectAccount() {
        User usr = User.findByEmail(session.get("email"));
    }

    public static void frontpage() {
        int isLoged = 3;
        if (session.get("id") != null)
            isLoged = 6;
        render(isLoged);
    }

    public static void add_video() {
        int tt = 10;        
        render(tt);
    }   

    public static void timeline() {
        render();
    }
    //-------------------------------------------------------------------------------
    // maybe will be needed if we support 
    // checking if email and password are OK
    public static void authenticate(String email, String password) {
        User user = User.findByEmail(email);
        if (user == null || !user.checkPassword(password)) {
            System.out.println("not successfully");
            flash.error("Bad email or bad password");
            flash.put("email", email);
            // redirect somewhere
        }
        connect(user);
        flash.success("Welcome %s !", user.name);
        //redirect somewhere
    }
    //register  ( we have two password fields  password and verify password
    public static void register(@Required String name, @Required @Email String email, @Required String password, @Equals("password") String password2) {
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
}