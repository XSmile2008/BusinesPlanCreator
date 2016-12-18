package sample.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vladstarikov on 12/13/16.
 */
public class JsonTable {

    @SerializedName("Link")
    public String link;

    @SerializedName("Datas")
    public List<JsonRow> rows;

    @Override
    public String toString() {
        return super.toString() + ": link = " + link;
    }
}
