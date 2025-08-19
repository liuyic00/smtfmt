package smtfmt

object SmtFmt extends App {
  val formatter = SmtFormatter()
  val input     = scala.io.Source.stdin.getLines().mkString("\n")
  println(formatter.format(input))
}
