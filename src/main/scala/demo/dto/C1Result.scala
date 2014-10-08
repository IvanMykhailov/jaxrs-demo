package demo.dto

import scala.annotation.target.field
import javax.xml.bind.annotation._
import javax.xml.bind.annotation.adapters._
import javax.xml.bind._


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class C1Result(
  value: Double    
) {
  private def this() = this(0)
}