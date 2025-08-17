package smtfmt

case object SNone
case object SNewLine
case class SComment(comment: String)
type SBlank   = SComment | SNewLine.type
type SLineEnd = SComment | SNewLine.type | SNone.type

case class SSymbol(name: String, lineEnd: SLineEnd)
case class ParenExpr(ops: Seq[SExpr | SBlank], leftLineEnd: SLineEnd, rightLineEnd: SLineEnd)
type SExpr = ParenExpr | SSymbol

case class SFile(contents: Seq[ParenExpr | SBlank])
