package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Date;
import java.util.List;

import views.html.*;

public class Application extends Controller {

    public static Result toIndexPage() {
        return ok(index.render("404 Happy Error"));
    }

    public static Result toErrorPage() {
        return ok(error.render(""));
    }

    public static Result toLoginPage() {
        List<User> userList = User.getAllUsers();
        int count = 0;
        for (int i = 0 ; i < userList.size() ; i++) {
            if (userList.get(i).getType() == User.ADMIN) {
                count++;
            }
        }
        if (count == 0) {
            User.create("admin99", "admin", "Auto", "Created", 99);
            //Division1
            //FATCAT
            User.create("inont", "inont", "Natcha", "Charoen", 1);
            User.create("yanagi", "yanagi", "Chonni", "Kitti", 1);
            User.create("maxmi", "maxmi", "Kitti", "Promdi", 1);
            User.create("nichy", "nichy", "Nichy", "Han", 1);
            User.create("gurokung", "gurokung", "Jirat", "Inta", 1);
            Team.create("Fatcat","2,3,4,5,6");
            Project.create("Fatcat", "", 1);
            //Saint4
            User.create("munin","munin","Muninthorn","T",1);
            User.create("runya","runya","Runyasak","C",1);
            User.create("naras","naras","Nara","S",1);
            User.create("vasup","vasup","Vasupol","C",1);
            User.create("wutti","wutti","Wuttipong","K",1);
            Team.create("Saint4","7,8,9,10,11");
            Project.create("Saint4", "", 2);
            //Manat
            User.create("chinna","chinna","Chinnaporn","S",1);
            User.create("sorra","sorra","Sorrawit","C",1);
            User.create("worap","worap","Worapon","O",1);
            User.create("nitik","nitik","Niti","P",1);
            User.create("supas","supas","Supasn","K",1);
            Team.create("Manat","12,13,14,15,16");
            Project.create("Manat", "", 3);
            //2Big2Slim
            User.create("punpi","punpi","Punpikorn","R",1);
            User.create("nathak","natha","Nathakorn","S",1);
            User.create("piyap","piyap","Piyapat","T",1);
            User.create("nabhat","nabhat","Nabhat","Y",1);
            User.create("nutka","nutka","Nut","K",1);
            Team.create("2Big2Slim","17,18,19,20,21");
            Project.create("2Big2Slim", "", 4);

            //Division2
            //GG
            User.create("b5610545749","pongsachon.p","Pongsachon","Pornsriniyorm",1);
            User.create("b5610545757","manatsawin.h","Manatsawin","Hanmongkolchai",1);
            User.create("b5610546770", "varis.k", "Varis","Kritpolchai",1);
            Team.create("GG","22,23,24");

            //JDED
            User.create("b5410545044","warrunyou.r","Waranyu","Rerkdee",1);
            User.create("b5410545052","supayut.r","Supayut","Raksuk",1);
            User.create("b5410546334", "wasin.ha","Wasin","Hawaree",1);
            User.create("b5410546393", "akkarawit.p","Akkarawit","Piyawin",1);
            User.create("b5410547594","nachanok.su","Nachanok","Suktarachan",1);
            Team.create("JDED","25,26,27,28,29");

            //Malee
            User.create("b5610545048","tanatorn.a","Tanatorn","Assawaamnuey",1);
            User.create("b5610545714","patawat.w","Patawat","Watakul",1);
            User.create("b5610546745","thanyaboon.t","Thanyaboon","Tovorapan",1);
            User.create("b5610546761","mintra.t","Mintra","Thirasirisin",1);
            Team.create("Malee","30,31,32,33");

            //TheFrank
            User.create("b5610545692","thanachote.v","Thanachote","Visetsuthimout",1);
            User.create("b5610546281","perawith.j","Perawith","Jarunithi",1);
            User.create("b5610546681","kittinan.n","Kittinan","Napapongsa",1);
            User.create("b5610546729","thanaphon.k","Thanaphon","Ketsin",1);
            User.create("b5610546753","nathas.y","Nathas","Yingsukamol",1);
            Team.create("TheFrank","34,35,36,37,38");

            //Staff
            User.create("b5510546166","sarun.wo","Sarun","",3);
            User.create("b5410545036","thai.p","Thai","",3);
            User.create("fengjeb","james.b","Jim","",3);
            User.create("geedev","keeratipong.u","Keeratipong","",3);

            VoteCategory.create("Ease of use");
            VoteCategory.create("Reliability/Stability");
            VoteCategory.create("Completeness");
            VoteCategory.create("Security");
            VoteCategory.create("Quality of UI");

        }

        if (session().get("username") != null) {
            routes.ProjectController.toProjectListPage();
        }
        return ok(login.render(Form.form(Login.class)));
    }

    @Security.Authenticated(Secured.class)
    public static Result toTestPage() {
        return ok(test.render(User.findByUsername(request().username())));
    }

    public static Result toClockPage() {
        return ok(testclock.render());
    }

    public static Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("username", loginForm.get().username);
            User user = User.findByUsername(loginForm.get().username);
            return redirect(
                    routes.ProjectController.toProjectListPage()
            );
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.Application.toLoginPage());
    }

    public static class Login {
        public String username;
        public String password;

        public String validate() {
            if(User.authenticate(username, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }
    }

}