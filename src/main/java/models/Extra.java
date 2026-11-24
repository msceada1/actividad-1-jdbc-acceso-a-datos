package models;

import java.util.ArrayList;
import java.util.List;

public class Extra {

    private List<String> extras = new ArrayList<>();

    public Extra(List<String> extras) {
        this.extras = extras;
    }

    public List<String> getExtras() {
        return extras;
    }

    public void setExtras(List<String> extras) {
        this.extras = extras;
    }

    @Override
    public String toString() {
        return "Extra{" +
                "extras=" + extras +
                '}';
    }
}
