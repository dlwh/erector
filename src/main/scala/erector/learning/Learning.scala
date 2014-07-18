package erector.learning

import breeze.features.FeatureVector
import breeze.linalg.DenseVector

/**
 * @author jda
 */
trait Learning[Obs,Pred,Dec] {

  def learn(obsFactory: () => Iterator[Obs]): DenseVector[Num]
  def zeroParam: DenseVector[Num]
  def initParam: DenseVector[Num]
  //def featureVector(observation: Obs, prediction: Pred): FeatureVector
  //def featureFunction: OPFeatureFunction[Obs,Pred]
  def featureFunction: LocalFeatureFunction[Obs,Dec]

}
