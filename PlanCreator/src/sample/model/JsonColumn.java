package sample.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vladstarikov on 12/13/16.
 */
public class JsonColumn {

    @SerializedName("NameColumn")
    public String name;

    @SerializedName("Value")
    public double value;

    @Override
    public String toString() {
        return super.toString() + ": name = " + name + ", value = " + value;
    }
}