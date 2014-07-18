package erector.learning

import breeze.linalg.DenseVector

/**
 * @author jda
 */
trait MaxInference[Obs,Pred,Dec] extends Inference[Obs,Pred,Dec] {

  def max(observation: Obs, param: DenseVector[Num]): Pred
  def goldMax(observation: Obs, param: DenseVector[Num]): Pred

}
