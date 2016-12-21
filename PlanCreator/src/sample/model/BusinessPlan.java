package sample.model;

import java.util.List;

/**
 * Created by vladstarikov on 12/21/16.
 */
public class BusinessPlan {

    public String businessName;
    public String currency;
    public String unitOfMeasurment;//pieces, ton's, liters
    public int monthlySales;

    public List<Cost> constantCosts;
    public List<Cost> variableCosts;
}
