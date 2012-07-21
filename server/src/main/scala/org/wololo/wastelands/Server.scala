package org.wololo.wastelands

import scala.actors._
import scala.actors.Actor._
import scala.actors.remote._
import scala.actors.remote.RemoteActor._

case object Connect
case object Event
case object Stop
case object Shutdown

object ServerApp extends App {
  override def main(args: Array[String]): Unit = {
    Server.start
  }
}

class Client(server: AbstractActor) extends Actor { 
  def act { loop { react {
	case Shutdown => System.out.println("Shutdown event"); exit
  }}}
}

object Server extends Actor {
  override def act() : Unit = {
    System.out.println("Server starting... " + Thread.currentThread().getId())
    
    alive(9000)
    register('wastelandsServer, self)
    
    var oc: OutputChannel[Any] = null;
    
    loop {
      react {
        case Connect => oc = sender; System.out.println("Connect event")
        case Stop => System.out.println("Exit event"); oc ! Shutdown; exit
      }
    }
    
    System.out.println("Server shutting down... " + Thread.currentThread().getId())
  }
}

