package sample.model;

/**
 * Created by vladstarikov on 12/11/16.
 */
public class BusinessKind {

    public String name;

    //Subekti gospodoruvannya
    public int smallCompaniesCount;
    public int middleCompaniesCount;
    public int largeCompaniesCount;

    public int privateCount;

    //Employers
    public int employers;
    public int privateEmployers;

    //Personnel costs in UAH
    public long smallCompaniesPersonnelCosts;
    public long middleCompaniesPersonnelCosts;
    public long largeCompaniesPersonnelCosts;

    //Produced goods in UAH
    public long smallCompaniesProducedGoods;
    public long middleCompaniesProducedGoods;
    public long largeCompaniesProducedGoods;

    //Sales in UAH
    public long smallCompaniesSales;
    public long middleCompaniesSales;
    public long largeCompaniesSales;

    //Net profit int UAH //TODO:
    public int companiesCountPositive;
    public int companiesCountNegative;

    //Profitability in UAH
    public long operatingActivitiesResult;
    public long operatingCost;
    public double profitabilityLevel;//In percents

    public BusinessKind(String name) {
        this.name = name;
    }
}