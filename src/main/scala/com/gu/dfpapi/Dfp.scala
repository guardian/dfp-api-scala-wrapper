package com.gu.dfpapi

import com.google.api.ads.dfp.axis.factory.DfpServices
import com.google.api.ads.dfp.axis.utils.v201711.StatementBuilder
import com.google.api.ads.dfp.axis.utils.v201711.StatementBuilder.SUGGESTED_PAGE_LIMIT
import com.google.api.ads.dfp.axis.v201711.{LineItem, LineItemServiceInterface}
import com.google.api.ads.dfp.lib.client.DfpSession
import com.gu.dfpapi.PageFetcherInstances._

import scala.annotation.tailrec

object Dfp {

  private val services = new DfpServices()

  def fetchLineItems(session: DfpSession, query: Query): Seq[LineItemSummary] = {
    val service = services.get(session, classOf[LineItemServiceInterface])

    @tailrec
    def fetch(acc: Seq[LineItem], stmtBuilder: StatementBuilder): Seq[LineItem] = {
      val page  = PageFetcher.fetchPage(service, stmtBuilder)
      val soFar = acc ++ page.results
      if (soFar.size >= page.totalResultSetSize || soFar.size >= query.limit) {
        soFar
      } else {
        stmtBuilder.increaseOffsetBy(SUGGESTED_PAGE_LIMIT)
        fetch(soFar, stmtBuilder)
      }
    }

    val items = fetch(Nil, buildStatementBuilder(query))
    items.map { item =>
      LineItemSummary(
        id = item.getId,
        name = item.getName
      )
    }
  }

  private def buildStatementBuilder(query: Query): StatementBuilder = {
    val stmtBuilder = new StatementBuilder()
    query.where
      .fold(stmtBuilder)(conditions => stmtBuilder.where(conditions))
      .limit(query.limit)
  }

//  private def fetch[A, B](service: B, stmtBuilder: StatementBuilder): Seq[A] = {
//
//    @tailrec
//    def fetch(acc: Seq[A]): Seq[A] = {
//      val page = service.fetchPage(stmtBuilder)
//      //val page:PageFetchResult[A]  = fetchPage[A,B](service, stmtBuilder)
//      val soFar = acc ++ page.results
//      if (soFar.size >= page.totalResultSetSize) {
//        soFar
//      } else {
//        stmtBuilder.increaseOffsetBy(SUGGESTED_PAGE_LIMIT)
//        fetch(soFar)
//      }
//    }
//
//    stmtBuilder.limit(SUGGESTED_PAGE_LIMIT)
//    fetch(Nil)
//  }
}
