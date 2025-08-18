package smtfmt

import fastparse.*
import SingleLineWhitespace.*
import scala.runtime.SymbolLiteral

object SMT2Parser {
  /* line breaking character */
  def lbc(using p: P[?]): P[Unit] = P(StringIn("\n", "\r\n"))
  val lbcs                        = Seq('\n', '\r')

  // Match CST

  def element(using p: P[?]): P[Element] = P(comment | newLine | expr)

  def newLine(using p: P[?]): P[NewLine.type] = P(
    (lbc).map(x => NewLine)
  )
  def comment(using p: P[?]): P[Comment] = P(
    ";" ~~/ CharsWhile(!lbcs.contains(_)).!.map(Comment(_))
  )

  def term(using p: P[?]): P[Term] = P(
    CharsWhile(!" \r\t\n();".contains(_)).!.map(x => Term(x))
  )
  def parenExpr(using p: P[?]): P[ParenExpr] = P(
    ("(" ~/ (element).rep ~ ")").map(ParenExpr(_))
  )
  def expr(using p: P[?]): P[Expr] = P(parenExpr | term)

  def block(using p: P[?]): P[Block] = P(
    (element).rep.map(Block(_)) ~ End
  )
}
