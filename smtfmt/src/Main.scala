package smtfmt

object SmtFmt extends App {
  val formatter = SmtFormatter()

  print(
    formatter.format(
      """(set-logic QF_BV)
        |
        |
        |; cocoment   bbb  
        |(; comment at end of line
        | declare-fun
        |  a
        |  ()
        |  (_   BitVec 32 )
        |)
        |(declare-fun a () (_ BitVec 32))
        |  (declare-fun b () (_ BitVec 32))
        |( ; comment at end of line
        |assert (= (bvadd a b) (_ bv0 32))
        |)
        |(( aaa
        |    bb
        |    cc
        | ) a )
        |(check-sat)
        |""".stripMargin
    )
  )
}
