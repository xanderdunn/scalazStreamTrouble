package scalazproblem
/** An app that drives domains (i.e. pong), imitation learners, reinforcement learners, and neural nets and facilitates communication between them. */

// System
import scala.collection.mutable

// Third Party
import com.typesafe.scalalogging.LazyLogging
import scalaz.{\/, -\/, \/-}
import scalaz.stream.{Process, wye}
import scalaz.concurrent.Task
import com.rabbitmq.client.{Connection, ConnectionFactory, Channel, QueueingConsumer}

case class RabbitMQProvider(connection: Connection, channel: Channel, channelName: String)

object Main extends LazyLogging {
  val exchangeName = "exchange"
  val queueName = "queue"

  def main(args: Array[String]) : Unit = {
    val provider = createProvider()
    val consumer = new QueueingConsumer(provider.channel)
    provider.channel.basicConsume(queueName, true, consumer)

    (receivedStrings(consumer) wye stringsToSend(provider))(wye.mergeHaltBoth).run.runAsync {
      case -\/(error) => {
        logger.error(s"Oops ${error}")
      }
      case \/-(info) => { cleanup(provider, consumer) }
    }
  }

  // scalaz-stream
  def receivedStrings(consumer: QueueingConsumer): Process[Task, Unit] = Process.repeatEval(receive(consumer)) flatMap { message =>
    Process eval Task {
      logger.info(s"Received message ${message}")
    }
  }
  def stringsToSend(publisher: RabbitMQProvider): Process[Task, Unit] = Process("Message1", "Message2", "Message3", "Message4") flatMap { message =>
    Process eval Task {
      Thread.sleep(50)
      logger.info(s"Sending ${message}")
      send(message, publisher)
    }
  }

  def cleanup(provider: RabbitMQProvider, consumer: QueueingConsumer): Unit = {
    logger.info("Cleaning up and ending.")
    provider.channel.basicCancel(consumer.getConsumerTag())
    provider.channel.close()
    provider.connection.close()
  }

  // RabbitMQ Boilerplate
  def send(message: String, publisherProvider: RabbitMQProvider) : Unit = {
    publisherProvider.channel.basicPublish("", queueName, null, message.getBytes())
  }

  def receive(consumer: QueueingConsumer) : Task[String] = {
    Task {
      logger.info("Receiving a stub")
      //val message = consumer.nextDelivery().getBody()
      //new String(message, "UTF-8")
      "Stub"
    }
  }

  def createProvider() : RabbitMQProvider = { // Create the RabbitMQ Publisher
    val factory : ConnectionFactory = new ConnectionFactory();
    factory.setHost("localhost")
    val connection : Connection = factory.newConnection()
    val channel : Channel = connection.createChannel()
    channel.queueDeclare(queueName, false, false, true, null)
    RabbitMQProvider(connection, channel, queueName)
  }
}
