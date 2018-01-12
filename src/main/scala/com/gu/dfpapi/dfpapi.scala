package com.gu

package object dfpapi {

  def toSeq[A](as: Array[A]): Seq[A] = Option(as) map (_.toSeq) getOrElse Nil
}
