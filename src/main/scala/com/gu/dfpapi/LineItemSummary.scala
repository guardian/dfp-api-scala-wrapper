package com.gu.dfpapi

import com.google.api.ads.dfp.lib.client.DfpSession

case class LineItemSummary(id: Long, name: String)

object LineItemSummary {
  def fetch(session: DfpSession, query: Query): Seq[LineItemSummary] = {
    Fetcher.fetchLineItemSummaries(session, query)
  }
}
