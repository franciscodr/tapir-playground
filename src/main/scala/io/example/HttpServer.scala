package io.example

import cats.effect.{IO, IOApp}
import com.comcast.ip4s.IpLiteralSyntax
import io.example.algebra.UserRepository
import io.example.http.Routes
import io.example.service.{GreetingService, UserService}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger

object HttpServer extends IOApp.Simple {
  override def run: IO[Unit] = {
    implicit val userRepository: UserRepository[IO] = UserRepository.impl[IO]
    val routes = new Routes[IO](new GreetingService[IO], new UserService[IO])
    val httpApp = Logger.httpApp[IO](logHeaders = true, logBody = true)(routes.routes.orNotFound)
    EmberServerBuilder
      .default[IO]
      .withHost(host"localhost")
      .withPort(port"8081")
      .withHttpApp(httpApp)
      .build
      .use(_ => IO.never)
  }
}
