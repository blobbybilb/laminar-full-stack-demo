import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object JsApp {
  def main(args: Array[String]): Unit = {
    println("-- Scala.js app start --")

    lazy val container = dom.document.getElementById("root")

    lazy val x = {
      div("hi")
    }

    render(container, x)
  }
}
