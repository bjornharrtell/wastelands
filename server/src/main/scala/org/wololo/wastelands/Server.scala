package org.wololo.wastelands

import scala.collection.mutable.ArrayBuffer

import scala.actors._
import scala.actors.Actor._
import scala.actors.remote._
import scala.actors.remote.RemoteActor._

object Server extends App with Actor {
  var clients:ArrayBuffer[OutputChannel[Any]] = null
  
  override def main(args: Array[String]): Unit = {
    clients = ArrayBuffer[OutputChannel[Any]]()
    
    println("Server starting...")
    start
  }

  def connect(client: OutputChannel[Any]) {
    clients += client
    println("Clients connected: " + clients.length)
  }
  
  def disconnect(client: OutputChannel[Any]) {
    clients -= client
    println("Clients connected: " + clients.length)
  }
  
  def shutdown() {
    clients.foreach(_ ! Shutdown)
    exit
  }
  
  def handleEvent(e: Event) {
    println(e)
    e match {
      case Connect => connect(sender)
      case Disconnect => disconnect(sender)
      case Shutdown => shutdown()
    }
  }

  def act(): Unit = {
    println("Server listening to port 9000...")
    alive(9000)
    register('server, self)
    
    println("Server ready and awaiting connections....")
    loop {
      react {
        case e: Event => handleEvent(e)
      }
    }
  }
}

