package org.wololo.wastelands.test

import org.junit.Test

import scala.actors._
import scala.actors.Actor._
import scala.actors.remote._
import scala.actors.remote.RemoteActor._

import org.wololo.wastelands._

class BasicTests {
	@Test def connect() {
		val server = select(Node("localhost", 9000), 'wastelandsServer)

		val client = new Client(server)
		client.start
		
		Thread.sleep(1000)
	}
}