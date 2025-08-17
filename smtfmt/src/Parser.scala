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

  // Match AST

  def noNewLine(using p: P[?]): P[NoNewLine.type] = P(
    (!(comment | newLine)).map(_ => NoNewLine)
  )
  def newLine(using p: P[?]): P[NewLine.type] = P(
    (CharsWhileIn(" \r\t") ~ &(lbc) | (&(lbc) ~ !End)).map(x => NewLine)
  )
  def comment(using p: P[?]): P[Comment] = P(
    ";" ~~/ CharsWhile(!lbcs.contains(_)).!.map(Comment(_))
  )
  def blankLine(using p: P[?]): P[BlankLine] = P(newLine | comment)
  def lineEnd(using p: P[?]): P[LineEnd]     = P(comment | newLine | noNewLine)

  def symbol(using p: P[?]): P[Symbol] = P(
    (rawSymbol ~ lineEnd).map(x => Symbol(x._1, x._2))
  )
  def parenExpr(using p: P[?]): P[ParenExpr] = P(
    ("(" ~/ lineEnd ~ (expr | blankLine).rep ~ ")" ~ lineEnd)
      .map(x => ParenExpr(x._2, x._1, x._3))
  )
  def expr(using p: P[?]): P[Expr] = P(parenExpr | symbol)

  def block(using p: P[?]): P[Block] = P(
    (parenExpr | blankLine).rep(sep = lbc).map(Block(_)) ~ lbc.? ~ End
  )
}
