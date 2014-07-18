package erector.util.text

/**
 * @author jda
 */
class NGramIterable(val value: IndexedSeq[String]) extends AnyVal {

  //def nGrams(n: Int, start: String = DefaultStartSymbol, stop: String = DefaultStopSymbol): Seq[IndexedSeq[String]] = {
  def nGrams(n: Int, start: String = DefaultStartSymbol): Seq[IndexedSeq[String]] = {
    for {
      //i <- 0 until value.length + n - 1
      i <- 0 until value.length
      slice = value.slice(i - n + 1, i + 1)
      lPadded =
        if (i < n)
          IndexedSeq.fill(n - i - 1)(start) ++ slice
        else
          slice
      //rPadded =
      //  if (i >= value.length)
      //    lPadded ++ IndexedSeq.fill(i - value.length + 1)(stop)
      //  else
      //    lPadded
    } yield lPadded //yield rPadded
  }

}
