package de.perdian.flightlog.support.utils;

import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ResourceUtils {

    public static List<String> resourceToLines(Resource resource) throws IOException {
        List<String> resourceLines = new ArrayList<>();
        try (BufferedReader resourceReader = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"))) {
            for (String line = resourceReader.readLine(); line != null; line = resourceReader.readLine()) {
                String strippedLine = line.strip();
                if (!line.startsWith("#")) {
                    resourceLines.add(line);
                }
            }
        }
        return resourceLines;
    }

}
