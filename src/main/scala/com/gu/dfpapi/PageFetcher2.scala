package com.gu.dfpapi

import com.google.api.ads.dfp.axis.utils.v201711.StatementBuilder
import com.google.api.ads.dfp.axis.v201711._

object ServiceFinder[A] {
  def getService()
}

sealed trait PageFetcher2[A] {
  def fetchPage(stmtBuilder: StatementBuilder): PageFetchResult[A]
}

object PageFetcher2Instances {

  implicit val lineItemPageFetcher2: PageFetcher2[LineItem] =
    new PageFetcher2[LineItem] {
      def fetchPage(stmtBuilder: StatementBuilder): PageFetchResult[LineItem] = {
        val page = service.getLineItemsByStatement(stmtBuilder.toStatement)
        PageFetchResult(toSeq(page.getResults), page.getTotalResultSetSize)
      }
    }

  implicit val orderPageFetcher: PageFetcher[Order, OrderServiceInterface] =
    new PageFetcher[Order, OrderServiceInterface] {
      def fetchPage(service: OrderServiceInterface,
                    stmtBuilder: StatementBuilder): PageFetchResult[Order] = {
        val page = service.getOrdersByStatement(stmtBuilder.toStatement)
        PageFetchResult(toSeq(page.getResults), page.getTotalResultSetSize)
      }
    }
}

object PageFetcher {
  def fetchPage[A, S](service: S, stmtBuilder: StatementBuilder)(
      implicit fetcher: PageFetcher[A, S]): PageFetchResult[A] =
    fetcher.fetchPage(service, stmtBuilder)
}
