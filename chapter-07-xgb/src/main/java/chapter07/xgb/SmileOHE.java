package chapter07.xgb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import joinery.DataFrame;
import smile.data.NumericAttribute;
import smile.data.SparseDataset;

public class SmileOHE {

    public static SparseDataset oneHotEncoding(DataFrame<Object> categorical) {
        Map<String, Integer> valueToIndex = new HashMap<>();
        List<Object> columns = new ArrayList<>(categorical.columns());

        int ncol = 0;

        for (Object name : columns) {
            List<Object> column = categorical.col(name);
            Set<Object> distinct = new HashSet<>(column);
            for (Object val : distinct) {
                String stringValue = Objects.toString(name) + "_" + Objects.toString(val);
                valueToIndex.put(stringValue, ncol);
                ncol++;
            }
        }

        SparseDataset result = new SparseDataset(ncol, new NumericAttribute("target"));

        ListIterator<List<Object>> rows = categorical.iterrows();
        while (rows.hasNext()) {
            int rowIdx = rows.nextIndex();
            List<Object> row = rows.next();
            for (int colIdx = 0; colIdx < columns.size(); colIdx++) {
                Object name = columns.get(colIdx);
                Object val = row.get(colIdx);
                String stringValue = Objects.toString(name) + "_" + Objects.toString(val);
                int targetColIdx = valueToIndex.get(stringValue);

                result.set(rowIdx, targetColIdx, 1.0);
            }
        }

        return result;
    }

}
