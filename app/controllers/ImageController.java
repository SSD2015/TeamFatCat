package controllers;

import models.Image;
import models.Project;
import models.Team;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.editproject;

import java.io.File;
import java.util.List;

public class ImageController extends Controller {
    @Security.Authenticated(Secured.class)
    public static Result toEditProjectPage(long projectId) {
        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);
        Team team = Team.findById(project.getTeamId());

        if (!team.isMember(user.getId()) && (user.getType() != User.ADMIN)) {
            return redirect(routes.ProjectListController.toProjectListPage());
        }

        List<Image> images = Image.findImagesByProject(projectId);
        return ok(editproject.render(user, project, images));
    }

    @Security.Authenticated(Secured.class)
    public static Result getImage(long imageId) {
        Image image = Image.findById(imageId);

        if (image != null) {
            return ok(image.getData()).as("image");
        } else {
            flash("error", "File not found");
            return redirect(routes.Application.toErrorPage());
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result upload(long projectId, String tag) {
        Http.MultipartFormData form = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart file = form.getFile("image");


        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);

        List<Image> images = Image.findImagesByProject(projectId);

        if (file == null) {
            return badRequest(editproject.render(user, project, images));
        }

        File img = form.getFile("image").getFile();

//        if (img != null) {
//            File file = img;
//            File newFile = new File(play.Play.application().path().toString() + "//public//uploads//"+ "_" + fileName);
//            file.renameTo(newFile); //here you are moving photo to new directory
//            System.out.println(newFile.getPath()); //this pa
//        }
//        Image.create(tag, form.get().file.getFile(), projectId);


        if (tag.equals(Image.AVT)) {
            Image image_a = Image.findByNameAndProject(Image.AVT, projectId);
            if (image_a == null) {
                new Image(Image.AVT, img, projectId);
            } else {
                image_a.setData(img);
                image_a.update();
            }
        } else {
            Image image_s = new Image(Image.SCR, img, projectId);
            image_s.setName(image_s.getName() + image_s.getId());
            image_s.update();
        }

        return redirect(routes.ImageController.toEditProjectPage(projectId));
    }

//    public static class Upload {
//        public Http.MultipartFormData.FilePart file;
//
//        public String validate() {
//
//            file = form.getFile("image");
//
//            if (file == null) {
//                return "No file";
//            }
//
//            return null;
//        }
//    }
}