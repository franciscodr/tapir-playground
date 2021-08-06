package io.example.model

import io.circe.Codec
import io.circe.generic.semiauto._

case class HelloWorldResponse(greeting: String)

object HelloWorldResponse {
  implicit val codec: Codec[HelloWorldResponse] = deriveCodec[HelloWorldResponse]
}
