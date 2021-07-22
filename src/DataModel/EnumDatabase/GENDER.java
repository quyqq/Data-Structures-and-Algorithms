



package DataModel.EnumDatabase;

/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 * Date: 25/05/2020
 * Version: 1.0
 * Enum: GENDER
 * This enum for storing data
 * */
public enum  GENDER {
    Female("Female","Female"),
    Male("Male","Male");
    private final String name;
    private final String gender;

    GENDER(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }
    /**
     * this method find a gender
     * @param inGender the gender
     * @return  GENDER
     * */
    public static GENDER findGender(String inGender)
    {
        for (GENDER gender:GENDER.values()) {
            if(gender.getName().equalsIgnoreCase(inGender))
                return gender;
        }
        return null;
    }
    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }
}
