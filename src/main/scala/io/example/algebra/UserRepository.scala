package io.example.algebra

import cats.{Applicative, ApplicativeThrow}
import io.example.model.{User, UserNotFound}

trait UserRepository[F[_]] {
  def getUserByHandle(handle: String): F[User]
  def getUserById(id: Long): F[User]
}

object UserRepository {
  def apply[F[_]](implicit ev: UserRepository[F]): UserRepository[F] = ev

  def impl[F[_]: ApplicativeThrow]: UserRepository[F] = new UserRepository[F] {
    override def getUserByHandle(handle: String): F[User] =
      if (handle == "error")
        ApplicativeThrow[F].raiseError(new Throwable("Database connection failed!"))
      else if (handle.startsWith("s"))
        ApplicativeThrow[F].raiseError(UserNotFound(handle))
      else
        Applicative[F].pure(User(100, handle, 42))

    override def getUserById(id: Long): F[User] =
      if (id == 99)
        ApplicativeThrow[F].raiseError(new Throwable("Database connection failed!"))
      else if (id % 5 == 0)
        ApplicativeThrow[F].raiseError(UserNotFound(id.toString))
      else
        Applicative[F].pure(User(id, "John", 42))
  }
}
