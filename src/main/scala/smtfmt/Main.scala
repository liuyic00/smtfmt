package smtfmt

object SmtFmt {
  def main(args: Array[String]): Unit = {
    val formatter = SmtFormatter()
    val input     = scala.io.Source.stdin.getLines().mkString("\n")
    println(formatter.format(input))
  }
}
