package sample.model;

/**
 * Created by vladstarikov on 12/21/16.
 * Витрата
 */
@SuppressWarnings("WeakerAccess")
public class Cost {

    public String name;
    public double value;

    public Cost() {}

    public Cost(String name, int value) {
        this.name = name;
        this.value = value;
    }
}