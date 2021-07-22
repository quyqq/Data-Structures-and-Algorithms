package DataModel;

import DataModel.EnumDatabase.*;
/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 * Date: 15/04/2020
 * Version: 1.0
 * Class: Person
 * This is a generic class and has an age category, and a gender category.
 * This class will remain abstract as no objects will be required of type Person.
 * */

public abstract class Person {

    private AGE ageCategory;
    private GENDER genderCategory;

    /**
     * this method is parameter constructor
     * @param ageCategory enum of AGE
     * @param genderCategory  enum of GENDER
     *
     * */
    public Person(AGE ageCategory, GENDER genderCategory) {
        this.ageCategory = ageCategory;
        this.genderCategory = genderCategory;
    }
    /**
     * this method is default constructor
     * */
    public Person() {
    }
    /**
     * this methods are getter and setter
     * */
    public AGE getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(AGE ageCategory) {
        this.ageCategory = ageCategory;
    }

    public GENDER getGenderCategory() {
        return genderCategory;
    }

    public void setGenderCategory(GENDER genderCategory) {
        this.genderCategory = genderCategory;
    }
    @Override
    public String toString() {
        return String.format(" [%s]  [%s]",this.ageCategory.name(),this.genderCategory.name());
    }



}
