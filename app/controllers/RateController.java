package controllers;

import com.avaje.ebean.Ebean;
import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import play.data.DynamicForm;
import views.html.*;

import java.sql.Timestamp;
import java.util.*;

public class RateController extends Controller {

    @Security.Authenticated(Secured.class)
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
                    else
                        rate.setScore( newScore );
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
                    else
                        rate.setScore( newScore );
                    rate.setTimestamp();
                    Ebean.update(rate);
                }
            } else {
                redirect(routes.Application.toErrorPage());
            }
        }

        return redirect(routes.ProjectController.toProjectPage(project.getId()));
    }

    @Security.Authenticated(Secured.class)
    public static Result toRatePage(long projectId) {
        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);
        List<RateCategory> rateCategories = RateCategory.all();

        return ok(rate.render(user,project,rateCategories));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result toResultPage() {
        List<Rate> rateList = Rate.getAllRates();
        List<RateCategory> catList = RateCategory.all();
        List<Project> progList = Project.getAllProjects();
        User user = User.findByUsername(request().username());
        return ok(result.render(user, rateList,catList,progList));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result addRateCat() {
        Form<RateCategory> rateCatForm = Form.form(RateCategory.class).bindFromRequest();
        if (rateCatForm.hasErrors()) {
            User user = User.findByUsername(request().username());
            List<RateCategory> rateCatList = RateCategory.getAllCategories();
            return badRequest(addratecat.render(user, rateCatList, rateCatForm));
        }
        RateCategory rateCat = rateCatForm.get();
        rateCat.save();
        return redirect(routes.RateController.toAddRateCatPage());
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result toAddRateCatPage(){
        List<RateCategory> ratecatlist = RateCategory.all();
        User user = User.findByUsername(request().username());
        return ok(addratecat.render(user, ratecatlist, Form.form(RateCategory.class)));
    }


}