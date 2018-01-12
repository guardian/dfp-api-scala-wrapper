package com.gu.dfpapi

def getService()
}

object ServiceFinder

sealed trait ServiceFinder[A] {
  def findService: PageFetchResult[A]
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
