package com.gu.dfpapi.model

case class StatementBuilder(
    select: Option[String] = None,
    from: Option[String] = None,
    where: Option[String] = None,
    bindVariableValues: Map[String, Any] = Map.empty,
    limit: Int = 100
)
