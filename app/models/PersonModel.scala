package models

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
import org.joda.time.DateTime
import play.api.libs.concurrent._
import play.api.libs.concurrent.Execution.Implicits._
import reactivemongo.api._
import reactivemongo.bson._
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.{ElasticDsl, HttpClient}
import com.sksamuel.elastic4s.http.search.SearchResponse
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy
import com.sksamuel.elastic4s.http.ElasticDsl._



case class Person (
    _id: BSONObjectID,
    name: String
) {
  def getCCParams = this.getClass.getDeclaredFields.map( _.getName ) // all field names
    .zip( this.productIterator.to ).toMap // zipped with all values
}

object PersonModel {

    // Use Reader to deserialize document automatically

	
	implicit object PersonBSONReader extends BSONDocumentReader[Person] {
		def read(doc: BSONDocument): Person = {
			Person(
				doc.getAs[BSONObjectID]("_id").get,
				doc.getAs[String]("name").get
			)
		}
	}


	implicit object PersonBSONWriter extends BSONDocumentWriter[Person] {
		def write(person: Person): BSONDocument = {
			BSONDocument(
				"_id" -> person._id,
				"name" -> person.name
			)
		}
	}
    
	// Call MongoDriver
	val driver = new MongoDriver
	val connection = driver.connection(List("localhost"))
	val db = Await.result(connection.database("reactivemongo"), Duration(5000, "millis"))
	val collection = db.collection("persons")

  //ES
  val client = HttpClient(ElasticsearchClientUri("localhost", 9200))


  // Insert new document using non blocking
	def insert(p_doc:Person)= {

    collection.insert(p_doc).onComplete {
	  	case Failure(e) => throw e
	  	case Success(lastError) => {
	  		println("Mongocli: successfully inserted to with lastError = " + lastError)
	  	}
	  }

    client.execute {
      indexInto("myindex" / "mytype").fields(p_doc.getCCParams - "_id").withId(p_doc._id.stringify)
        .refresh(RefreshPolicy.WAIT_UNTIL)
    }.onComplete{
      case Failure(e) => throw e
      case Success(lastError) => {
        println("EScli: successfully inserted document with lastError = " + lastError)
      }
    }

	}
	
	// Update document using blocking
	def update(p_query:BSONDocument,p_modifier:Person) = {

	  collection.update(p_query, p_modifier)

    client.execute {
      indexInto("myindex" / "mytype").fields(p_modifier.getCCParams - "_id").withId(p_modifier._id.stringify)
        .refresh(RefreshPolicy.WAIT_UNTIL)
    }.onComplete{
      case Failure(e) => throw e
      case Success(lastError) => {
        println("EScli: successfully inserted document with lastError = " + lastError)
      }
    }
	}
	
	// Optional - Soft deletion by setting deletion flag in document
	def remove(p_query:BSONDocument) = {}
	
	// Delete document using blocking
	def removePermanently(p_query:BSONDocument) = {
	  collection.remove(p_query)
    println("__   ", p_query.get("_id").get.asInstanceOf[BSONObjectID].stringify)
    client.execute(ElasticDsl.delete(p_query.get("_id").get.asInstanceOf[BSONObjectID].stringify) from "myindex" / "mytype")
	}
	
	// Find all documents using blocking
	def find(p_query:BSONDocument) = {
	  collection.find(p_query).cursor[Person]().collect[List]()
	}
	
	// Find one document using blocking
	// Return the first found document
	def findOne(p_query:BSONDocument) = {
	  collection.find(p_query).one[Person]
	}
	
	// Optional - Find all document with filter
	def find(p_query:BSONDocument,p_filter:BSONDocument) = {}
	
	// Optional - Find all document with filter and sorting
	def find(p_query:BSONDocument,p_filter:BSONDocument,p_sort:BSONDocument) = {}
	
}