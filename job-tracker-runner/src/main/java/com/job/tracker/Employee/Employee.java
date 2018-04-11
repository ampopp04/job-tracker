package com.job.tracker.Employee;


import com.job.tracker.branch.Branch;
import com.job.tracker.department.Department;
import com.system.db.entity.named.NamedEntity;
import com.system.security.user.SystemSecurityUser;
import com.system.util.string.StringUtils;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>Employee</class> represents
 * an employee of this company
 *
 * @author Andrew Popp
 */
public class Employee extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Linked from ActiveDirectory SamAccountName
     */
    @ManyToOne
    @JoinColumn(name = "system_security_user_id")
    private SystemSecurityUser systemSecurityUser;

    private String company;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private String emailAddress;

    //GivenName,Surname,Title,
    private String firstName;
    private String lastName;
    private String jobTitle;

    // StreetAddress,City,State,PostalCode,Country,
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    //OfficePhone,MobilePhone,
    private String businessPhone;
    private String mobilePhone;

    //Manager
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    //Enabled,PasswordExpired
    private Boolean enabled;
    private Boolean passwordExpired;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public Employee() {
    }

    @Override
    public String toString() {
        return "Employee{" +
                "systemSecurityUser=" + systemSecurityUser +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", company='" + company + '\'' +
                ", branch=" + branch +
                ", department='" + department + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", country='" + country + '\'' +
                ", businessPhone='" + businessPhone + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", manager=" + manager +
                ", enabled=" + enabled +
                ", passwordExpired=" + passwordExpired +
                '}';
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                          Override Getter/Setters                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public String getName() {
        String name = super.getName();

        if (StringUtils.isEmpty(name)) {
            name = getFirstName() + " " + getLastName();
            super.setName(name);
        }

        return name;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        setName(firstName + " " + getLastName());
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        setName(getFirstName() + " " + lastName);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemSecurityUser getSystemSecurityUser() {
        return systemSecurityUser;
    }

    public void setSystemSecurityUser(SystemSecurityUser systemSecurityUser) {
        this.systemSecurityUser = systemSecurityUser;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getPasswordExpired() {
        return passwordExpired;
    }

    public void setPasswordExpired(Boolean passwordExpired) {
        this.passwordExpired = passwordExpired;
    }
}
