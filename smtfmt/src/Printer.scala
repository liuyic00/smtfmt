package smtfmt

import fastparse.*

case class SmtFormatter() {
  def format(input: String): String = {
    val parseResult = parse(input, SMT2Parser.block(using _))
    val someResult = parseResult match {
      case Parsed.Success(value, _) =>
        println(value)
        Some(value)
      case f @ Parsed.Failure(label, index, extra) =>
        println(f)
        println(f.trace().longAggregateMsg)
        None
    }

    input
  }
}
