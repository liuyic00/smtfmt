package smtfmt

import fastparse.*
import SingleLineWhitespace.*
import scala.runtime.SymbolLiteral

object SMT2Parser {
  // From SMT-LIB Standard

  def simple_symbol(using p: P[?]): P[String] = P(
    CharsWhileIn("0-9a-zA-Z+\\-/*=%?!.$_~&^<>@", 1).!
      // and not keywords
  )

  /* line breaking character */
  def lbc(using p: P[?]): P[Unit] = P(StringIn("\n", "\r\n"))
  val lbcs                        = Seq('\n', '\r')

  // For formatter

  def rawSymbol(using p: P[?]): P[String] = P(
    simple_symbol
  )

  def symbol(using p: P[?]): P[SSymbol] = P(
    (rawSymbol ~ lineEnd).map(x => SSymbol(x._1, x._2))
  )

  def expr(using p: P[?]): P[SExpr] = P(parenExpr | symbol)

  def parenExpr(using p: P[?]): P[ParenExpr] = P(
    ("(" ~/ lineEnd ~ (expr | blank).rep ~ ")" ~ lineEnd).map(x => ParenExpr(x._2, x._1, x._3))
  )

  def newLine(using p: P[?]): P[SNewLine.type] = P(
    (CharsWhileIn(" \r\t") ~ &(lbc) | (&(lbc) ~ !End)).map(x => SNewLine)
  )
  def comment(using p: P[?]): P[SComment] = P(
    ";" ~~/ CharsWhile(!lbcs.contains(_)).!.map(SComment(_))
  )

  def blank(using p: P[?]): P[SBlank] = P(newLine | comment)
  def lineEnd(using p: P[?]): P[SLineEnd] = P(
    ((comment | newLine).?.map(_.getOrElse(SNone)))
  )

  def file(using p: P[?]): P[Seq[ParenExpr | SBlank]] = P(
    (blank | parenExpr).rep(sep = lbc) ~ lbc.? ~ End
  )
}
