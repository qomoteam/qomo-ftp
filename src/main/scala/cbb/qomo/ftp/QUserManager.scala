package cbb.qomo.ftp

import org.apache.ftpserver.ftplet._
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication
import org.mindrot.jbcrypt.BCrypt
import scalikejdbc._

class QUserManager extends UserManager {

  val userNames = List[String]()

  override def isAdmin(username: String): Boolean = false

  override def getAdminName: String = "__admin__"

  override def doesExist(username: String): Boolean = {
    implicit val session = AutoSession
    sql"""
        SELECT count(*) as c FROM users WHERE username=${username}
      """.map(rs => rs.long("c")).single().apply().get == 1
  }

  override def authenticate(authentication: Authentication): User = {
    authentication match {
      case upauth: UsernamePasswordAuthentication =>
        val username: String = upauth.getUsername
        var password: String = upauth.getPassword

        if (username == null) {
          throw new AuthenticationFailedException("Authentication failed")
        }

        if (password == null) {
          password = ""
        }

        val user = getUserByName(username)
        if (checkpw(password, user.getPassword)) {
          userNames ++ username
          return user
        }
        else {
          throw new AuthenticationFailedException("Authentication failed")
        }
      case _ =>
        throw new AuthenticationFailedException("Authentication failed")
    }
  }

  override def delete(username: String): Unit = ???

  override def getAllUserNames: Array[String] = {
    userNames.toArray
  }

  override def save(user: User): Unit = ???

  override def getUserByName(username: String): QUser = {
    implicit val session = AutoSession
    sql"""
        SELECT id, username, encrypted_password as password FROM users WHERE username=${username}
      """.map(rs => {
      QUser(rs.string("id"), rs.string("username"), rs.string("password"), Main.usersHome)
    }).single().apply().get
  }

  def checkpw(plainpasswd: String, encpassword: String): Boolean = {
    BCrypt.checkpw(plainpasswd, encpassword)
  }

}
