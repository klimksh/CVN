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
    static String id = session.get("id");

    public static void index() {

        renderTemplate("Application/frontpage.html");
    }

    // creating a session
    public static void connect(User user) {
        session.put("logged", user.id);
    }

    public static void login(String email, String id, String displayName) {
        System.out.println(email + "  " + id + " " + displayName);
        User a;
        if (User.findByEmail(email) == null)
            a = new User(email, "randomPassword", displayName, id);
        else
            a = User.findByEmail(email);
        session.put("id", a.id);
        session.put("email", a.email);
        id = session.get("id");

        System.out.println("myy:" + session.get("logid"));
    }

    public static void loginToken(String token) {
        session.put("token", token);
        System.out.println("i have this token" + session.get("token"));
    }

    // get the user which is logged ON
    static User connectedUser() {
        String userId = session.get("logged");
        return userId == null ? null : (User) User.findById(Long.parseLong(userId));
    }


    //logout
    public static void logout() {

        flash.success("You've been logged out");
        session.remove("id");
        session.remove("email");
        id = null;


    }

    public static void disconnectAccount() {

        User usr = User.findByEmail(session.get("email"));
         /*
    	  * should we delete account from the system or not
    	  */
        // JPA.em().remove(usr);

    }


    public static void frontpage() {
        //  String kk=session.get("id");
        int isLoged = 3;
        if (session.get("id") != null)
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
                // tell him that this ID already exists
            } else {
                // tell to user that his email or paswword does not match
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