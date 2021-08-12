package com.softwaremill.bootzooka.metrics

import com.softwaremill.bootzooka.http.{Error_OUT, Http}
import com.softwaremill.bootzooka.infrastructure.Json._
import com.softwaremill.bootzooka.version.BuildInfo
import monix.eval.Task
import sttp.model.StatusCode
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.generic.auto._

/** Defines an endpoint which exposes the current application version information.
  */
class VersionApi(http: Http) {
  import VersionApi._
  import http._

  val versionEndpoint: ServerEndpoint[Unit, (StatusCode, Error_OUT), Version_OUT, Any, Task] = baseEndpoint.get
    .in("version")
    .out(jsonBody[Version_OUT])
    .serverLogic { _ =>
      Task.now(Version_OUT(BuildInfo.lastCommitHash)).toOut
    }
}

object VersionApi {
  case class Version_OUT(buildSha: String)
}
