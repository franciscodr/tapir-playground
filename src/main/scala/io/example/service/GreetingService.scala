package io.example.service

import cats.ApplicativeThrow
import cats.syntax.applicative._
import cats.syntax.applicativeError._
import io.example.model.{HelloWorldResponse, PersonaNonGrata}

class GreetingService[F[_]: ApplicativeThrow] {
  def greeting(name: String): F[HelloWorldResponse] =
    if (name.equalsIgnoreCase("troll"))
      PersonaNonGrata(name).raiseError[F, HelloWorldResponse]
    else
      HelloWorldResponse(s"Hi $name!").pure
}
