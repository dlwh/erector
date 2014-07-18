package erector.util

/**
 * @author jda
 */

package object text {

  final val DefaultStartSymbol = "<s>"
  final val DefaultStopSymbol = "</s>"

  implicit def toNGramIterable(xs: IndexedSeq[String]) = new NGramIterable(xs)

}
