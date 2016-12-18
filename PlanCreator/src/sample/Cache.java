package sample;

import com.sun.istack.internal.NotNull;
import sample.model.BusinessKind;
import sample.model.JsonTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladstarikov on 12/11/16.
 */
public final class Cache {

    private static final String TABLE_KSGD = "http://ukrstat.org/uk/operativ/operativ2014/fin/osp/ksg/ksg_u/ksg_u_14.htm";
    private static final String TABLE_KP = "http://ukrstat.org/uk/operativ/operativ2013/fin/kp_ed/kp_ed_u/kp_ed_u_2015.htm";

    private static final String TABLE_KZP = "http://ukrstat.org/uk/operativ/operativ2014/fin/osp/kzp/kzp_u/kzp_u_14.htm";
    private static final String TABLE_VNP = "https://ukrstat.org/uk/operativ/operativ2013/fin/kp_ed/kp_ed_u/vpp_ed_u_2015.htm";

    private static final String TABLE_CHPR = "https://ukrstat.org/uk/operativ/operativ2016/fin/chpr/chpr_ed/chpr_ed_u/chpr_ed_0116_u.htm";
    private static final String TABLE_RODP = "https://ukrstat.org/uk/operativ/operativ2016/fin/rodp/rodp_ed/rodp_ed_u/rodp_ed_0116_u.htm";

    private static final String TABLE_OVP = "https://ukrstat.org/uk/operativ/operativ2015/fin/ovpp/ovpp_2014_u.htm";
    private static final String TABLE_ORP = "https://ukrstat.org/uk/operativ/operativ2013/fin/kp_ed/kp_ed_u/orp_ed_u_2015.htm";

    private static final int MILLION = 1000000;
    private static final int THOUSAND = 1000;

    private static final String REGEXP_DEl = "(')|(`)|(’)|( )";

    @NotNull
    public List<BusinessKind> businessKinds = new ArrayList<>();

    private static volatile Cache instance;

    private Cache() {
    }

    public static Cache getInstance() {
        if (instance == null) {
            synchronized (Cache.class) {
                if (instance == null) {
                    instance = new Cache();
                }
            }
        }
        return instance;
    }

    public BusinessKind getBusinessKindByName(String name) {
        return businessKinds.stream()
                .filter(businessKind -> equals(businessKind.name, name))
                .findFirst()
                .orElseGet(() -> {
                    BusinessKind created = new BusinessKind(name);
                    businessKinds.add(created);
                    return created;
                });
    }

    private boolean equals(String l, String r) {
        return l.replaceAll(REGEXP_DEl, "").equalsIgnoreCase(r.replaceAll(REGEXP_DEl, ""));
    }

    public void mapTable(JsonTable table) {
        switch (table.link) {
            case TABLE_KSGD:
                table.rows.stream().skip(2).forEach(jsonRow -> {
                    BusinessKind business = getBusinessKindByName(jsonRow.name);
                    business.privateCount = (int) jsonRow.columns.get(3).value;
                });
                break;
            case TABLE_KP:
                table.rows.stream().skip(2).forEach(jsonRow -> {
                    BusinessKind business = getBusinessKindByName(jsonRow.name);
                    business.largeCompaniesCount = (int) jsonRow.columns.get(1).value;
                    business.middleCompaniesCount = (int) jsonRow.columns.get(3).value;
                    business.smallCompaniesCount = (int) jsonRow.columns.get(5).value;
                });
                break;
            case TABLE_KZP:
                table.rows.stream().skip(2).forEach(jsonRow -> {
                    BusinessKind business = getBusinessKindByName(jsonRow.name);
                    business.employers = (int) (jsonRow.columns.get(1).value * THOUSAND);
                    business.privateEmployers = (int) (jsonRow.columns.get(3).value * THOUSAND);
                });
                break;
            case TABLE_VNP:
                table.rows.stream().skip(2).forEach(jsonRow -> {
                    BusinessKind business = getBusinessKindByName(jsonRow.name);
                    business.smallCompaniesPersonnelCosts = (long) (jsonRow.columns.get(1).value * MILLION);
                    business.middleCompaniesPersonnelCosts = (long) (jsonRow.columns.get(3).value * MILLION);
                    business.largeCompaniesPersonnelCosts = (long) (jsonRow.columns.get(5).value * MILLION);
                });
                break;
            case TABLE_CHPR:
                table.rows.stream().skip(2).forEach(jsonRow -> {
                    BusinessKind business = getBusinessKindByName(jsonRow.name);
                    business.companiesCountPositive = (int) (jsonRow.columns.get(2).value * MILLION);//TODO:
                    business.companiesCountNegative = (int) (jsonRow.columns.get(4).value * MILLION);//TODO:
                });
                break;
            case TABLE_RODP:
                table.rows.stream().skip(2).forEach(jsonRow -> {
                    BusinessKind business = getBusinessKindByName(jsonRow.name);
                    business.operatingActivitiesResult = (long) (jsonRow.columns.get(0).value * MILLION);
                    business.operatingCost = (long) (jsonRow.columns.get(1).value * MILLION);
                    business.profitabilityLevel = jsonRow.columns.get(2).value;
                });
                break;
            case TABLE_OVP:
                table.rows.stream().skip(2).forEach(jsonRow -> {
                    BusinessKind business = getBusinessKindByName(jsonRow.name);
                    business.smallCompaniesProducedGoods = (long) (jsonRow.columns.get(5).value * MILLION);
                    business.middleCompaniesProducedGoods = (long) (jsonRow.columns.get(3).value * MILLION);
                    business.largeCompaniesProducedGoods = (long) (jsonRow.columns.get(1).value * MILLION);
                });
                break;
            case TABLE_ORP:
//                table.rows.stream().skip(2).forEach(jsonRow -> {
//                    BusinessKind business = getBusinessKindByName(jsonRow.name);
//                    business.smallCompaniesSales = (int) (jsonRow.columns.get(5).value * MILLION);
//                    business.middleCompaniesSales = (int) (jsonRow.columns.get(3).value * MILLION);
//                    business.largeCompaniesSales = (int) (jsonRow.columns.get(1).value * MILLION);
//                });
                break;
        }
    }
}