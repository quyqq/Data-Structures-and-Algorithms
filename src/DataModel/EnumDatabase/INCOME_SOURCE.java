
package DataModel.EnumDatabase;
/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 * Date: 25/05/2020
 * Version: 1.0
 * Enum: INCOME_SOURCE
 * This enum for storing data
 * */
public enum INCOME_SOURCE {

    Employed("Employed","Employed"),
    Other("Other","Other");

    private final String name;
    private final String incomeSource;
    /**
     * this is constructor
     * */
    INCOME_SOURCE(String name, String incomeSource) {
        this.name = name;
        this.incomeSource = incomeSource;
    }
    /**
     * this is to find the income source
     * @param incomeSo name of income source
     * @return an enum of income source
     * */
    public static INCOME_SOURCE findIncomeSource(String incomeSo)
    {
        for (INCOME_SOURCE income_source:INCOME_SOURCE.values()) {
            if(income_source.getName().equalsIgnoreCase(incomeSo))
                return income_source;
        }
        return null;
    }
    /**
     * getter and setter
     * */
    public String getName() {
        return name;
    }
    /**
     * getter and setter
     * */
    public String getIncomeSource() {
        return incomeSource;
    }
}
