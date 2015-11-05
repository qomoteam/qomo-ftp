package cbb.qomo.ftp

import java.util

import org.apache.ftpserver.ftplet.{Authority, AuthorizationRequest}
import org.apache.ftpserver.usermanager.impl.{ConcurrentLoginPermission, WritePermission}
import scala.collection.JavaConversions._

case class QUser(id: String, username: String, password: String, usersHome: String) extends org.apache.ftpserver.ftplet.User {
  private val authorities: util.List[Authority] = new util.ArrayList[Authority]

  authorities.add(new WritePermission())

  authorities.add(new ConcurrentLoginPermission(6, 2))

  override def getHomeDirectory: String = s"$usersHome/$id"

  override def getName: String = username

  override def authorize(request: AuthorizationRequest): AuthorizationRequest = {
    // check for no authorities at all
    if (authorities == null) {
      return null
    }

    var someoneCouldAuthorize: Boolean = false

    var r: AuthorizationRequest = null
    for (authority <- authorities) {
      if (authority.canAuthorize(request)) {
        someoneCouldAuthorize = true
        r = authority.authorize(request)
        if (r == null) {
          return null
        }
      }
    }

    if (someoneCouldAuthorize) {
      return r
    }
    else {
      return null
    }
  }

  override def getEnabled: Boolean = true

  override def getPassword: String = password

  override def getAuthorities: util.List[Authority] = authorities

  override def getAuthorities(clazz: Class[_ <: Authority]): util.List[Authority] = {
    val selected: util.List[Authority] = new util.ArrayList[Authority]
    import scala.collection.JavaConversions._
    for (authority <- authorities) {
      if (authority.getClass == clazz) {
        selected.add(authority)
      }
    }

    return selected
  }

  override def getMaxIdleTime: Int = 3600

}
