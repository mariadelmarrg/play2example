package services

import java.sql.Connection
import play.api.db.DB
import anorm._
import model.Client

object ClientServiceImpl extends ClientService {

  val clientParser: RowParser[Client] = {
    import anorm.~
    import anorm.SqlParser._
    str("username") ~ str("name") ~ str("password") map {
      case username ~ name ~ password => Client(username, name, password)
    }
  }

  def findById(id: Long)(implicit conn: Connection): Option[Client] = {
    var sql = SQL("SELECT username, name, password FROM Client WHERE id={id}")
    sql.on('id -> id).singleOpt(clientParser)
  }

  def insert(client: Client)(implicit conn: Connection) = {
    var sql = SQL("INSERT INTO Client(username, name, password) VALUES ({username}, {name}, {password})")
    var result = sql.on('username -> client.username, 'name -> client.name, 'password -> client.password).executeInsert()
  }

  def canUseUsername(username: String)(implicit conn: Connection): Boolean = {
    var sql = SQL("SELECT COUNT(*) AS c FROM Client u WHERE u.username = {username}")
    val row = sql.on("username" -> username).apply().head
    if (row[Long]("c") == 1) false else true
  }

  def checkAuthenticationDetails(username: String, password: String)(implicit conn: Connection): Boolean = {
    var sql = SQL("SELECT COUNT(*) AS c FROM Client WHERE username = {username} AND password = {password}") 
    var row = sql.on("username" -> username, "password" -> password).apply().head;
    println(row[Long]("c"))
    if (row[Long]("c") == 1) true else false
  }

}