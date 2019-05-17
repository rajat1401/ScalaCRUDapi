
object UserValidator {

  def validation(user: createUsers): Option[ApiError] = {
    if(user.name.isEmpty) Some(ApiError.emptyTitle)
    else None
  }

}
