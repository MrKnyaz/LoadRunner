package model;

import java.time.LocalDate;
import java.util.*;

public class Metrics {

    HashMap<String, ArrayList<MetricsDetails>> data = new HashMap<>();

    public HashMap<String, ArrayList<MetricsDetails>> getData() {
        return data;
    }

    public void add(Metrics other) {
        for (String key: other.data.keySet()) {
            if (this.data.get(key) != null) {
                this.data.get(key).addAll(other.data.get(key));
            } else {
                this.data.put(key, other.data.get(key));
            }
        }
    }

    public void add(String metricName, MetricsDetails value) {
        if (data.get(metricName) != null) {
            data.get(metricName).add(value);
        } else {
            data.put(metricName, new ArrayList<>());
            data.get(metricName).add(value);
        }
    }

    public String toCSV() {
        StringBuilder out = new StringBuilder();
        for (String key: data.keySet()) {
            List<MetricsDetails> details = data.get(key);
            Collections.sort(details, Comparator.comparing(curr -> curr.startTime));
            for (MetricsDetails detail: details) {
                out.append(detail.toCSV() + "\n");
            }
        }
        return out.toString();
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("******************************************************\n\n");
        for (String key: data.keySet()) {
            out.append(key + ": ");
            List<MetricsDetails> details = data.get(key);
            long total = 0;
            for (MetricsDetails detail: details) {
                total += detail.execTime;
                out.append(detail + " ");
            }
            if (details.size() > 0) {
                long avg = total / details.size();
                out.append("(AVG: " + avg + "ms)\n");
            }
            out.append("------------\n");
        }
        out.append("Current Date:" + LocalDate.now() + "\n");
        out.append("******************************************************");
        return out.toString();
    }

}
