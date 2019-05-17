import org.joda.time.DateTime

case class Users(_id: Int, name: String, mail: String, number: Long, lastDay: DateTime)

case class createUsers(name: String, mail: String, number: Long, lastDay: String)

case class updateUsers(name: Option[String], mail: Option[String], number: Option[Long], lastDay: Option[String])