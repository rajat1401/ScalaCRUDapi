//correct the return of the functions..
//import akka.http.scaladsl.model.DateTime
import Helpers.DocumentObservable
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.{MongoCollection, _}

import scala.concurrent.{ExecutionContext, Future}


//trait todorepo[MongoCollection[Document]] {//no need to define full methods here, its a trait. /PUN{Methods are supposed to be overridden}
//
//  def get_todos(): Future[Seq[Document]]
//
////  def get_inrange(collection: MongoCollection[Document], d1: Date, d2: Date): Future[Seq[Document]]
////
////  def insert_todo(collection: MongoCollection[Document], doc: Document): Future[Seq[Document]]
////
////  def delete_todo(collection: MongoCollection[Document], id: Int): Future[Seq[Document]]
////
////  def update_todo(collection: MongoCollection[Document], id: Int, updatedoc: Document): Future[Seq[Document]]
//
//  def sort_name_ascending(): Future[Seq[Document]]
//
//  def sort_name_descending(): Future[Seq[Document]]
//
//  def sort_numbers_ascending(): Future[Seq[Document]]
//
//  def sort_numbers_descending(): Future[Seq[Document]]
//
//}

//case class mammal(color: String, legs: Int, born: DateTime)

//case class Document(_id: Int, name: String, mail: String, number: Long, lastDay: DateTime)


class todorepository(collection: MongoCollection[Users])(implicit ec: ExecutionContext){

  def get_todos(): Future[Seq[Users]] = {
//    println (collection.find().sort(descending("_id")).headResult()._id + 1)
    collection.find().toFuture()
  }

//  def get_hello_world(): Future[List[Users]] = {
//    val a = Users(1, "brown", "rajat@joveo.com", 9811802432L, DateTime(2010,5, 10,0,0, 0))
//    val b = Users(2, "blue", "anwesh@joveo.com", 9999999999L, DateTime(2050,5, 10,0,0, 0))
//    Future.successful(List(a, b))
//  }

  def get_todos_page(page: Int): Future[Seq[Users]] = {//pagenation much late
    collection.find().skip(10*page).limit(10).toFuture()
  }

  def sort(startdate: String, enddate: String, sortby: String, page: Int = 0, limit: Int= 20): Future[Seq[Users]] = {
    val formatter = DateTimeFormat.forPattern("dd-MM-yyyy")
    val d1: DateTime= formatter.parseDateTime(startdate)
    val d2: DateTime = formatter.parseDateTime(enddate)
//    val pg: Int = page.toInt
//    val lt: Int = limit.toInt
    collection.find(and(gte("lastDay", d1), lte("lastDay", d2))).sort(ascending(sortby)).skip(10*page).limit(limit).toFuture()
  }

  def sort_numbers_descending(): Future[Seq[Users]] = {
    collection.find().sort(descending("number")).limit(10).toFuture()
  }

  def sort_numbers_ascending(): Future[Seq[Users]] = {
    collection.find().sort(ascending("number")).limit(10).toFuture()
  }

  def get_inrange(d1: String, d2: String): Future[Seq[Users]] = {
    val formatter = DateTimeFormat.forPattern("dd-MM-yyyy")
    val startdate: DateTime= formatter.parseDateTime(d1)
    val enddate: DateTime = formatter.parseDateTime(d2)
    collection.find(and(gte("lastDay", startdate))).toFuture()
//    println (enddate)
//    collection.find().toFuture()
  }

  def sort_name_ascending(): Future[Seq[Users]]= {
    collection.find().sort(ascending("name")).limit(10).toFuture()
  }

  def sort_name_descending(): Future[Seq[Users]]= {
    collection.find().sort(descending("name")).limit(10).toFuture()
  }

  def insert_todo(users: createUsers): Unit = {//do the without id implementation
    val formatter = DateTimeFormat.forPattern("dd-MM-yyyy")
    val date: DateTime = formatter.parseDateTime(users.lastDay)
    val user = Users(collection.find().sort(descending("_id")).headResult()._id + 1, users.name, users.mail, users.number, date)
    collection.insertOne(user)//if id's are implicit, we need to keep this asynchronous, therefore .results()
    //otherwise .printHeadResult() would also do.
    println("Inserted the user.")
  }

  def insert_todo2(users: Users): Unit = {//do the without id implementation
//    val formatter = DateTimeFormat.forPattern("dd-MM-yyyy")
//    val date: DateTime = formatter.parseDateTime(users.lastDay)
//    val user = Users(collection.find().sort(descending("_id")).headResult()._id + 1, users.name, users.mail, users.number, date)
    Future.successful(collection.insertOne(users))//if id's are implicit, we need to keep this asynchronous, therefore .results()
    //otherwise .printHeadResult() would also do.
    println("Updated the user.")
  }

  def updateHelper(founduser: Users, toupdate: updateUsers, date: DateTime): Unit = {
    val formatter = DateTimeFormat.forPattern("dd-MM-yyyy")
    val t1= toupdate.name.map(name => founduser.copy(name= name)).getOrElse(founduser)
    val t2= toupdate.mail.map(mail => founduser.copy(mail= mail)).getOrElse(t1)
    val t3= toupdate.number.map(number => founduser.copy(number= number)).getOrElse(t2)
    toupdate.lastDay.map(date => founduser.copy(lastDay = formatter.parseDateTime(date))).getOrElse(t3)
  }

  //
//  override def delete_todo(collection: MongoCollection[Document], id: Int): Future[Seq[Document]] = {//with the given id
//    collection.deleteOne(equal("id", id)).headResult()
//    println("Deleted the task")
//  }
//

//  def update_todo(id: String, toupdate: updateUsers): Unit = {
////    collection.deleteOne(equal("id", id)).headResult()
//    val actualid: Int= id.toInt
//    collection.find(equal("_id", actualid)) match {
//      case Some() => {
//        val newuser: Users = updateHelper(collection.find(equal("_id", actualid)).headResult(), toupdate, date)
//        insert_todo2(newuser)
//      }
//      case None => println (s"User with id $id not found")
//    }
//  }

}


//  val mongoClient: MongoClient = MongoClient()
//  val db: MongoDatabase = mongoClient.getDatabase("maybelike")
//  val collection: MongoCollection[Document] = db.getCollection("segment")
//  val doc: Document = Document("_id" -> "3", "name" -> "Rajat", "email" -> "rajat@joveo.com", "number" -> 101, "last_day" -> )
//  val format = new SimpleDateFormat("dd/MM/yyyy")
//  val d1:Date = format.parse("24/01/2019")
//  val d2:Date = format.parse("24/09/2019")
//  get_todos(collection, 4)
//  get_inrange(collection, d1, d2)


