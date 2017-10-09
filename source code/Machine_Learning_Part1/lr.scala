
package template.template;
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.mllib.classification.{LogisticRegressionWithLBFGS, LogisticRegressionModel}
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.util.MLUtils

object lr {
  def main(args: Array[String]) = {
      val conf = new SparkConf().setAppName("logistic regression")
      val sc = new SparkContext(conf)
          // Load training data in LIBSVM format.
      
    /*  
      // Split data into training (70%) and test (30%).
       val data = sc.textFile(args(0))
      val parsedData = data.map { line =>
      val parts = line.split(',')
      LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
      }
    */  
     
      // Split the data into training and test sets (30% held out for testing)
   //   val splits = parsedData.randomSplit(Array(0.7, 0.3))
   //   val (training, test) = (splits(0), splits(1))
      val data = MLUtils.loadLibSVMFile(sc, args(0))
      val splits = data.randomSplit(Array(0.7, 0.3), seed = 11L)
      val training = splits(0).cache()
      val test = splits(1)
      
      // Run training algorithm to build the model
      val numClasses = args(1).toInt;
      val model = new LogisticRegressionWithLBFGS()
        .setNumClasses(numClasses)
        .run(training)
      
      // Compute raw scores on the test set.
      val predictionAndLabels = test.map { case LabeledPoint(label, features) =>
        val prediction = model.predict(features)
        (prediction, label)
      }
      
      // Get evaluation metrics.
      val metrics = new MulticlassMetrics(predictionAndLabels)
      val precision = metrics.precision
      println("LR Precision = " + precision)
      
      // Save and load model
    //  model.save(sc, "mylogisticRegression")
    //  val sameModel = LogisticRegressionModel.load(sc, "logisticRegression")
  }
}