# Estimating-PageRank-Values-of-Wikipedia-Articles-using-Apache-Spark

Objectives

The goal of this project to enable you to gain experience in:

• Installing and using analytics tools such as HDFS and Apache Spark

• Implementing iterative algorithms to estimate PageRank values of Wikipedia articles using Wikipedia dump data

• Designing and implementing batch layer computation using Hadoop MapReduce and HDFS

Overview

In this project, you will design and implement a system that calculates PageRank1 values of currently available Wikipedia articles. The PageRank algorithm is used by Google Search to rank web pages in their search engine query results. PageRank measures the importance of web pages. The underlying assumption is that more important web pages are likely to receive more links from other web pages.

I will implement algorithms to estimate PageRank values over the Wikipedia dataset. These results can be used to rank search results within Wikipedia. Most of the Wikipedia articles contain links to other articles or external web contents. We will consider only internal Wikipedia articles to calculate the PageRank. Currently Wikipedia contains more than 4.9 million Articles2. Wikipedia publishes data dumps in various formats periodically3. In this project, I will perform:

• Estimation of PageRank values under ideal conditions

• Estimation of the PageRank values while considering dead-end articles

• Analysis of the above results: Creating a Wikipedia Bomb

To perform the PageRank algorithm over the Wikipedia dump, you are required to use Spark in an iterative fashion
