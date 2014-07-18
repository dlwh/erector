package erector.lm

/**
 * @author jda
 */
trait LanguageModel[C,D] {

  def predict(context: C, decision: D)

}
