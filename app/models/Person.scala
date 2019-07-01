package models

import play.api.data.Form
import play.api.data.Forms._

object Person {
  val personForm: Form[PersonForm] = Form {
    val regexForEmail = """[a-zA-Z0-9.+_-]+@[A-Z0-9.-]+\.[A-Z]"""
    mapping(
      "name" -> nonEmptyText
        .verifying("3文字以上で入力してください", s => isGreaterThanN(s, 2))
        .verifying("10文字以下で入力してください", s => isLessThanN(s, 11)),
      "mail" -> email
        .verifying("正しいメールアドレスを入力してください", s => s.matches(regexForEmail)),
      "tel" -> nonEmptyText
        .verifying("10桁または11桁の半角数字で入力してください", s => s.matches("""[0-9]{10,11}""")),
    )(PersonForm.apply)(PersonForm.unapply)
  }

  def isGreaterThanN(s: String, n: Int): Boolean =
    if(s.length > n) true else false

  def isLessThanN(s: String, n: Int): Boolean =
    if(s.length < n) true else false

  val personFindForm: Form[PersonFind] = Form {
    mapping(
      "query" -> text
    )(PersonFind.apply)(PersonFind.unapply)
  }
}

case class Person(id: Int, name: String, mail: String, tel: String)
case class PersonForm(name: String, mail: String, tel: String)
case class PersonFind(query: String)
