package io.example.http

import cats.effect.Async
import cats.syntax.applicativeError._
import cats.syntax.either._
import cats.syntax.functor._
import cats.syntax.semigroupk._
import io.example.model._
import io.example.service.{GreetingService, UserService}
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.implicits._
import sttp.model.StatusCode
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.server.interceptor.ValuedEndpointOutput
import sttp.tapir.server.interceptor.exception.{ExceptionContext, ExceptionHandler}
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.{statusCode, stringBody}

object ServiceExceptionHandler extends ExceptionHandler {
  override def apply(ctx: ExceptionContext): Option[ValuedEndpointOutput[_]] =
    Some(
      ValuedEndpointOutput(statusCode.and(stringBody), (StatusCode.InternalServerError, "BOOM!"))
    )
}

class Routes[F[_]: Async](greetingService: GreetingService[F], userService: UserService[F]) {

  def helloWorldRoute: ServerEndpoint[String, StatusCode, HelloWorldResponse, Any, F] =
    Endpoints.helloWorld.serverLogic(name => greetingService.greeting(name).map(_.asRight))

  def getUserByHandleRoute: ServerEndpoint[String, UserError, UserResponse, Any, F] =
    Endpoints.getUserByHandle.serverLogic(handle =>
      userService
        .getUserByHandle(handle)
        .map(UserResponse.fromUser(_).asRight[UserError])
        .handleError {
          case e: UserHandleNotValid => e.asLeft[UserResponse]
          case e: UserNotFound => e.asLeft[UserResponse]
          case _ => UnexpectedError.asLeft[UserResponse]
        }
    )

  def swaggerRoutes: HttpRoutes[F] =
    Http4sServerInterpreter().toRoutes(SwaggerUI[F](OpenAPI.yamlContent))

  def routes: HttpRoutes[F] =
    Http4sServerInterpreter().toRoutes(List(helloWorldRoute, getUserByHandleRoute)) <+>
      Http4sServerInterpreter().toRouteRecoverErrors(Endpoints.getUserById) { id: Long =>
        userService.getUserById(id).map(UserResponse.fromUser)
      } <+>
      swaggerRoutes

  def routesWithErrorHandler: HttpApp[F] =
    new RoutesWithErrorHandler[F](routes).routesWithErrorHandler.orNotFound
}
