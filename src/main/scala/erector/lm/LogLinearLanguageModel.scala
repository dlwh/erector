package erector.lm

import erector.corpus.TextCorpusReader
import breeze.linalg._
import breeze.features.FeatureVector
import erector.learning._
import erector.structure.Label
import com.typesafe.scalalogging.slf4j.Logging

/**
 * @author jda
 */
class LogLinearLanguageModel[T](weights: DenseVector[Double]) extends NGramLanguageModel[T] {

  override def predict(context: Seq[T], decision: T): Unit = ???

}

object LogLinearLanguageModel extends Logging {

  def main(args: Array[String]) {
    val trainPath = args(0)
    val corpusReader = TextCorpusReader(trainPath)
    logger.info("instantiated erector.corpus reader")
    val learning = new LogLinearLanguageModelLearning(3, corpusReader)
    logger.info("set up erector.learning")
    val trainingSamples = { () => corpusReader.nGramIterator(3) }
    corpusReader.nGramIterator(3).foreach(x => ())
    trainingSamples().foreach(x => ())
    logger.info("created training samples")
    val optParams = learning.learn(trainingSamples)
    logger.info("done with erector.learning")
    learning.featIndex.pairs.foreach { case (feat, idx) => println(s"$feat ${optParams(idx)}")}
  }


}

class LogLinearLanguageModelLearning(order: Int, corpus: TextCorpusReader) extends LogLinearLearning[NGram,Label,Int] {

  def featurize(ngram: NGram) = ngram.slice(0, ngram.length - 1).zipWithIndex.map(wi => s"${wi}__${ngram.last}")

  val vocabularyIndex = corpus.vocabularyIndex
  val featIndex = corpus.nGramFeatureIndexer(order, featurize)
  corpus.lineIterator
  println("woo")

  //println(featIndex.size)

  override def zeroParam = DenseVector.zeros[Num](featIndex.size)
  override def initParam = DenseVector.ones[Num](featIndex.size)

  override def featureFunction = new LocalFeatureFunction[NGram,Int] {
    override def nFeatures = featIndex.size
    override def apply(pair: (NGram, Int)): SparseVector[Num] = {
      val (obs, pred) = pair
      val features = featurize(obs.slice(0, obs.length - 1) :+ vocabularyIndex.get(pred))
      //new FeatureVector(features.map(featIndex(_)).toArray)
      SparseVector(featIndex.size)(features filter { featIndex.contains } map { feat => (featIndex(feat), 1.0) }:_*)
    }
  }

  override def goldMarginal(observation: erector.lm.NGram, param: DenseVector[Num]): Label.UMarginal = {
    //logger.debug(s"compute gold marginal $observation $param")
    //logger.debug(featureFunction((observation, vocabularyIndex.indexOf(observation.last))).toString)
    Label.UMarginal.fromLogWeightDelta(vocabularyIndex(observation.last),
                                       featureFunction((observation, vocabularyIndex.indexOf(observation.last))) dot param)
  }

  override def marginal(observation: erector.lm.NGram, param: DenseVector[Num]): Label.UMarginal = {
    Label.UMarginal.fromLogWeights {
      vocabularyIndex.map { word => featureFunction((observation, vocabularyIndex.indexOf(word))) dot param }
    }
  }

}
