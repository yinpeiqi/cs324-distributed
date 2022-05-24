
import pyspark
from pyspark import SparkContext, SparkConf
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

if __name__ == "__main__":
    spark = SparkSession.builder.master("local").\
        appName("CS324").\
        config("spark.driver.bindAddress","localhost").\
        config("spark.ui.port","4050").\
        getOrCreate()

    df = spark.read.csv("parking_data_sz.csv")
    df = df.withColumnRenamed("_c0", "out_time") \
        .withColumnRenamed("_c1", "admin_region") \
        .withColumnRenamed("_c2", "in_time") \
        .withColumnRenamed("_c3", "berthage") \
        .withColumnRenamed("_c4", "section")

    # remove the value that out_time > in_time.
    df = df.filter(to_timestamp(df["out_time"]) > to_timestamp(df["in_time"]))
    # add a column `parking time`.
    df = df.withColumn("parking_time", to_timestamp(df["out_time"]).cast("long") - to_timestamp(df["in_time"]).cast("long"))

    # Q1
    df.groupBy("section").count().repartition(1) \
        .write.format("com.databricks.spark.csv") \
        .option("header", "true") \
        .save("result/Q1")

    # Q2
    df.groupBy("section").avg("parking_time").repartition(1) \
        .write.format("com.databricks.spark.csv") \
        .option("header", "true") \
        .save("result/Q2")

    # Q3
    df.groupBy("berthage").avg("parking_time").sort(desc(avg("parking_time"))).repartition(1) \
        .write.format("com.databricks.spark.csv") \
        .option("header", "true") \
        .save("result/Q3")
