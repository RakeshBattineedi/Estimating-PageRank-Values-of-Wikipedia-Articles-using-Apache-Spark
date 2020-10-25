import org.apache.spark.sql.SparkSession

object PageRank {
  def main(args: Array[String]) {
    val spark = SparkSession
      .builder()
      .appName("PageRank")
      .master("local")
      .getOrCreate();
    val sc1 = spark.sparkContext

    val lines = spark.read.textFile(args(0)).rdd
    val titles = spark.read.textFile(args(1)).rdd.zipWithIndex().mapValues(x => x + 1).map(_.swap);
    val links = lines.map(s => (s.split(": ")(0), s.split(": ")(1).split(" ")))
    val total = lines.count()
    var ranks = links.mapValues(v => 1.0 / total)
    for (i <- 1 to 25) {
      val tempRank = links.join(ranks).values.flatMap {
        case (urls, rank) =>
          val noOfOutgoingLinks = urls.size
          urls.map(url => (url, rank / noOfOutgoingLinks))
      }
      ranks = tempRank.reduceByKey(_ + _)
    }
    val titlefinal = titles.map { case (index, name) => (index.toString, name) }
    val finalrank = titlefinal.join(ranks).values
    val sortPR = finalrank.sortBy(_._2, false).take(10)
    sc1.parallelize(sortPR.toSeq).saveAsTextFile(args(2))

  }
}