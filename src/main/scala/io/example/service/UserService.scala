package io.example.service

import cats.ApplicativeThrow
import io.example.algebra.UserRepository
import io.example.model.{User, UserHandleNotValid, UserIdNotValid}

class UserService[F[_]: ApplicativeThrow: UserRepository] {
  def getUserByHandle(handle: String): F[User] =
    if (handle.headOption.forall(_.isDigit))
      ApplicativeThrow[F].raiseError(UserHandleNotValid(handle))
    else
      UserRepository[F].getUserByHandle(handle)

  def getUserById(id: Long): F[User] =
    if (id <= 0)
      ApplicativeThrow[F].raiseError(UserIdNotValid(id))
    else
      UserRepository[F].getUserById(id)
}
