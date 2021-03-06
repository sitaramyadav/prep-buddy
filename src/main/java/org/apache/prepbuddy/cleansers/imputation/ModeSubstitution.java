package org.apache.prepbuddy.cleansers.imputation;

import org.apache.prepbuddy.cluster.TextFacets;
import org.apache.prepbuddy.rdds.TransformableRDD;
import org.apache.prepbuddy.utils.RowRecord;
import scala.Tuple2;

import java.util.List;

/**
 * An imputation strategy that imputes the column value by the mode of specified column
 */
public class ModeSubstitution implements ImputationStrategy {
    private  Tuple2 mode;
    @Override
    public void prepareSubstitute(TransformableRDD rdd, int missingDataColumn) {
        TextFacets textFacets = rdd.listFacets(missingDataColumn);
        List<Tuple2> listOfHighest = textFacets.highest();
        mode = listOfHighest.get(0);
    }

    @Override
    public String handleMissingData(RowRecord record) {
        return mode._1().toString();
    }
}
