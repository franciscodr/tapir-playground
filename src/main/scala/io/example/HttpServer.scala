package io.example

import cats.effect.{IO, IOApp}
import com.comcast.ip4s.IpLiteralSyntax
import io.example.algebra.UserRepository
import io.example.http.Routes
import io.example.service.{GreetingService, UserService}
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.slf4j.Slf4jLogger

object HttpServer extends IOApp.Simple {
  override def run: IO[Unit] = {
    implicit val userRepository: UserRepository[IO] = UserRepository.impl[IO]
    val routes = new Routes[IO](new GreetingService[IO], new UserService[IO])
    Slf4jLogger
      .create[IO].flatMap(logger =>
        EmberServerBuilder
          .default[IO]
          .withLogger(logger)
          .withHost(host"localhost")
          .withPort(port"8081")
          .withHttpApp(routes.routes.orNotFound)
          .build
          .use(_ => IO.never)
      )
  }
}
