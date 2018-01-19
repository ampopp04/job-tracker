package com.job.tracker.customer;


import com.system.db.entity.named.NamedEntity;

import javax.persistence.Column;

/**
 * The <class>Customer</class> represents
 * a customer for this company
 *
 * @author Andrew Popp
 */
public class Customer extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Unique identifier that this company uses to identify their customers
     * across their various systems
     */
    @Column(unique = true)
    private String customerNumber;

    /**
     * The status of this customer
     * Active/Inactive
     */
    private String status;

    /**
     * Various root address placeholders
     */
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;

    /**
     * Location information
     */
    private String city;
    private String state;

    private String zipCode;

    /**
     * Contact information
     */
    private String phoneNumber;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public Customer() {
    }

    public static Customer newBasicInstance(String name) {
        Customer customer = new Customer();

        return customer;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
