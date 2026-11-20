package cn.edu.payroll;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工资台账：Employee 为键、Salary 为值，封装四项业务。
 * 只负责数据与业务规则，输入输出交给 PayrollApp。
 */
public class PayrollBook {

    /** 键唯一：Employee 的 equals / hashCode 均按工号判断 */
    private final Map<Employee, Salary> ledger = new HashMap<>();

    public Map<Employee, Salary> getLedger() {
        return ledger;
    }

    public int size() {
        return ledger.size();
    }

    /** 初始数据，与课堂示例不同的一组员工 */
    public void loadSample() {
        enroll(new Employee("EMP-1001", "陈嘉禾", 24), new Salary(8600.0));
        enroll(new Employee("EMP-1002", "林知远", 27), new Salary(10200.0));
        enroll(new Employee("EMP-1003", "苏沐晴", 31), new Salary(6800.0));
        enroll(new Employee("EMP-1004", "周允诚", 29), new Salary(12500.0));
    }

    /** 业务1：登记员工工资，工号已存在则拒绝 */
    public boolean enroll(Employee staff, Salary pay) {
        if (staff == null || pay == null || ledger.containsKey(staff)) {
            return false;
        }
        ledger.put(staff, pay);
        return true;
    }

    /** 遍历 keySet()，按工号找到 Map 中真正的键对象 */
    public Employee locate(String empId) {
        for (Employee key : ledger.keySet()) {
            if (key.getEmpId().equals(empId)) {
                return key;
            }
        }
        return null;
    }

    /** 业务2：按工号删除，查无此人返回 false */
    public boolean dismiss(String empId) {
        Employee target = locate(empId);
        return target != null && ledger.remove(target) != null;
    }

    /** 业务3：按工号涨薪，返回更新后的工资；查无此人返回 null */
    public Salary giveRaise(String empId, double increment) {
        Employee target = locate(empId);
        if (target == null) {
            return null;
        }
        Salary pay = ledger.get(target);
        pay.raise(increment);
        return pay;
    }

    /** 业务4-1：entrySet 转 List，用 Comparator 降序 */
    public List<Map.Entry<Employee, Salary>> rankByPayDesc() {
        List<Map.Entry<Employee, Salary>> rows = new ArrayList<>(ledger.entrySet());
        rows.sort((left, right) -> Double.compare(right.getValue().getMonthlyPay(),
                left.getValue().getMonthlyPay()));
        return rows;
    }

    /** 业务4-2：Stream 写法，结果与写法一相同 */
    public List<Map.Entry<Employee, Salary>> rankByPayDescWithStream() {
        return ledger.entrySet().stream()
                .sorted(Comparator.comparingDouble(
                        (Map.Entry<Employee, Salary> row) -> row.getValue().getMonthlyPay()).reversed())
                .collect(Collectors.toList());
    }
}
