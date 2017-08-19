package de.perdian.apps.flighttracker.business.modules.importexport.data.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OpenflightsHelper {

    public static List<String> tokenizeLine(String line) {
        String[] lineValues = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        List<String> resultValues = new ArrayList<>(lineValues.length);
        for (String lineValue : lineValues) {
            int startIndex = lineValue.startsWith(String.valueOf(OpenflightsConstants.FIELD_DELIMITER)) ? 1 : 0;
            int endIndex = lineValue.endsWith(String.valueOf(OpenflightsConstants.FIELD_DELIMITER)) ? lineValue.length() - 1 : lineValue.length();
            resultValues.add(lineValue.substring(startIndex, endIndex));
        }
        return Collections.unmodifiableList(resultValues);
    }

}
