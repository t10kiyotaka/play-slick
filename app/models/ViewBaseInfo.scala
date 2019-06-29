package models

case class ViewBaseInfo(
  title:  String,
  header: String,
  footer: String = "copyright 2018."
)

object ViewBaseInfo {

  def from(pageIs: String): ViewBaseInfo = pageIs match {
    case "index"  => ViewBaseInfo("Index Page", "Hello")
    case "show"   => ViewBaseInfo("Show Person", "Show")
    case "add"    => ViewBaseInfo("Add, Person", "Add")
    case "edit"   => ViewBaseInfo("Edit Person", "Edit")
  }

}



