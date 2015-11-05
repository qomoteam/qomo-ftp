package cbb.qomo.ftp

import org.apache.ftpserver.{ConnectionConfigFactory, FtpServerFactory}
import org.apache.ftpserver.listener.ListenerFactory
import scalikejdbc.ConnectionPool

object Main extends App {

  val port = args(0).toInt
  val usersHome = args(1)
  val host = args(2)
  val dbname = args(3)
  val username = args(4)
  val password = args(5)

  Class.forName("org.postgresql.Driver")
  ConnectionPool.singleton(s"jdbc:postgresql://$host/$dbname", username, password)

  val serverFactory = new FtpServerFactory()
  serverFactory.setUserManager(new QUserManager())

  val factory = new ListenerFactory()
  factory.setPort(port)
  serverFactory.addListener("default", factory.createListener())

  val scf=new ConnectionConfigFactory ()
  scf.setAnonymousLoginEnabled(false)
  scf.setMaxLoginFailures(5)
  scf.setMaxLogins(9999)

  serverFactory.setConnectionConfig(scf.createConnectionConfig())

  val server = serverFactory.createServer()
  server.start()

}
