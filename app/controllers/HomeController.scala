package controllers

import javax.inject._
import models.{Person, PersonForm, PersonRepository}
import play.api._
import play.api.data.Form
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
      Ok(views.html.index("People Data.", people, Person.personFindForm))
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
      }
    )
  }

  def edit(id: Int) = Action async { implicit  request =>
    repository.get(id).map { person =>
      val fdata: Form[PersonForm] = Person.personForm
        .fill(PersonForm(person.name, person.mail, person.tel))
      Ok(views.html.edit(
        "Edit Person.", fdata, id
      ))
    }
  }

  def update(id: Int) = Action async { implicit  request =>
    Person.personForm.bindFromRequest.fold(
      errorForm => Future.successful(Ok(views.html.edit("errors", Person.personForm, id))),
      person => {
        repository.update(id, person.name, person.mail, person.tel)
        Future.successful(Redirect(routes.HomeController.index))
      }
    )
  }

  def delete(id: Int) = Action async { implicit request =>
    for {
      _ <- repository.delete(id)
    } yield Redirect(routes.HomeController.index())
  }

  def search() = Action async { implicit request =>
    Person.personFindForm.bindFromRequest.fold(
      errorForm => for {
        people <- repository.list()
      } yield Ok(views.html.index("People Data", people, Person.personFindForm)),
      input => for {
        people <- repository.list()
        result <- repository.filterByNameOrMail(input.query)
      } yield Ok(views.html.index("People Data", people, Person.personFindForm, result))
    )
  }

}
