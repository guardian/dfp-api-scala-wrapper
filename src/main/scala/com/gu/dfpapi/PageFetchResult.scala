package com.gu.dfpapi

case class PageFetchResult[A](results: Seq[A], totalResultSetSize: Int)
