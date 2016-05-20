package org.apache.prepbuddy.datacleansers.imputation;

import org.apache.prepbuddy.SparkTestCase;
import org.apache.prepbuddy.rdds.TransformableRDD;
import org.apache.spark.api.java.JavaRDD;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ImputationTest extends SparkTestCase {
    @Test
    public void shouldImputeTheValueWithTheMean() {
        JavaRDD<String> initialDataSet = javaSparkContext.parallelize(Arrays.asList(
                "07434677419,07371326239,Incoming,31,Wed Sep 15 19:17:44 +0100 2010",
                "07641036117,01666472054,Outgoing,20,Mon Feb 11 07:18:23 +0000 1980",
                "07641036117,07371326239,Incoming, ,Mon Feb 11 07:45:42 +0000 1980",
                "07641036117,07681546436,Missed,12,Mon Feb 11 08:04:42 +0000 1980"

        ));
        TransformableRDD initialRDD = new TransformableRDD(initialDataSet);
        TransformableRDD imputed = initialRDD.impute(3, new MeanSubstitution());
        List<String> listOfRecord = imputed.collect();

        String expected1 = "07641036117,07371326239,Incoming,15.75,Mon Feb 11 07:45:42 +0000 1980";
        assertTrue(listOfRecord.contains(expected1));
    }

    @Test
    public void shouldImputeTheValueWithTheMeanApprox() {
        JavaRDD<String> initialDataSet = javaSparkContext.parallelize(Arrays.asList(
                "07434677419,07371326239,Incoming,31,Wed Sep 15 19:17:44 +0100 2010",
                "07641036117,01666472054,Outgoing,20,Mon Feb 11 07:18:23 +0000 1980",
                "07641036117,07371326239,Incoming, ,Mon Feb 11 07:45:42 +0000 1980",
                "07641036117,07681546436,Missed,12,Mon Feb 11 08:04:42 +0000 1980"

        ));
        TransformableRDD initialRDD = new TransformableRDD(initialDataSet);
        TransformableRDD imputed = initialRDD.impute(3, new ApproxMeanSubstitution());
        List<String> listOfRecord = imputed.collect();

        String expected1 = "07641036117,07371326239,Incoming,15.75,Mon Feb 11 07:45:42 +0000 1980";
        assertTrue(listOfRecord.contains(expected1));
    }

    @Test
    public void shouldImputeTheValueWithTheMostOccurredValue() throws Exception {
        JavaRDD<String> initialDataSet = javaSparkContext.parallelize(Arrays.asList(
                "07434677419,07371326239,Incoming,31,Wed Sep 15 19:17:44 +0100 2010",
                "07641036117,01666472054,Outgoing,31,Mon Feb 11 07:18:23 +0000 1980",
                "07641036117,07371326239,Incoming, ,Mon Feb 11 07:45:42 +0000 1980",
                "07641036117,07681546436,Missed,12,Mon Feb 11 08:04:42 +0000 1980"

        ));
        TransformableRDD initialRDD = new TransformableRDD(initialDataSet);
        TransformableRDD imputed = initialRDD.impute(3, new MostOccurredSubstitution());
        List<String> listOfRecord = imputed.collect();
        String expected1 = "07641036117,07371326239,Incoming,31,Mon Feb 11 07:45:42 +0000 1980";
        assertTrue(listOfRecord.contains(expected1));
    }
}