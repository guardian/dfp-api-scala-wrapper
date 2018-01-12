package com.gu.dfpapi

import com.google.api.ads.dfp.axis.factory.DfpServices
import com.google.api.ads.dfp.axis.utils.v201711.StatementBuilder
import com.google.api.ads.dfp.axis.utils.v201711.StatementBuilder.SUGGESTED_PAGE_LIMIT
import com.google.api.ads.dfp.axis.v201711.{
  LineItem,
  LineItemServiceInterface,
  Order,
  OrderServiceInterface
}
import com.google.api.ads.dfp.lib.client.DfpSession
import com.gu.dfpapi.PageFetcherInstances._

import scala.annotation.tailrec

object Fetcher {

  private val services = new DfpServices()

  private def fetchLineItems(session: DfpSession,
                             service: LineItemServiceInterface,
                             query: Query): Seq[LineItem] = {

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

    fetch(Nil, buildStatementBuilder(query))
  }

  private def fetchOrders[A](session: DfpSession,
                             service: OrderServiceInterface,
                             query: Query): Seq[A] = {

//    @tailrec
//    def fetch(acc: Seq[Order], stmtBuilder: StatementBuilder): Seq[Order] = {
//      val page  = PageFetcher.fetchPage(service, stmtBuilder)
//      val soFar = acc ++ page.results
//      if (soFar.size >= page.totalResultSetSize || soFar.size >= query.limit) {
//        soFar
//      } else {
//        stmtBuilder.increaseOffsetBy(SUGGESTED_PAGE_LIMIT)
//        fetch(soFar, stmtBuilder)
//      }
//    }

    @tailrec
    def fetch(acc: Seq[A],
              stmtBuilder: StatementBuilder,
              f: StatementBuilder => PageFetchResult[A]): Seq[A] = {
      val page  = f(stmtBuilder)
      val soFar = acc ++ page.results
      if (soFar.size >= page.totalResultSetSize || soFar.size >= query.limit) {
        soFar
      } else {
        stmtBuilder.increaseOffsetBy(SUGGESTED_PAGE_LIMIT)
        fetch(soFar, stmtBuilder, f)
      }
    }

    fetch(Nil, buildStatementBuilder(query), { sb =>
      PageFetcher.fetchPage(service, sb)
    })
  }

  def fetchLineItemSummaries(session: DfpSession, query: Query): Seq[LineItemSummary] = {
    val service = services.get(session, classOf[LineItemServiceInterface])
    fetchLineItems(session, service, query) map { item =>
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
