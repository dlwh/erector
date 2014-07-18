package erector.predictor

import erector.learning.{LocalFeatureFunction, FeatureFunction, UMarginal}
import erector.structure.Label
import breeze.linalg.{argmax, max, DenseVector}

/**
 * @author jda
 */

trait Predictor[Obs,Pred] {
  def predict(observation: Obs): Pred
}

trait Classifier[Obs] extends Predictor[Obs,Label] {
  def predict(observation: Obs): Label
}

class LogLinearClassifier[Obs](val featureFunction: LocalFeatureFunction[Obs,Int],
                               val weights: DenseVector[Double],
                               val nChoices: Int) extends Classifier[Obs] {

  def predict(observation: Obs): Label = {
    new Label(argmax {
      ((0 until nChoices) map { choice: Int =>
        featureFunction(observation, choice) dot weights
      }).toArray
    })
  }

}
