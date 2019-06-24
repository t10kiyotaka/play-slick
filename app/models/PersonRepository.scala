package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PersonRepository @Inject() (
  dbConfigProvider: DatabaseConfigProvider
)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class PeopleTable(tag: Tag) extends Table[Person](tag, "people") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def mail = column[String]("mail")
    def tel = column[String]("tel")

    def * = (id, name, mail, tel) <>
      ((Person.apply _).tupled, Person.unapply)
  }

  private val people = TableQuery[PeopleTable]

  def list(): Future[Seq[Person]] = db.run {
    people.result
  }

  def get(id: Int): Future[Person] = db.run {
    people.filter(_.id === id)
      .result
      .head
  }

  def create(name: String, mail: String, tel: String): Future[Int] = db.run {
    people += Person(0, name, mail, tel)
  }

}
