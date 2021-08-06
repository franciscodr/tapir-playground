package io.example.http

import io.example.model._
import sttp.model.StatusCode
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir._

object Endpoints {
  val helloWorld: Endpoint[String, StatusCode, HelloWorldResponse, Any] =
    endpoint.get
      .name("Hello world")
      .description("Dummy endpoint that say hi!")
      .in("hello")
      .in(path[String](name = "name"))
      .out(jsonBody[HelloWorldResponse])
      .errorOut(
        statusCode
          .description(StatusCode.NotFound, "The provided employee id is not valid")
          .description(StatusCode.BadRequest, "Some of the provided parameters are incorrect")
      )

  val getUserById: Endpoint[Long, UserError, UserResponse, Any] =
    endpoint.get
      .name("Get user by id")
      .description("Return the user associated with the given id")
      .in("user")
      .in(path[Long](name = "id"))
      .out(jsonBody[UserResponse])
      .errorOut(
        oneOf[UserError](
          oneOfMapping(StatusCode.NotFound, jsonBody[UserNotFound]),
          oneOfMapping(StatusCode.BadRequest, jsonBody[UserIdNotValid])
        )
      )

  val getUserByHandle: Endpoint[String, UserError, UserResponse, Any] =
    endpoint.get
      .name("Get user by handle")
      .description("Return the user associated with the given id")
      .in("user")
      .in("handle")
      .in(path[String](name = "handle"))
      .out(jsonBody[UserResponse])
      .errorOut(
        oneOf[UserError](
          oneOfMapping(StatusCode.NotFound, jsonBody[UserNotFound]),
          oneOfMapping(StatusCode.BadRequest, jsonBody[UserHandleNotValid])
        )
      )

  val all = List(helloWorld, getUserById, getUserByHandle)
}
