import akka.http.scaladsl.server.{Directives, Route}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.Decoder.Result
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import org.joda.time.DateTime

import scala.util.{Failure, Success}


//trait Router {
//  def route: Route
//}




class TodoRouter(todoRepository: todorepository) extends Directives {

  implicit val TimestampFormat: Encoder[DateTime] with Decoder[DateTime] = new Encoder[DateTime] with Decoder[DateTime] {
    override def apply(a: DateTime): Json = Encoder.encodeString.apply(a.toString("dd-MM-yyyy"))

    override def apply(c: HCursor): Result[DateTime] = Decoder.decodeLong.map(s => new DateTime(s)).apply(c)
  }


  def route: Route = pathPrefix("todos") {
    pathEndOrSingleSlash {
      get {
        onComplete(todoRepository.get_todos()) {
          case Success(list) => complete {
            list.map(_.asJson)
          }
          case Failure(exception) => complete {
            exception.toString
          }
        }
      } ~ post {
        entity(as[createUsers]) {user =>
          UserValidator.validation(user) match {
            case Some(apiError) =>
              complete(apiError.statusCode, apiError.message)
            case None =>
              complete(todoRepository.insert_todo(user))
          }
        }
      }
    } ~ path(IntNumber) { page => //working nicely
      get {
        onComplete(todoRepository.get_todos_page(page)) {
          case Success(list) => complete {
            list.map(_.asJson)
          }
          case Failure(exception) => complete {
            exception.toString
          }
        }
      }
    } ~ path("start" / Segment / "end" / Segment / "sortBy" / Segment) { (start, end, sortBy) =>
      onComplete(todoRepository.sort(start, end, sortBy)) {
        case Success(list) => complete {
          list.map(_.asJson)
        }
        case Failure(exception) => complete {
          exception.toString
        }
      }
    } ~ path("start" / Segment / "end" / Segment / "sortBy" / Segment / "page" / IntNumber / "limit" / IntNumber) { (start, end, sortBy, page, limit) =>
      get {
        onComplete(todoRepository.sort(start, end, sortBy, page, limit)) {
          case Success(list) => complete {
            list.map(_.asJson)
          }
          case Failure(exception) => complete {
            exception.toString
          }
        }
      }
    }
  }
}