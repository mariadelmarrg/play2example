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

object SignUpController extends Controller {

  val clientService: ClientService = ClientServiceImpl;

  val signUpForm: Form[Client] = Form(
    mapping(
      "username" -> nonEmptyText.verifying("Username is already taken", username => DB.withConnection { implicit conn => clientService.canUseUsername(username) }),
      "name" -> nonEmptyText,
      "password" -> nonEmptyText)(Client.apply)(Client.unapply))

  def index = Action {
    implicit req => Ok(views.html.signUp(signUpForm))
  }
      
  def signUp = Action {
    implicit req =>
      DB.withConnection {
        implicit conn =>
          signUpForm.bindFromRequest.fold(
            { errors => BadRequest(views.html.signUp(errors)) },
            { user =>
              var client = Client(user.username, user.name, user.password)
              clientService.insert(client)
              Redirect(routes.IndexController.index()).flashing("status" -> "User was created correctly")
            })
      }
  }

  def logout = Action {
    implicit req => Redirect(routes.IndexController.index()).withNewSession
  }

}