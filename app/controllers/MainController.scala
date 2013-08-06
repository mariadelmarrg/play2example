package controllers

import play.api.mvc.Controller
import play.api.mvc.Action

object MainController extends Controller{

  def index = Action {
    implicit req => 
      if(req.session.get("username").isEmpty) Unauthorized(views.html.common.unauthorized())
      else Ok(views.html.main())
  }
  
}