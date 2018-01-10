package com.gu.dfpapi

package object service {

  def toSeq[A](as: Array[A]): Seq[A] = Option(as) map (_.toSeq) getOrElse Nil
}
