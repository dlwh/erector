package erector.learning

import breeze.optimize.{BatchDiffFunction,minimize}
import breeze.linalg.DenseVector
import com.typesafe.scalalogging.slf4j.Logging
import breeze.optimize.FirstOrderMinimizer.OptParams

/**
 * @author jda
 */
trait MaxLikelihoodLearning[Obs,Pred,Dec] extends Learning[Obs,Pred,Dec] with Logging {

  def likelihoodAndGradient(observation: Obs, param: DenseVector[Num]): (Num, DenseVector[Num])

  override def learn(exampleFactory: () => Iterator[Obs]): DenseVector[Num] = {

    val objective = new BatchDiffFunction[DenseVector[Double]] {

      override def fullRange: IndexedSeq[Int] = {
        var counter = 0
        exampleFactory().foreach { ex => counter += 1 }
        0 until counter
      }

      override def calculate(param: DenseVector[Double], batch: IndexedSeq[Int]): (Double, DenseVector[Double]) = {

        val setBatch = batch.toSet
        println (setBatch.size)
        //println(setBatch)

        var ll: Double = 0.0
        val grad: DenseVector[Double] = zeroParam

        exampleFactory().zipWithIndex.foreach { case (obs, i) =>
          if (setBatch.contains(i)) {
            val (obsLL, obsGrad) = likelihoodAndGradient(obs, param)
            ll += obsLL
            grad += obsGrad
          }
        }

        //exit()
        println(-ll)

        (-ll, -grad)

      }

    }

    val opt = OptParams(useStochastic = true, batchSize = 100000, regularization = 1000)
    opt.minimize(objective, initParam)
    //val optimizer = new LBFGS[DenseVector[Double]]
    //optimizer.minimize(objective, initParam)
    //minimize(objective, initParam)

    // val param: DenseVector[Double] = initParam
    // examples.foreach { obs =>
    //   val (obsLL, obsGrad) = likelihoodAndGradient(obs, param)
    //   param += obsGrad
    //   logger.info(obsLL.toString)
    // }
  }

}
