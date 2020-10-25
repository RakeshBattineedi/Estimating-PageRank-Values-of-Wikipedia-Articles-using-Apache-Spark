import org.apache.spark.sql.SparkSession

object Wikibomb extends App {

  val sc = SparkSession.builder().appName("Wikibomb")
    .master("local")
    .getOrCreate();
  val sc1 = sc.sparkContext

  val lines = sc.read.textFile(args(0)).rdd
  val titles = sc.read.textFile(args(1)).rdd.zipWithIndex().mapValues(x => x + 1).map(_.swap);
  val titlefinal = titles.map{case(index, name)=> (index.toString, name)}
  val links = lines.map(s=>(s.split(": ")(0), s.split(": ")(1).split(" ")))
  val surfingtitle = titlefinal.filter{case(k,v)=> v.toLowerCase.contains("surfing")}
  val InitialWebGraph = surfingtitle.join(links).map { case(k, (_, v2)) => (k, v2) }

 // val InitialWebGraph = links.filter{case(k,v)=>(surfingtitle.contains(k))}
  val total = InitialWebGraph.count()
  val rockytitle = titlefinal.filter{case(k,v)=> v.equalsIgnoreCase("rocky_mountain_national_park")}.keys.take(1)
  val bomb = InitialWebGraph.mapValues(v => v:+rockytitle.mkString(""))
  var ranks = bomb.mapValues(v => 1.0/ total )

  for (i <- 1 to 25) {
    val tempRank =  bomb.join(ranks).values.flatMap {
      case (urls, rank) =>
        val noOfOutgoingLinks = urls.size
        urls.map(url => (url, rank / noOfOutgoingLinks))  }
    ranks = tempRank.reduceByKey(_ + _)
  }

  val finalrank = titlefinal.join(ranks).values
  val sortPR = finalrank.sortBy(_._2, false).take(10)
  sc1.parallelize(sortPR.toSeq).saveAsTextFile(args(2))

}
