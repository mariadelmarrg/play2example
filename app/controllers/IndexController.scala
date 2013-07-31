package controllers

import play.api._
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import play.api.data.Field
import play.api.db.DB
import play.api.Play.current
import play.api.data.validation.Constraint
import model.Client
import services._

object IndexController extends Controller{

  
  val clientService: ClientService = ClientServiceImpl;

  val signInForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText)
      verifying (
        "Invalid email or password",
        result =>
          DB.withConnection {
            implicit conn =>
              {
                clientService.checkAuthenticationDetails(result._1, result._2)
              }
          }))
  
  def index = Action {
    implicit req => 
      if(req.session.get("username").isEmpty) Ok(views.html.index(signInForm, req.flash.get("status")))
      else Ok(views.html.main())
  }
  
  def signIn = Action {
    implicit req =>
      DB.withConnection {
        implicit conn =>
          signInForm.bindFromRequest.fold(
            { errors => BadRequest(views.html.index(errors)) },
            { form => Redirect(routes.MainController.index()).withSession(Security.username -> form._1)}
          )
      }
  }
  
}