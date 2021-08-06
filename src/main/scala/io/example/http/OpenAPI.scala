package io.example.http

import cats.syntax.option._
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi._
import sttp.tapir.openapi.circe.yaml._

object OpenAPI {
  val docs: OpenAPI = OpenAPIDocsInterpreter()
    .toOpenAPI(es = Endpoints.all, title = "CU Integrations", version = "0.1")
    .servers(
      List(
        Server(
          "/",
          "Local server".some
        )
      )
    )

  val yamlContent: String = docs.toYaml
}
