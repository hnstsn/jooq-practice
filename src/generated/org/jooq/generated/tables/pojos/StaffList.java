/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.pojos;


import java.io.Serializable;


/**
 * VIEW
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class StaffList implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String address;
    private String zipCode;
    private String phone;
    private String city;
    private String country;
    private Long sid;

    public StaffList() {}

    public StaffList(StaffList value) {
        this.id = value.id;
        this.name = value.name;
        this.address = value.address;
        this.zipCode = value.zipCode;
        this.phone = value.phone;
        this.city = value.city;
        this.country = value.country;
        this.sid = value.sid;
    }

    public StaffList(
        Long id,
        String name,
        String address,
        String zipCode,
        String phone,
        String city,
        String country,
        Long sid
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.zipCode = zipCode;
        this.phone = phone;
        this.city = city;
        this.country = country;
        this.sid = sid;
    }

    /**
     * Getter for <code>sakila.staff_list.ID</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>sakila.staff_list.ID</code>.
     */
    public StaffList setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Getter for <code>sakila.staff_list.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>sakila.staff_list.name</code>.
     */
    public StaffList setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>sakila.staff_list.address</code>.
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Setter for <code>sakila.staff_list.address</code>.
     */
    public StaffList setAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * Getter for <code>sakila.staff_list.zip code</code>.
     */
    public String getZipCode() {
        return this.zipCode;
    }

    /**
     * Setter for <code>sakila.staff_list.zip code</code>.
     */
    public StaffList setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    /**
     * Getter for <code>sakila.staff_list.phone</code>.
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * Setter for <code>sakila.staff_list.phone</code>.
     */
    public StaffList setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    /**
     * Getter for <code>sakila.staff_list.city</code>.
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Setter for <code>sakila.staff_list.city</code>.
     */
    public StaffList setCity(String city) {
        this.city = city;
        return this;
    }

    /**
     * Getter for <code>sakila.staff_list.country</code>.
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * Setter for <code>sakila.staff_list.country</code>.
     */
    public StaffList setCountry(String country) {
        this.country = country;
        return this;
    }

    /**
     * Getter for <code>sakila.staff_list.SID</code>.
     */
    public Long getSid() {
        return this.sid;
    }

    /**
     * Setter for <code>sakila.staff_list.SID</code>.
     */
    public StaffList setSid(Long sid) {
        this.sid = sid;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final StaffList other = (StaffList) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.address == null) {
            if (other.address != null)
                return false;
        }
        else if (!this.address.equals(other.address))
            return false;
        if (this.zipCode == null) {
            if (other.zipCode != null)
                return false;
        }
        else if (!this.zipCode.equals(other.zipCode))
            return false;
        if (this.phone == null) {
            if (other.phone != null)
                return false;
        }
        else if (!this.phone.equals(other.phone))
            return false;
        if (this.city == null) {
            if (other.city != null)
                return false;
        }
        else if (!this.city.equals(other.city))
            return false;
        if (this.country == null) {
            if (other.country != null)
                return false;
        }
        else if (!this.country.equals(other.country))
            return false;
        if (this.sid == null) {
            if (other.sid != null)
                return false;
        }
        else if (!this.sid.equals(other.sid))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.address == null) ? 0 : this.address.hashCode());
        result = prime * result + ((this.zipCode == null) ? 0 : this.zipCode.hashCode());
        result = prime * result + ((this.phone == null) ? 0 : this.phone.hashCode());
        result = prime * result + ((this.city == null) ? 0 : this.city.hashCode());
        result = prime * result + ((this.country == null) ? 0 : this.country.hashCode());
        result = prime * result + ((this.sid == null) ? 0 : this.sid.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("StaffList (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(address);
        sb.append(", ").append(zipCode);
        sb.append(", ").append(phone);
        sb.append(", ").append(city);
        sb.append(", ").append(country);
        sb.append(", ").append(sid);

        sb.append(")");
        return sb.toString();
    }
}
