package virtualscrum.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ResponseVelocity {
    ArrayList<LinkedHashMap<String, Object>> sprints;

    LinkedHashMap<String, HashMap<String,Object>> velocityStatEntries;

    public ResponseVelocity() {

    }

    public ResponseVelocity(ArrayList<LinkedHashMap<String, Object>> sprints, LinkedHashMap<String, HashMap<String,Object>> velocityStatEntries) {
        this.sprints = sprints;
        this.velocityStatEntries = velocityStatEntries;
    }

    public ArrayList<LinkedHashMap<String, Object>> getSprints() {
        return sprints;
    }

    public void setSprints(ArrayList<LinkedHashMap<String, Object>> sprints) {
        this.sprints = sprints;
    }

    public HashMap<String, HashMap<String,Object>> getVelocityStatEntries() {
        return velocityStatEntries;
    }

    public void setVelocityStatEntries(LinkedHashMap<String, HashMap<String,Object>> velocityStatEntries) {
        this.velocityStatEntries = velocityStatEntries;
    }
}
