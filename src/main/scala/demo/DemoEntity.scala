package demo

import scala.annotation.target.field
import javax.xml.bind.annotation._
import javax.xml.bind.annotation.adapters._
import javax.xml.bind._


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class DemoEntity(
  name: String,
  value: Int
) {
  private def this() = this("", 0)
}