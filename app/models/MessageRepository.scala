package models

import java.time.LocalDateTime

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MessageRepository @Inject() (
  dbConfigProvider: DatabaseConfigProvider
)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class MessageTable(tag: Tag) extends Table[Message](tag, "message") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def personId = column[Int]("person_id")
    def message = column[String]("message")
    def createdAt = column[LocalDateTime]("created_at")

    def * = (id, personId, message, createdAt) <>
      ((Message.apply _).tupled, Message.unapply)
  }

  private val messages = TableQuery[MessageTable]

  def list(): Future[Seq[Message]] = db.run {
    messages.result
  }

  def get(id: Int): Future[Message] = db.run {
    messages.filter(_.id === id)
      .result
      .head
  }

  def create(personId: Int, message: String, createdAt: LocalDateTime): Future[Int] = db.run {
    messages += Message(0, personId, message, createdAt)
  }

  def update(messageInstance: Message): Future[Int] = db.run {
    messages.insertOrUpdate(messageInstance)
  }

  def delete(id: Int): Future[Int] = db.run {
    messages.filter(_.id === id).delete
  }

  def filterByPersonId(pId: Int): Future[Seq[Message]] = db.run {
    messages.filter{ row =>
      row.personId === pId
    }.result
  }
}
