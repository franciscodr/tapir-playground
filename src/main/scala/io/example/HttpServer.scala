package io.example

import cats.effect.{IO, IOApp}
import io.example.algebra.UserRepository
import io.example.http.Routes
import io.example.service.{GreetingService, UserService}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import org.http4s.syntax.kleisli._

object HttpServer extends IOApp.Simple {
  override def run: IO[Unit] = {
    implicit val userRepository: UserRepository[IO] = UserRepository.impl[IO]
    val routes = new Routes[IO](new GreetingService[IO], new UserService[IO])
    val httpApp = Logger.httpApp[IO](logHeaders = true, logBody = true)(routes.routes.orNotFound)
    BlazeServerBuilder[IO](runtime.compute)
      .bindHttp(8081, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
  }
}
