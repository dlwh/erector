package erector.learning

import breeze.features.FeatureVector
import breeze.linalg.Vector

/**
 * @author jda
 */
trait UMarginal[T,D] {

  def score(t: T): Num
  def logScore(t: T): Num
  def sum: Num
  def logSum: Num
  def expectedFeatures(featureFunction: FeatureFunction[D]): Vector[Num]

}
