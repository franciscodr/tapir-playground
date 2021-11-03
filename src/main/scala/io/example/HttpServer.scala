package io.example

import cats.effect.{IO, IOApp}
import io.example.algebra.UserRepository
import io.example.config.HttpConfig
import io.example.http.Routes
import io.example.service.{GreetingService, UserService}
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.slf4j.Slf4jLogger
import pureconfig.ConfigSource

object HttpServer extends IOApp.Simple {
  override def run: IO[Unit] = {
    implicit val userRepository: UserRepository[IO] = UserRepository.impl[IO]
    val routes = new Routes[IO](new GreetingService[IO], new UserService[IO])
    for {
      config <- IO.fromEither(
        ConfigSource.default
          .at(namespace = "http").load[HttpConfig].left.map(f =>
            new RuntimeException(f.prettyPrint())
          )
      )
      logger <- Slf4jLogger.create[IO]
      server <- EmberServerBuilder
        .default[IO]
        .withLogger(logger)
        .withHost(config.host)
        .withPort(config.port)
        .withHttpApp(routes.routes.orNotFound)
        .build
        .use(_ => IO.never)
    } yield server
  }
}
