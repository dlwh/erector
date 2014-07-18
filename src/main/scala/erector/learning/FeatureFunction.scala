package erector.learning

import breeze.linalg.SparseVector

/**
 * @author jda
 */
trait FeatureFunction[T] extends (T => SparseVector[Num]) {

  def nFeatures: Int

}

//trait OPFeatureFunction[LObs,LPred] extends FeatureFunction[(LObs,LPred)] {
//  def fixObservation(observation: LObs) = new FeatureFunction[Pred] {
//
//  }
//}

trait LocalFeatureFunction[Obs,Dec] extends FeatureFunction[(Obs,Dec)] {
  def fixObservation(observation: Obs) = new FeatureFunction[Dec] {
    //def apply(localContext: LCont, localPrediction: LPred) = LocalFeatureFunction.this.apply(observation, localContext, localPrediction)
    def apply(decision: Dec) = LocalFeatureFunction.this.apply(observation, decision)
    def nFeatures = LocalFeatureFunction.this.nFeatures
  }
}

// trait OPFeatureFunction[Obs,Pred] extends FeatureFunction[(Obs,Pred)] {
//
//   def fixObservation(observation: Obs) = new FeatureFunction[Pred] {
//     def apply(prediction: Pred) = OPFeatureFunction.this.apply(observation, prediction)
//     def nFeatures = OPFeatureFunction.this.nFeatures
//   }
//
// }
