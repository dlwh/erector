package erector.learning

import breeze.linalg.DenseVector

/**
 * @author jda
 */
trait MarginalInference[Obs,Pred,Dec] extends Inference[Obs,Pred,Dec] {

  def marginal(observation: Obs, param: DenseVector[Num]): UMarginal[Pred,Dec]
  def goldMarginal(observation: Obs, param: DenseVector[Num]): UMarginal[Pred,Dec]

}
