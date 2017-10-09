package template.template;
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint

object dt {
  def main(args: Array[String]) = {
      val conf = new SparkConf().setAppName("decision tree")
      val sc = new SparkContext(conf)
        // Load and parse the data file.
      
    //  val data = MLUtils.loadLibSVMFile(sc, args(0))
      //val process = sc.parallelize(data).
     /* 
      val data = sc.textFile(args(0))
      val parsedData = data.map { line =>
      val parts = line.split(',')
      LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
      }
      
      
      // Split the data into training and test sets (30% held out for testing)
      val splits = parsedData.randomSplit(Array(0.7, 0.3))
      val (trainingData, testData) = (splits(0), splits(1))
      */
      val data = MLUtils.loadLibSVMFile(sc, args(0))
      val splits = data.randomSplit(Array(0.7, 0.3), seed = 11L)
      val trainingData = splits(0).cache()
      val testData = splits(1)
      
      // Train a DecisionTree model.
      //  Empty categoricalFeaturesInfo indicates all features are continuous.
      val numClasses = args(3).toInt;
      val categoricalFeaturesInfo = Map[Int, Int](0 -> args(3).toInt)
      val impurity = args(2)
      //trim desicion tree by controling maxDepth
      val maxDepth = args(1).toInt;
      val maxBins = 32
      
      val model = DecisionTree.trainClassifier(trainingData, numClasses, categoricalFeaturesInfo,
        impurity, maxDepth, maxBins)
      
      // Evaluate model on test instances and compute test error
      val labelAndPreds = testData.map { point =>
        val prediction = model.predict(point.features)
        (point.label, prediction)
      }
      val accuracy = labelAndPreds.filter(r => r._1 == r._2).count().toDouble / testData.count()
      println(testData.count())
      println("DT accuracy = " + accuracy)
      println("Learned classification tree model:\n" + model.toDebugString)
      
      // Save and load model
    //  model.save(sc, "myDecisionTreeClassificationModel")
    //  val sameModel = DecisionTreeModel.load(sc, "myDecisionTreeClassificationModel")
  }
}