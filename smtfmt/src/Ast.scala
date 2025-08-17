package smtfmt

case object NoNewLine
case object NewLine
case class Comment(comment: String)
type BlankLine = Comment | NewLine.type
type LineEnd   = Comment | NewLine.type | NoNewLine.type

case class Symbol(name: String, lineEnd: LineEnd)
case class ParenExpr(ops: Seq[Expr | BlankLine], leftLineEnd: LineEnd, rightLineEnd: LineEnd)
type Expr = ParenExpr | Symbol

case class SFile(contents: Seq[ParenExpr | BlankLine])
