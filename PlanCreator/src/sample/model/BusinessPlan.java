package sample.model;

import java.util.List;

/**
 * Created by vladstarikov on 12/21/16.
 */
public class BusinessPlan {

    public String businessName;
    public String currency;
    public String unitOfMeasurement;//pieces, ton's, liter
    public int onePeacePrice;
    public int monthlySales;

    public List<Cost> constantCosts;
    public List<Cost> variableCosts;
}