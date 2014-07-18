package erector.corpus

import scala.io.Source
import erector.util.text._
import erector.learning.Feat

//import scala.collection.mutable
import breeze.linalg.Counter
import breeze.util.Index

/**
 * @author jda
 */
class TextCorpusReader(source: Source, stopSymbol: Option[String] = Some(DefaultStartSymbol)) {

  def nGramIterator(n: Int): Iterator[IndexedSeq[String]] = {
    source.reset().getLines().flatMap { l => l.split(" ").toIndexedSeq.++(stopSymbol).nGrams(n) }.toIterator
  }

  def lineIterator = source.reset().getLines()

  def lineGroupIterator(n: Int) = source.reset().getLines().grouped(n)

  val vocabularyIndex: Index[String] = Index[String](nGramIterator(1).map(_.head))

  def nGramFeatureIndexer(n: Int, featurizer: IndexedSeq[String] => Iterable[Feat]) =
      Index[Feat](nGramIterator(n).flatMap(featurizer))

  def dummy(n: Int = 10): TextCorpusReader = new TextCorpusReader(Source.fromString(source.reset().getLines().slice(0,n).mkString("\n")))
}

object TextCorpusReader {

  def apply(path: String): TextCorpusReader = {
    new TextCorpusReader(Source.fromFile(path))
  }
}
