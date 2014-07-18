package erector.learning

import breeze.linalg.{SparseVector, DenseVector, *}

/**
 * @author jda
 */
trait LogLinearLearning[Obs,Pred,Dec] extends MaxLikelihoodLearning[Obs,Pred,Dec] with MarginalInference[Obs,Pred,Dec] {

  override def likelihoodAndGradient(observation: Obs, param: DenseVector[Num]): (Num, DenseVector[Num]) = {
    val predMarginal = marginal(observation, param)
    val predGoldMarginal = goldMarginal(observation, param)

    // logger.debug(predMarginal.toString)
    // logger.debug(predGoldMarginal.toString)
    // logger.debug(predMarginal.sum.toString)
    // logger.debug(predGoldMarginal.sum.toString)

    val fixedFeatFunc = featureFunction.fixObservation(observation)

    val score = predGoldMarginal.logSum - predMarginal.logSum

    val goldExpFeats: SparseVector[Num] = predGoldMarginal.expectedFeatures(fixedFeatFunc)
      .asInstanceOf[SparseVector[Num]]
    //val expFeats: DenseVector[Num] = predMarginal.expectedFeatures(fixedFeatFunc).asInstanceOf[DenseVector[Num]]

    //val grad: DenseVector[Num] = goldExpFeats - expFeats
    val grad: DenseVector[Num] = goldExpFeats.toDenseVector

    //println(observation, score)
    //println(score, grad)
    //println()

    (score, grad)
  }

}
