package org.apache.prepbuddy.datacleansers.imputation;

import org.apache.prepbuddy.rdds.TransformableRDD;
import org.apache.prepbuddy.utils.RowRecord;
import org.apache.spark.api.java.JavaDoubleRDD;

/**
 * A imputation strategy that imputes the missing column value by
 * approx mean of the specified column.
 */
public class ApproxMeanSubstitution implements ImputationStrategy {
    private Double approximateMean;

    @Override
    public void prepareSubstitute(TransformableRDD rdd, int missingDataColumn) {
        int timeout = 20000;
        JavaDoubleRDD javaDoubleRDD = rdd.toDoubleRDD(missingDataColumn);
        this.approximateMean = javaDoubleRDD.meanApprox(timeout).getFinalValue().mean();
    }

    @Override
    public String handleMissingData(RowRecord record) {
        return approximateMean.toString();
    }
}
