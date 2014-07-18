package erector.structure

import breeze.linalg.{*, DenseVector, SparseVector, Vector, axpy}
import breeze.features.FeatureVector
import erector.learning.{Num, FeatureFunction}
import breeze.numerics.{exp,log,Inf}

/**
 * @author jda
 */
class Label(val value: Int) {
}

object Label {

  def apply(value: Int) = new Label(value)

  trait UMarginal extends erector.learning.UMarginal[Label,Int]

  class DenseUMarginal(val logWeights: DenseVector[Num]) extends Label.UMarginal {

    val weights: DenseVector[Num] = exp(logWeights)

    override def logScore(l: Label): Num = logWeights(l.value)
    override def score(l: Label): Num = weights(l.value)

    override def logSum: Num = log(sum)
    override def sum: Num = breeze.linalg.sum(weights)

    //override def expectedFeatures(featureFunction: FeatureFunction[Int]): Vector[Num] = {
    //  weights.active.iterator.foldLeft(DenseVector.zeros[Num](featureFunction.nFeatures)) { case (total, (index, weight)) =>
    //    ((featureFunction(index) :* weight) + total).toDenseVector
    //  } :/ sum
    //}
    override def expectedFeatures(featureFunction: FeatureFunction[Int]): Vector[Num] = {
      val accum = DenseVector.zeros[Num](featureFunction.nFeatures)
      //weights.activeIterator.foreach { case (i, w) => accum += featureFunction(i) :* w }
      weights.activeIterator.foreach { case (i, w) => axpy(w, featureFunction(i), accum)}
      accum
    }

    override def toString = s"DenseUMarginal($logWeights)"

  }

  class DeltaUMarginal(val value: Int, val logWeight: Num) extends Label.UMarginal {

    //val logWeight = log(weight)
    val weight = exp(logWeight)

    override def logScore(l: Label): Num = if (l.value == value) logWeight else -Inf
    override def score(l: Label): Num = if (l.value == value) weight else 0

    override def logSum: Num = logWeight
    override def sum: Num = weight

    override def expectedFeatures(featureFunction: FeatureFunction[Int]): Vector[Num] = featureFunction(value)

    override def toString = s"DeltaUMarginal($value=$logWeight)"

  }

  object UMarginal {

    //def fromDelta(value: Int, weight: Num, nLabels: Int): Label.UMarginal = new Label.UMarginal(SparseVector(nLabels)((value, weight)))
    def fromLogWeightDelta(value: Int, logWeight: Num): Label.UMarginal = new Label.DeltaUMarginal(value, logWeight)
    def fromLogWeights(logWeights: Iterable[Num]): Label.UMarginal = new Label.DenseUMarginal(DenseVector[Num](logWeights.toArray))

  }
}


