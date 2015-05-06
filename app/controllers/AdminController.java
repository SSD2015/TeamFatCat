package controllers;

import models.Deadline;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.adddeadline;
import views.html.admin;

public class AdminController extends Controller{

    @Security.Authenticated(AdminSecured.class)
    public static Result toAdminPage() {
        response().setHeader("Cache-Control", "no-cache");
        return ok(admin.render(User.findByUsername(request().username())));
    }


    @Security.Authenticated(AdminSecured.class)
    public static Result toDeadlinePage() {
        response().setHeader("Cache-Control", "no-cache");
        return ok(adddeadline.render(User.findByUsername(request().username())));
    }
    @Security.Authenticated(AdminSecured.class)
    public static Result editDeadline() {
        DynamicForm form = new DynamicForm().bindFromRequest();
        String dateandtime = form.data().get("DeadLineInput");
        String[] datesplit = dateandtime.split(" ");
        String date = datesplit[0];
        String time = datesplit[1];
        String[] mdy = date.split("/"); //month day year
        String[] hms = time.split(":"); // hour minute second
        String M = datesplit[2];
        int year = Integer.parseInt(mdy[2]);
        int month = Integer.parseInt(mdy[0]);
        int day = Integer.parseInt(mdy[1]);
        int hour = Integer.parseInt(hms[0]);
        int minute = Integer.parseInt(hms[1]);
        if(M.equals("PM")){
            hour+=12;

        }
        Deadline.create(year , month, day, hour, minute, 0);
        return redirect(routes.AdminController.toDeadlinePage());
    }

}