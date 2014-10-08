package demo.dto

import scala.annotation.target.field
import javax.xml.bind.annotation._
import javax.xml.bind.annotation.adapters._
import javax.xml.bind._


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class C2Params(
  v2: Double,
  v3: Int,
  v4: Int
) {
  private def this() = this(0, 0, 0)
}