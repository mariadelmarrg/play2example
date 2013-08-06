package services

import model.Client
import java.sql.Connection

trait ClientService {

  def findById(id:Long)(implicit conn: Connection) : Option[Client]
  
  def insert(client: Client)(implicit conn: Connection)
  
  def canUseUsername(username: String)(implicit conn: Connection): Boolean
  
  def checkAuthenticationDetails(username: String, password: String)(implicit conn: Connection): Boolean
  
}