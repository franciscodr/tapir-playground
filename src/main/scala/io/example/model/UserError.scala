package io.example.model

import io.circe.generic.semiauto.deriveDecoder
import io.circe.{Codec, Encoder, Json}

abstract class UserError(val errorMessage: String) extends Throwable(errorMessage)

case class PersonaNonGrata(name: String)
    extends UserError(errorMessage = "This user is not welcomed here")

case class UserNotFound(identifier: String) extends UserError(errorMessage = "User not found")

object UserNotFound {
  implicit def codec: Codec[UserNotFound] = Codec.from(
    deriveDecoder[UserNotFound],
    Encoder.instance(error =>
      Json.obj(
        ("message", Json.fromString(error.errorMessage)),
        ("identifier", Json.fromString(error.identifier))
      )
    )
  )
}

case class UserIdNotValid(id: Long)
    extends UserError(errorMessage = s"The user id is not valid. It should be a positive number.")

object UserIdNotValid {
  implicit def codec: Codec[UserIdNotValid] = Codec.from(
    deriveDecoder[UserIdNotValid],
    Encoder.instance(error =>
      Json.obj(
        ("message", Json.fromString(error.errorMessage)),
        ("id", Json.fromLong(error.id))
      )
    )
  )
}

case class UserHandleNotValid(handle: String)
    extends UserError(
      errorMessage = s"The user handle is not valid. It shouldn't start with number"
    )

object UserHandleNotValid {
  implicit def codec: Codec[UserHandleNotValid] = Codec.from(
    deriveDecoder[UserHandleNotValid],
    Encoder.instance(error =>
      Json.obj(
        ("message", Json.fromString(error.errorMessage)),
        ("handle", Json.fromString(error.handle))
      )
    )
  )
}

case object UnexpectedError
    extends UserError(errorMessage = s"Something wrong happened when processing the request") {
  implicit def codec: Codec[UnexpectedError.type] = Codec.from(
    deriveDecoder[UnexpectedError.type],
    Encoder.instance(error =>
      Json.obj(
        ("message", Json.fromString(error.errorMessage))
      )
    )
  )
}
