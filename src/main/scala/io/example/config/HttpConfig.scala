package io.example.config

import com.comcast.ip4s._
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert
import pureconfig.generic.semiauto._

case class HttpConfig(host: Host, port: Port)

object HttpConfig {
  implicit val hostConfigReader: ConfigReader[Host] =
    ConfigReader[String].emap(v =>
      Host.fromString(v).toRight(CannotConvert(v, "Host", "It's not a valid host"))
    )
  implicit val portConfigReader: ConfigReader[Port] = ConfigReader[Int].emap(v =>
    Port.fromInt(v).toRight(CannotConvert(v.toString, "Port", "It's not a valid host"))
  )
  implicit val configReader: ConfigReader[HttpConfig] = deriveReader[HttpConfig]
}
