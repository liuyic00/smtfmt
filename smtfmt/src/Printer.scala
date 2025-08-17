package smtfmt

import fastparse.*

case class Smt2Formatter(
    maxLineLength: Int = 80
) {
  def format(input: String): String = {
    val parseResult = parse(input, SMT2Parser.file(using _))
    parseResult match {
      case Parsed.Success(value, _) => value.foreach(println)
      case f @ Parsed.Failure(label, index, extra) =>
        println(f)
        println(f.trace().longAggregateMsg)
    }
    input
  }
}
