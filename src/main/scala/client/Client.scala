package client

import com.gu.dfpapi.{LineItemSummary, Query, SessionBuilder}

object Client extends App {
  val session   = SessionBuilder.buildSessionFromFile()
  val query     = Query(where = Some("id != 0"), limit = 10)
  val lineItems = LineItemSummary.fetch(session, query)
  lineItems.foreach(println)
  println(lineItems.size)
}
