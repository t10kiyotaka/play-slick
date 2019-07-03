package models

import java.time.LocalDateTime

import play.api.data.Form
import play.api.data.Forms._

case class Message(
  id:        Int,
  personId:  Int,
  message:   String,
  createdAt: LocalDateTime
)

object Message {
  val messageForm: Form[MessageForm] = Form {
    mapping(
      "personId" -> number,
      "message" -> nonEmptyText
    )(MessageForm.apply)(MessageForm.unapply)
  }
}

case class MessageForm(personId: Int, message: String)

case class MessageWithPerson(message: Message, person: Person)
