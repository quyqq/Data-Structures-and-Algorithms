

package DataModel;

import DataModel.EnumDatabase.*;

/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 * Date: 15/04/2020
 * Version: 1.0
 * Class:SA3TenantCategory
 * This class will store apart of data from file
 * */

public class SA3TenantCategory extends Person {
    private int SA3TenantCategoryID;
    private Location location;
    private WEEKLY_INCOME incomeCategory;
    private INCOME_SOURCE incomeSource;
    /**
     * this is parameter constructor
     * @param genderCategory enum GENDER
     * @param ageCategory enum AGE
     * @param incomeCategory enum WEEKLY_INCOME
     * @param incomeSource enum INCOME_SOURCE
     * @param location object Location
     * @param SA3TenantCategoryID the primary key in database
     * */
    public SA3TenantCategory(int SA3TenantCategoryID, Location location, WEEKLY_INCOME incomeCategory,
                             INCOME_SOURCE incomeSource,AGE ageCategory, GENDER genderCategory) {
        super(ageCategory,genderCategory);
        this.SA3TenantCategoryID = SA3TenantCategoryID;
        this.location = location;
        this.incomeCategory = incomeCategory;
        this.incomeSource = incomeSource;
    }

    /**
     * default constructor
     * */
    public SA3TenantCategory() {
    }

    /**
     * getter and setter
     * */
    public int getSA3TenantCategoryID() {
        return SA3TenantCategoryID;
    }

    public Location getLocation() {
        return location;
    }

    public WEEKLY_INCOME getIncomeCategory() {
        return incomeCategory;
    }

    public INCOME_SOURCE getIncomeSource() {
        return incomeSource;
    }

    public void setSA3TenantCategoryID(int SA3TenantCategoryID) {
        this.SA3TenantCategoryID = SA3TenantCategoryID;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setIncomeCategory(WEEKLY_INCOME incomeCategory) {
        this.incomeCategory = incomeCategory;
    }

    public void setIncomeSource(INCOME_SOURCE incomeSource) {
        this.incomeSource = incomeSource;
    }

    @Override
    public String toString() {
        return String.format("[%s] [%s] [%s] [%s] [%s]",this.SA3TenantCategoryID,this.location.toString(),
                            this.incomeSource.getName(),this.incomeCategory.getName(),super.toString());
    }
}
