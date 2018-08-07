package com.maxcheung.camelsimple.beanio;

import java.util.Date;

public class Employee {

    private String firstName;
    private String lastName;
    private String title;
    private int salary;
    private Date hireDate;

    public Employee() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + salary;
        result = 31 * result + (hireDate != null ? hireDate.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (object == this) {
            return true;
        } else if (!(object instanceof Employee)) {
            return false;
        }

        Employee e = (Employee) object;

        return this.getFirstName().equals(e.getFirstName())
                && this.getLastName().equals(e.getLastName())
                && this.getTitle().equals(e.getTitle())
                && this.getSalary() == e.getSalary()
                && this.getHireDate().equals(e.getHireDate());
    }

    @Override
    public String toString() {
        return "Employee{"
                + "firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", title='" + title + '\''
                + ", salary=" + salary
                + ", hireDate=" + hireDate
                + '}';
    }
}