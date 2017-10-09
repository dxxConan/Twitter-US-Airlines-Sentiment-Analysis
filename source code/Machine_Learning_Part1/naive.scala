package template.template;

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint

object naive {
  def main(args: Array[String]) = {
      val conf = new SparkConf().setAppName("naive")
      val sc = new SparkContext(conf)
        // Load and parse the data file.
    //  val data = MLUtils.loadLibSVMFile(sc, args(0))
      //val process = sc.parallelize(data).
     
      val data = sc.textFile(args(0))
      val parsedData = data.map { line =>
      val parts = line.split(',')
      LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
      }


    // Split data into training (70%) and test (30%).
      val splits = parsedData.randomSplit(Array(0.7, 0.3), seed = 11L)
      val training = splits(0)
      val test = splits(1)

      val model = NaiveBayes.train(training, lambda = 1.0, modelType = "multinomial")

      val predictionAndLabel = test.map(p => (model.predict(p.features), p.label))
      val accuracy =1.0*predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()
      println("NB accuracy = " + accuracy)
  //    println("Learned classification naive bayes model:\n" + model.toDebugString)
      
// Save and load model
   //   model.save(sc, "myNBClassificationModel")
   //   val sameModel = NaiveBayesModel.load(sc, "myNBClassificationModel")
      
  }
}