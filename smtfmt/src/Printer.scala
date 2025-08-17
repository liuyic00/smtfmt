package smtfmt

import fastparse.*

case class Smt2Formatter(
    maxLineLength: Int = 80
) {
  // extension (Symbol: S)
  // extension (parenExpr: ParenExpr) def fmtString(indent: Int): String = "a"
  // extension (sfile: SFile) def fmtString: String                      = sfile.contents.map(_.fmtString).reduce(_ + _)

  def format(input: String): String = {
    val parseResult = parse(input, SMT2Parser.sfile(using _))
    val someResult = parseResult match {
      case Parsed.Success(value, _) =>
        println(value)
        Some(value)
      case f @ Parsed.Failure(label, index, extra) =>
        println(f)
        println(f.trace().longAggregateMsg)
        None
    }

    // someResult.map(_.fmtString).getOrElse(input)
    input
  }
}
