package com.gu.dfpapi

case class Query(
    select: Option[String] = None,
    from: Option[String] = None,
    where: Option[String] = None,
    bindVariableValues: Map[String, Any] = Map.empty,
    limit: Int = 100
)
