package sample.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vladstarikov on 12/13/16.
 */
public class JsonRow {

    @SerializedName("NameRow")
    public String name;

    @SerializedName("Columns")
    public List<JsonColumn> columns;

    @Override
    public String toString() {
        return super.toString() + ": name = " + name;
    }
}
