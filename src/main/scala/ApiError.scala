import akka.http.scaladsl.model.{StatusCode, StatusCodes}

final case class ApiError private(statusCode: StatusCode, message: String)

object ApiError{

  private def apply(statusCode: StatusCode, message: String): ApiError = new ApiError(statusCode, message)

  val generic: ApiError = new ApiError(StatusCodes.InternalServerError, message= "Unknown Error.")

  val emptyTitle: ApiError = new ApiError(StatusCodes.BadRequest, message = "Name can't be empty")

//  val emptyMail: ApiError = new ApiError(StatusCodes.BadRequest, message = "")
}