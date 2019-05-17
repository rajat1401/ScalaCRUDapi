import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.bson.{BsonReader, BsonWriter}
import org.joda.time.DateTime
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{MongoCollection, _}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}



class JodaCodec extends Codec[DateTime] {

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): DateTime = new DateTime(bsonReader.readDateTime())

  override def encode(bsonWriter: BsonWriter, t: DateTime, encoderContext: EncoderContext): Unit = bsonWriter.writeDateTime(t.getMillis)

  override def getEncoderClass: Class[DateTime] = classOf[DateTime]
}


object Main extends App{
  val host= "0.0.0.0"
  val port= 9000

  implicit val system: ActorSystem = ActorSystem(name= "userapi")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  import system.dispatcher


  val codecRegistry = fromRegistries(fromProviders(classOf[Users]), CodecRegistries.fromCodecs(new JodaCodec), DEFAULT_CODEC_REGISTRY )
  val mongoClient: MongoClient = MongoClient()
  val db: MongoDatabase = mongoClient.getDatabase("maybelike").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Users] = db.getCollection("segment")

  val todoRepository= new todorepository(collection)
  val router= new TodoRouter(todoRepository)
  val server= new Server(router, host, port)

  val binding= server.bind()
  binding.onComplete{
    case Success(_) => println("Success!")
    case Failure(error: Error) => println (s"Failed due to: ${error.getMessage}")
  }

  Await.result(binding, 3.seconds)
}

