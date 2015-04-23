package controllers;

import com.avaje.ebean.Ebean;
import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import play.data.DynamicForm;
import views.html.*;

import javax.servlet.annotation.ServletSecurity;
import java.sql.Timestamp;
import java.util.*;

public class RateController extends Controller {

    @Security.Authenticated(AdminSecured.class)
    public static Result rate(Long projectId) {
        DynamicForm form = Form.form().bindFromRequest();
        if (form.hasErrors()) {
            return redirect(routes.Application.toErrorPage());
        }

        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);

        List<RateCategory> ratecatList = RateCategory.all();
        int size = ratecatList.size();
        for (int i = 0 ; i < size ; i++) {
            if (form.get(ratecatList.get(i).getName()) != null) {
                Rate rate = Rate.getUniqueRate(request().username(), Project.findById(projectId),ratecatList.get(i));
                if(rate == null) {
                    rate = new Rate();
                    int newScore = Integer.parseInt(form.get(ratecatList.get(i).getName()));
                    String isNoRate = form.get( "noRate"+ratecatList.get(i).getName() );
                    if( isNoRate != null )
                        rate.setScore( -1 );
                    else {
                        if (newScore > 5 || newScore < 0) {
                            return redirect(routes.Application.toErrorPage());
                        }
                        rate.setScore(newScore);
                    }
                    rate.setUser(user);
                    rate.setProject(project);
                    rate.setCategory(ratecatList.get(i));
                    rate.setTimestamp();
                    rate.save();
                }
                else{
                    int newScore = Integer.parseInt(form.get(ratecatList.get(i).getName()));
                    String isNoRate = form.get( "noRate"+ratecatList.get(i).getName() );
                    if( isNoRate != null )
                        rate.setScore( -1 );
                    else {
                        if (newScore > 5 || newScore < 0) {
                            return redirect(routes.Application.toErrorPage());
                        }
                        rate.setScore(newScore);
                    }
                    rate.setTimestamp();
                    Ebean.update(rate);
                }

                Logger.info("[ " + request().username() + " ] has rate for project #" + project.getId() + ", category #" + ratecatList.get(i) + ", score = " + rate.getScore());
            } else {
                Logger.error("[ " + request().username() + " ] fail to rate");
                redirect(routes.Application.toErrorPage());
            }
        }
        return redirect(routes.ProjectController.toProjectPage(project.getId()));
    }

    @Security.Authenticated(Secured.class)
    public static Result toRatePage(long projectId) {
        //return redirect(routes.RateController.toRateClosedPage(projectId));
        Logger.info("[ " + request().username() + " ] arrive at rate project #" + projectId + " page.");

        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);
        List<RateCategory> rateCategories = RateCategory.all();

        response().setHeader("Cache-Control","no-cache");
        return ok(rate.render(user,project,rateCategories));
    }

//    @Security.Authenticated(Secured.class)
//    public static Result toRateClosedPage(long projectId) {
//        Logger.info("[ " + request().username() + " ] arrive at rate is now closed page.");
//
//        User user = User.findByUsername(request().username());
//        Project project = Project.findById(projectId);
//        List<RateCategory> rateCategories = RateCategory.all();
//
//        response().setHeader("Cache-Control","no-cache");
//        return ok(voteisnowclosed.render(user, project));
//    }

    @Security.Authenticated(AdminSecured.class)
    public static Result toResultPage() {
        Logger.info("[ " + request().username() + " ] arrive at result page.");
        List<Rate> rateList = Rate.getAllRates();
        List<RateCategory> catList = RateCategory.all();
        User user = User.findByUsername(request().username());

        List<Project> projList = Project.getAllProjects();
        response().setHeader("Cache-Control","no-cache");
        return ok(result.render(user, rateList,catList,projList));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result addRateCat() {
        Form<RateCategory> rateCatForm = Form.form(RateCategory.class).bindFromRequest();
        if (rateCatForm.hasErrors()) {
            User user = User.findByUsername(request().username());
            List<RateCategory> rateCatList = RateCategory.getAllCategories();
            Logger.error("[ " + request().username() + " ] fail to add rate cat.");
            return badRequest(addratecat.render(user, rateCatList, rateCatForm));
        }
        RateCategory rateCat = rateCatForm.get();
        rateCat.save();
        Logger.info("[ " + request().username() + " ] success add rate cat #" + rateCat.getId());
        return redirect(routes.RateController.toAddRateCatPage());
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result toAddRateCatPage(){
        Logger.info("[ " + request().username() + " ] arrive at add rate category page.");
        List<RateCategory> ratecatlist = RateCategory.all();
        User user = User.findByUsername(request().username());

        response().setHeader("Cache-Control","no-cache");
        return ok(addratecat.render(user, ratecatlist, Form.form(RateCategory.class)));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result removeRateByCatId(){
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        RateCategory rateCat = RateCategory.findById(Long.parseLong(form.data().get("cId")));
        List<Rate>  allRates = Rate.getAllRates();
        for( Rate rate: allRates){
            if(rateCat.getId() == rate.getCategory().getId()){
                rate.delete();
            }

        }
        rateCat.delete();
        return redirect(routes.RateController.toAddRateCatPage());
    }

}