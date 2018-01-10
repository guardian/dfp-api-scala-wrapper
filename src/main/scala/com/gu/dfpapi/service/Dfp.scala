package com.gu.dfpapi.service

import com.google.api.ads.common.lib.auth.OfflineCredentials
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api.DFP
import com.google.api.ads.dfp.axis.factory.DfpServices
import com.google.api.ads.dfp.axis.utils.v201705.StatementBuilder
import com.google.api.ads.dfp.axis.utils.v201705.StatementBuilder.SUGGESTED_PAGE_LIMIT
import com.google.api.ads.dfp.axis.v201705.{LineItem, LineItemServiceInterface}
import com.google.api.ads.dfp.lib.client.DfpSession
import com.gu.dfpapi.model.LineItemSummary
import org.apache.commons.configuration.Configuration

import scala.annotation.tailrec

import com.gu.dfpapi.model.{StatementBuilder => GuStatementBuilder}

object Dfp {

  private val services = new DfpServices()

  def fetchLineItems(session: DfpSession,
                     statementBuilder: GuStatementBuilder): Seq[LineItemSummary] = {
    val service = services.get(session, classOf[LineItemServiceInterface])

    @tailrec
    def fetch(acc: Seq[LineItem], stmtBuilder: StatementBuilder): Seq[LineItem] = {
      val page  = service.getLineItemsByStatement(stmtBuilder.toStatement)
      val soFar = acc ++ toSeq(page.getResults)
      if (soFar.size >= page.getTotalResultSetSize || soFar.size >= statementBuilder.limit) {
        soFar
      } else {
        stmtBuilder.increaseOffsetBy(SUGGESTED_PAGE_LIMIT)
        fetch(soFar, stmtBuilder)
      }
    }

    val items = fetch(Nil, buildStatementBuilder(statementBuilder))
    items.map { item =>
      LineItemSummary(
        id = item.getId,
        name = item.getName
      )
    }
  }

  private def buildStatementBuilder(guStmtBuilder: GuStatementBuilder): StatementBuilder = {
    val stmtBuilder = new StatementBuilder()
    guStmtBuilder.where
      .fold(stmtBuilder)(conditions => stmtBuilder.where(conditions))
      .limit(guStmtBuilder.limit)
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

  def createSessionFromFile(): DfpSession = {
    val credential = new OfflineCredentials.Builder()
      .forApi(DFP)
      .fromFile()
      .build()
      .generateCredential()
    new DfpSession.Builder()
      .withOAuth2Credential(credential)
      .fromFile()
      .build()
  }

  def createSessionFromConfig(config: Configuration): DfpSession = {
    val credential = new OfflineCredentials.Builder()
      .forApi(DFP)
      .from(config)
      .build()
      .generateCredential()
    new DfpSession.Builder()
      .withOAuth2Credential(credential)
      .from(config)
      .build()
  }
}
