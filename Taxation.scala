import java.io.{File, PrintWriter}

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark._
import org.apache.spark.sql.SparkSession

object Taxation extends App {

  val sc = SparkSession
    .builder()
    .appName("Taxation")
    .master("local")
    .getOrCreate();
  val sc1 = sc.sparkContext

  val lines = sc.read.textFile(args(0)).rdd
  val titles = sc.read.textFile(args(1)).rdd.zipWithIndex().mapValues(x => x + 1).map(_.swap);
  val links = lines.map(s => (s.split(": ")(0), s.split(": ")(1).split(" ")))
  val total = lines.count()
  var ranks = links.mapValues(v => 1.0 / total)
  for (i <- 1 to 25) {
    val tempRank = links.join(ranks).values.flatMap {
      case (urls, rank) =>
        val noOfOutgoingLinks = urls.size
        urls.map(url => (url, rank / noOfOutgoingLinks))
    }
    ranks = tempRank.reduceByKey(_ + _).mapValues((0.15 / total) + 0.85 * _)
  }
  val titlefinal = titles.map { case (index, name) => (index.toString, name) }
  val finalrank = titlefinal.join(ranks).values
  val sortPR = finalrank.sortBy(_._2, false).take(10)
  sc1.parallelize(sortPR.toSeq).saveAsTextFile(args(2))

}