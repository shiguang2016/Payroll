package cn.edu.payroll;

/**
 * 工资类，作为 Map 的值。值不要求唯一，无需重写 equals / hashCode。
 */
public class Salary {

    private double monthlyPay;

    public Salary(double monthlyPay) {
        setMonthlyPay(monthlyPay);
    }

    public double getMonthlyPay() {
        return monthlyPay;
    }

    public void setMonthlyPay(double monthlyPay) {
        if (monthlyPay < 0) {
            throw new IllegalArgumentException("工资不能为负数：" + monthlyPay);
        }
        this.monthlyPay = monthlyPay;
    }

    /** 涨薪：在原工资基础上增加 amount 元，amount 必须大于 0 */
    public void raise(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("涨薪额度必须大于 0：" + amount);
        }
        this.monthlyPay += amount;
    }

    @Override
    public String toString() {
        return String.format("%.2f 元", monthlyPay);
    }
}
