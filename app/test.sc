
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.HttpClient
import com.sksamuel.elastic4s.http.search.SearchResponse
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy

import com.sksamuel.elastic4s.http.ElasticDsl._

val client = HttpClient(ElasticsearchClientUri("localhost", 9200))

client.execute {
      indexInto("myindex" / "mytype").fields( "name" -> "fefe").withId("i")
  .refresh(RefreshPolicy.WAIT_UNTIL)
}.await

client.execute(delete())

val result: SearchResponse = client.execute {
  search("myindex")
}.await



// prints out the original json
println(result.hits.hits.head.sourceAsString)

client.close()