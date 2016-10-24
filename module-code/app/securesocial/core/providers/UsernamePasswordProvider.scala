/**
 * Copyright 2012-2014 Jorge Aliss (jaliss at gmail dot com) - twitter: @jaliss
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package securesocial.plugin.providers

import org.joda.time.DateTime
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import securesocial.controllers.ViewTemplates
import securesocial.core._
import securesocial.plugin.AuthenticationResult.{ Authenticated, NavigationFlow }
import securesocial.plugin._
import securesocial.plugin.providers.utils.PasswordHasher
import securesocial.plugin.services.{ AvatarService, UserService }

import scala.concurrent.{ ExecutionContext, Future }

/**
 * A username password provider
 */
class UsernamePasswordProvider[U](userService: UserService[U],
  avatarService: Option[AvatarService],
  viewTemplates: ViewTemplates,
  passwordHashers: Map[String, PasswordHasher])(implicit val executionContext: ExecutionContext)
    extends IdentityProvider with ApiSupport with Controller {

  override val id = UsernamePasswordProvider.UsernamePassword

  def authMethod = AuthenticationMethod.UserPassword

  val InvalidCredentials = "securesocial.login.invalidCredentials"

  def authenticateForApi(implicit request: Request[AnyContent]): Future[AuthenticationResult] = {
    doAuthentication(apiMode = true)
  }

  def authenticate()(implicit request: Request[AnyContent]): Future[AuthenticationResult] = {
    doAuthentication()
  }

  private def profileForCredentials(userId: String, password: String): Future[Option[BasicProfile]] = {
    userService.find(id, userId).map { maybeUser =>
      for (
        user <- maybeUser;
        pinfo <- user.passwordInfo;
        hasher <- passwordHashers.get(pinfo.hasher) if hasher.matches(pinfo, password)
      ) yield {
        user
      }
    }
  }

  protected def authenticationFailedResult[A](apiMode: Boolean)(implicit request: Request[A]) = Future.successful {
    if (apiMode)
      AuthenticationResult.Failed("Invalid credentials")
    else
      NavigationFlow(badRequest(UsernamePasswordProvider.loginForm, Some(InvalidCredentials)))
  }

  protected def withUpdatedAvatar(profile: BasicProfile): Future[BasicProfile] = {
    (avatarService, profile.email) match {
      case (Some(service), Some(e)) => service.urlFor(e).map {
        case url if url != profile.avatarUrl => profile.copy(avatarUrl = url)
        case _ => profile
      }
      case _ => Future.successful(profile)
    }
  }

  private def doAuthentication[A](apiMode: Boolean = false)(implicit request: Request[A]): Future[AuthenticationResult] = {
    val form = UsernamePasswordProvider.loginForm.bindFromRequest()
    form.fold(
      errors => Future.successful {
        if (apiMode)
          AuthenticationResult.Failed("Invalid credentials")
        else
          AuthenticationResult.NavigationFlow(badRequest(errors)(request))
      },
      credentials => {
        val userId = credentials._1.toLowerCase
        val password = credentials._2

        profileForCredentials(userId, password).flatMap {
          case Some(profile) => withUpdatedAvatar(profile).map(Authenticated)
          case None => authenticationFailedResult(apiMode)
        }
      })
  }

  private def badRequest[A](f: Form[(String, String)], msg: Option[String] = None)(implicit request: Request[A]): Result = {
    Results.BadRequest(viewTemplates.getLoginPage(f, msg))
  }
}

object UsernamePasswordProvider {
  val UsernamePassword = "userpass"
  private val Key = "securesocial.userpass.withUserNameSupport"
  private val SendWelcomeEmailKey = "securesocial.userpass.sendWelcomeEmail"
  private val Hasher = "securesocial.userpass.hasher"
  private val EnableTokenJob = "securesocial.userpass.enableTokenJob"
  private val SignupSkipLogin = "securesocial.userpass.signupSkipLogin"

  val loginForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )

  lazy val withUserNameSupport = current.configuration.getBoolean(Key).getOrElse(false)
  lazy val sendWelcomeEmail = current.configuration.getBoolean(SendWelcomeEmailKey).getOrElse(true)
  lazy val hasher = current.configuration.getString(Hasher).getOrElse(PasswordHasher.id)
  lazy val enableTokenJob = current.configuration.getBoolean(EnableTokenJob).getOrElse(true)
  lazy val signupSkipLogin = current.configuration.getBoolean(SignupSkipLogin).getOrElse(false)
}
