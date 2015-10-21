package scalazproblem

// System
import scala.collection.mutable

// Third Party
import com.typesafe.scalalogging.LazyLogging
import scalaz.{\/, -\/, \/-}
import scalaz.stream.{Process, wye}
import scalaz.concurrent.Task
import rx.lang.scala.Observable
import com.rabbitmq.client.{Connection, ConnectionFactory, Channel, QueueingConsumer}

case class RabbitMQProvider(connection: Connection, channel: Channel, channelName: String)

object Main extends LazyLogging {
  val exchangeName = "exchange"
  val queueName = "queue"

  def main(args: Array[String]) : Unit = {
    val provider = createProvider()
    val consumer = new QueueingConsumer(provider.channel)
    provider.channel.basicConsume(queueName, true, consumer)

    receive(consumer).subscribe(
    {message => {  // onNext
      logger.info(s"Received message ${message.get}")
    }},
    {null}, // onError
    {() =>  // onCompleted
        cleanup(provider, consumer)
    })

    stringsToSend(provider).run.runAsync {
      case -\/(error) => {
        logger.error(s"Oops ${error}")
      }
      case \/-(info) => { }
    }
  }

  def receive(consumer: QueueingConsumer) : Observable[Option[String]] = {
    Observable(
      subscriber => {
        new Thread(new Runnable() {
          def run() : Unit = {
            if (subscriber.isUnsubscribed) {
              return
            }
            var count = 0
            while(count < 4) {
              val message = consumer.nextDelivery().getBody()
              val action = new String(message, "UTF-8")
              subscriber.onNext(Some(action))
              count += 1
            }
            //subscriber.onNext(None)
            if (!subscriber.isUnsubscribed) {
              subscriber.onCompleted()
            }
          }
        }).start()
      }
    )
  }

  // scalaz-stream
  def stringsToSend(publisher: RabbitMQProvider): Process[Task, Unit] = Process("Message1", "Message2", "Message3", "Message4") flatMap { message =>
    Process eval Task {
      Thread.sleep(1000)
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

  def createProvider() : RabbitMQProvider = { // Create the RabbitMQ Publisher
    val factory : ConnectionFactory = new ConnectionFactory();
    factory.setHost("localhost")
    val connection : Connection = factory.newConnection()
    val channel : Channel = connection.createChannel()
    channel.queueDeclare(queueName, false, false, true, null)
    RabbitMQProvider(connection, channel, queueName)
  }
}
