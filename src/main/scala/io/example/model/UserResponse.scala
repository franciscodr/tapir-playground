package io.example.model

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

case class UserResponse(name: String, age: Int)

object UserResponse {
  def fromUser(user: User): UserResponse = UserResponse(user.name, user.age)
  implicit val codec: Codec[UserResponse] = deriveCodec[UserResponse]
}
