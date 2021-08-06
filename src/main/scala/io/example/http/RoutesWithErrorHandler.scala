package io.example.http

import cats.data.{Kleisli, OptionT}
import cats.syntax.applicativeError._
import cats.{Applicative, ApplicativeThrow}
import io.example.model.{PersonaNonGrata, UserHandleNotValid, UserIdNotValid, UserNotFound}
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Request, Response}

class RoutesWithErrorHandler[F[_]: Applicative](routes: HttpRoutes[F]) extends Http4sDsl[F] {
  private val handler: Throwable => F[Response[F]] = {
    case e: PersonaNonGrata => BadGateway(e.errorMessage)
    case e: UserNotFound => NotFound(e.errorMessage)
    case e: UserIdNotValid => BadRequest(e.errorMessage)
    case e: UserHandleNotValid => BadRequest(e.errorMessage)
    case _ => InternalServerError("Something went wrong, please try again")
  }

  def routesWithErrorHandler(implicit ev: ApplicativeThrow[OptionT[F, *]]): HttpRoutes[F] =
    Kleisli { req: Request[F] =>
      routes.run(req).handleErrorWith(e => OptionT.liftF(handler(e)))
    }
}
