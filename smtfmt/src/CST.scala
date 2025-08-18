package smtfmt

abstract class CST

case class Block(contents: Seq[Element]) extends CST

abstract class Element extends CST

case object NewLine                 extends Element
case class Comment(comment: String) extends Element

abstract class Expr                          extends Element
case class Term(str: String)                 extends Expr
case class ParenExpr(contents: Seq[Element]) extends Expr
