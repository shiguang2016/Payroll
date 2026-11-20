package cn.edu.payroll;

import java.util.Objects;

/**
 * 员工类，作为 Map 的键。
 * 工号唯一且不变，因此只用 empId 参与 equals / hashCode。
 */
public class Employee {

    private String empId;
    private String empName;
    private int empAge;

    public Employee() {
    }

    public Employee(String empId, String empName, int empAge) {
        this.empId = empId;
        this.empName = empName;
        this.empAge = empAge;
    }

    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }
    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }
    public int getEmpAge() { return empAge; }
    public void setEmpAge(int empAge) { this.empAge = empAge; }

    /** 工号相同即视为同一个人，与姓名、年龄无关 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Employee)) {
            return false;
        }
        return Objects.equals(empId, ((Employee) obj).empId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId);
    }

    @Override
    public String toString() {
        return empId + " | " + empName + " | " + empAge + "岁";
    }
}
