package erector.learning

/**
 * @author jda
 */
trait MaxMarginLearning[Obs,Pred,Dec] extends Learning[Obs,Pred,Dec] {

  def scoreAndStep(observation: Obs, prediction: Pred): (Num, Vector[Num])

}
