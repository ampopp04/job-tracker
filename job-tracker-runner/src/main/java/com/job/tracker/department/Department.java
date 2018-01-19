package com.job.tracker.department;


import com.system.db.entity.named.NamedEntity;

/**
 * The <class>Department</class> represents
 * a department of this company.
 * <p>
 * Ex: Accounting, Sales
 *
 * @author Andrew Popp
 */
public class Department extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public Department() {
    }

    public static Department newInstance(String name, String description) {
        Department department = new Department();
        department.setName(name);
        department.setDescription(description);
        return department;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

}
