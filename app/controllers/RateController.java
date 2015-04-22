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

    @Security.Authenticated(AdminSecured.class)
    public static Result rate(Long projectId) {
        DynamicForm form = Form.form().bindFromRequest();
        if (form.hasErrors()) {
            return redirect(routes.Application.toErrorPage());
        }

        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);

        List<RateCategory> rateCategories = RateCategory.findAll();
        int size = rateCategories.size();
        for (int i = 0 ; i < size ; i++) {
            if (form.get(rateCategories.get(i).getName()) != null) {
                boolean isNotRated = (form.get("noRate"+rateCategories.get(i).getName()) != null);

                Rate rate = null;

                if (isNotRated) {
                    Rate.create(user, project, rateCategories.get(i), -1);
                } else {
                    Rate.create(user, project, rateCategories.get(i), Integer.parseInt(form.get(rateCategories.get(i).getName())));
                }

                if (rate == null) {
                    return toBadRequestRatePage(project.getId());
                }
            } else {
                redirect(routes.Application.toErrorPage());
            }
        }

        return redirect(routes.ProjectController.toProjectPage(project.getId()));
    }

    @Security.Authenticated(Secured.class)
    public static Result toRatePage(long projectId) {
        //return redirect(routes.RateController.toRateClosedPage(projectId));
        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);
        List<RateCategory> rateCategories = RateCategory.findAll();

        response().setHeader("Cache-Control","no-cache");
        return ok(rate.render(user,project,rateCategories));
    }

    @Security.Authenticated(Secured.class)
    public static Status toBadRequestRatePage(long projectId) {
        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);
        List<RateCategory> rateCategories = RateCategory.findAll();

        response().setHeader("Cache-Control","no-cache");
        return badRequest(rate.render(user, project, rateCategories));
    }

    @Security.Authenticated(Secured.class)
    public static Result toRateClosedPage(long projectId) {
        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);
        List<RateCategory> rateCategories = RateCategory.findAll();

        response().setHeader("Cache-Control","no-cache");
        return ok(voteisnowclosed.render(user, project));
    }

    @Security.Authenticated(Secured.class)
    public static Result toResultPage() {
        List<Rate> rateList = Rate.findAll();
        List<RateCategory> catList = RateCategory.findAll();
        User user = User.findByUsername(request().username());

        response().setHeader("Cache-Control","no-cache");
        return ok(result.render(user, rateList,catList));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result addRateCat() {
        Form<RateCategory> rateCatForm = Form.form(RateCategory.class).bindFromRequest();
        if (rateCatForm.hasErrors()) {
            User user = User.findByUsername(request().username());
            List<RateCategory> rateCatList = RateCategory.findAll();
            return badRequest(addratecat.render(user, rateCatList, rateCatForm));
        }
        RateCategory rateCat = rateCatForm.get();
        rateCat.save();
        return redirect(routes.RateController.toAddRateCatPage());
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result toAddRateCatPage(){
        List<RateCategory> ratecatlist = RateCategory.findAll();
        User user = User.findByUsername(request().username());

        response().setHeader("Cache-Control","no-cache");
        return ok(addratecat.render(user, ratecatlist, Form.form(RateCategory.class)));
    }


}