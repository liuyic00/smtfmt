package smtfmt

import fastparse.*

//trait Transformer {
//  def transComment(c: Comment): Comment = c
//  def transBlankLine(b: BlankLine): BlankLine = b match {
//    case c: Comment => transComment(c)
//    case NewLine    => NewLine
//  }
//  def transLineEnd(l: LineEnd): LineEnd = l match {
//    case c: Comment => transComment(c)
//    case NewLine    => NewLine
//    case NoNewLine  => NoNewLine
//  }
//
//  def transSymbol(s: Symbol): Symbol = {
//    Symbol(s.name, transLineEnd(s.lineEnd))
//  }
//  def transParenExpr(p: ParenExpr): ParenExpr = {
//    ParenExpr(
//      p.ops.map {
//        case e: Expr      => transExpr(e)
//        case b: BlankLine => transBlankLine(b)
//      },
//      transLineEnd(p.leftLineEnd),
//      transLineEnd(p.rightLineEnd)
//    )
//  }
//  def transExpr(e: Expr): Expr = e match {
//    case p: ParenExpr => transParenExpr(p)
//    case s: Symbol    => transSymbol(s)
//  }
//
//  def transBlock(b: Block): Block = {
//    Block(b.contents.map {
//      case p: ParenExpr => transParenExpr(p)
//      case b: BlankLine => transBlankLine(b)
//    })
//  }
//}

case class SmtFormatter() {
  def splitToLines(contents: Seq[Element], keepNewLine: Boolean = false): Seq[Seq[Element]] = {
    var lines = List.empty[Seq[Element]]
    var line  = List.empty[Element]
    contents.foreach {
      case NewLine =>
        if keepNewLine then line = NewLine :: line
        lines = line.reverse :: lines
        line = Nil
      case x =>
        line = x :: line
    }
    lines = line.reverse :: lines
    lines.reverse.toSeq
  }
  def wordPrinter(cst: CST): String = cst match {
    case Block(contents) => splitToLines(contents, true).map(_.map(wordPrinter).mkString(" ")).mkString
    case NewLine         => "[\\n]\n"
    case Comment(text)   => s"<;$text>"
    case Term(text)      => s"<$text>"
    case ParenExpr(contents) =>
      "( " + splitToLines(contents, true).map(_.map(wordPrinter).mkString(" ")).mkString + " )"
  }
  def printer(cst: CST): String = cst match
    case Block(contents) => splitToLines(contents).map(_.map(printer).mkString(" ")).mkString("\n")
    case NewLine         => "\n"
    case Comment(text)   => if text.startsWith(" ") then s";$text" else s"; $text"
    case Term(text)      => s"$text"
    case ParenExpr(contents) =>
      "(" + ({
        val lines = splitToLines(contents)
          .map(_.map(printer).mkString(" "))
        (lines.head.indent(1).strip().stripLineEnd +:
          lines.tail.map(line => line.indent(2).stripLineEnd)).mkString("\n")
      }) + ")"

  def format(input: String): String = {
    val parseResult = parse(input, SMT2Parser.block(using _))
    val someResult = parseResult match {
      case Parsed.Success(value, _) => Some(value)
      case f @ Parsed.Failure(label, index, extra) =>
        System.err.println(f)
        System.err.println(f.trace().longAggregateMsg)
        sys.error("Parsing failed")
        None
    }

    someResult.map(printer).getOrElse(input)
  }
}
