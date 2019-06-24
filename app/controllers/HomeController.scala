package controllers

import javax.inject._
import models.{Person, PersonRepository}
import play.api._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(
  repository: PersonRepository,
  cc: MessagesControllerComponents
)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def index() = Action.async { implicit request =>
    repository.list().map { people =>
      Ok(views.html.index("People Data.", people))
    }
  }

  def show(id: Int) = Action.async { implicit requst =>
    repository.get(id).map { person =>
      Ok(views.html.show("People Data.", person))
    }
  }

  def add() = Action { implicit request =>
    Ok(views.html.add(
      "Please write form",
      Person.personForm
    ))
  }

  def create() = Action async { implicit request =>
    Person.personForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.add("error.", errorForm)))
      },
      person => {
        repository.create(person.name, person.mail, person.tel)
        Future.successful(Redirect(routes.HomeController.index()))
//        repository.create(person.name, person.mail, person.tel).map { _ =>
//          Redirect(routes.HomeController.index())
//        }
      }
    )
  }
}
