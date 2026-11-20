package cn.edu.payroll;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 控制台入口：无参数进入菜单，带参数 demo 运行固定演示。
 */
public class PayrollApp {

    private static final Scanner INPUT = new Scanner(System.in);
    private static final PayrollBook BOOK = new PayrollBook();

    public static void main(String[] args) {
        BOOK.loadSample();
        if (args.length > 0 && "demo".equalsIgnoreCase(args[0])) {
            runDemo();
        } else {
            runMenu();
        }
        INPUT.close();
    }

    /** 固定脚本依次执行四项业务，输出实验结果 */
    private static void runDemo() {
        System.out.println("========== 员工工资管理演示（HashMap 键值对） ==========");
        printRows("[初始数据]", BOOK.rankByPayDesc());

        System.out.println("\n[业务1] 登记员工，工号不能重复");
        System.out.println("  登记 EMP-1005 顾清和 → "
                + (BOOK.enroll(new Employee("EMP-1005", "顾清和", 26), new Salary(9300.0)) ? "成功" : "失败"));
        System.out.println("  用新对象再登记 EMP-1005 → "
                + (BOOK.enroll(new Employee("EMP-1005", "顾清和", 26), new Salary(9999.0)) ? "成功" : "失败：工号已存在"));
        System.out.println("  当前人数：" + BOOK.size());

        System.out.println("\n[业务2] 按工号删除员工");
        System.out.println("  删除 EMP-1008 → " + (BOOK.dismiss("EMP-1008") ? "成功" : "失败：查无此工号"));
        System.out.println("  删除 EMP-1003 苏沐晴 → " + (BOOK.dismiss("EMP-1003") ? "成功" : "失败：查无此工号"));
        System.out.println("  当前人数：" + BOOK.size());

        System.out.println("\n[业务3] 按工号涨薪");
        Employee target = BOOK.locate("EMP-1002");
        Salary updated = BOOK.giveRaise("EMP-1002", 1200.0);
        System.out.println("  EMP-1002 " + target.getEmpName() + " 涨 1200.00 元 → 成功，现为 " + updated);
        System.out.println("  EMP-1007 涨 500.00 元 → "
                + (BOOK.giveRaise("EMP-1007", 500.0) == null ? "失败：查无此工号" : "成功"));

        System.out.println("\n[业务4] 按工资从高到低");
        printRows("写法一（entrySet → List → Comparator）：", BOOK.rankByPayDesc());
        printRows("写法二（Stream 排序）：", BOOK.rankByPayDescWithStream());
        System.out.println("两种写法结果一致："
                + BOOK.rankByPayDesc().equals(BOOK.rankByPayDescWithStream()));

        System.out.println("\n[遍历方式]");
        showKeysAndTotal();
        System.out.println("\n========== 演示结束 ==========");
    }

    /** 菜单模式：四项业务循环执行，涨薪额度由键盘输入 */
    private static void runMenu() {
        System.out.println("员工工资管理系统（HashMap 版），当前 " + BOOK.size() + " 条记录。");
        boolean running = true;
        while (running) {
            System.out.println("\n1.登记  2.删除  3.涨薪  4.降序查看  0.退出");
            System.out.print("请选择：");
            switch (INPUT.nextLine().trim()) {
                case "1":
                    addEmployee();
                    break;
                case "2":
                    System.out.print("要删除的工号：");
                    String removeId = INPUT.nextLine().trim();
                    System.out.println(BOOK.dismiss(removeId) ? "删除成功：" + removeId : "删除失败：查无工号 " + removeId);
                    break;
                case "3":
                    raiseSalary();
                    break;
                case "4":
                    printRows("按工资从高到低（entrySet）：", BOOK.rankByPayDesc());
                    showKeysAndTotal();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("没有这个选项。");
            }
        }
        System.out.println("已退出。");
    }

    private static void addEmployee() {
        System.out.print("工号：");
        String empId = INPUT.nextLine().trim();
        if (empId.isEmpty() || BOOK.locate(empId) != null) {
            System.out.println("登记失败：工号为空或已存在。");
            return;
        }
        System.out.print("姓名：");
        String empName = INPUT.nextLine().trim();
        System.out.print("年龄：");
        int empAge = readInt();
        System.out.print("月薪：");
        double monthlyPay = readDouble();
        if (empName.isEmpty() || empAge <= 0 || monthlyPay <= 0) {
            System.out.println("输入不合法，登记取消。");
            return;
        }
        boolean ok = BOOK.enroll(new Employee(empId, empName, empAge), new Salary(monthlyPay));
        System.out.println(ok ? "登记成功。" : "登记失败。");
    }

    private static void raiseSalary() {
        System.out.print("要涨薪的工号：");
        String empId = INPUT.nextLine().trim();
        Employee target = BOOK.locate(empId);
        if (target == null) {
            System.out.println("操作失败：查无工号 " + empId + "。");
            return;
        }
        System.out.println("当前员工：" + target);
        System.out.print("涨薪额度（元）：");
        double increment = readDouble();
        if (increment <= 0) {
            System.out.println("涨薪额度必须大于 0，操作取消。");
            return;
        }
        System.out.println("涨薪成功，当前月薪：" + BOOK.giveRaise(empId, increment));
    }

    /** 打印排序结果，同时演示 keySet() 与 values() */
    private static void printRows(String title, List<Map.Entry<Employee, Salary>> rows) {
        System.out.println(title);
        for (int i = 0; i < rows.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + rows.get(i).getKey() + " | " + rows.get(i).getValue());
        }
    }

    private static void showKeysAndTotal() {
        System.out.print("keySet()：");
        for (Employee staff : BOOK.getLedger().keySet()) {
            System.out.print(staff.getEmpId() + "  ");
        }
        double total = 0;
        for (Salary pay : BOOK.getLedger().values()) {
            total += pay.getMonthlyPay();
        }
        System.out.printf("%nvalues() 月薪合计：%.2f 元%n", total);
    }

    private static int readInt() {
        try {
            return Integer.parseInt(INPUT.nextLine().trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private static double readDouble() {
        try {
            return Double.parseDouble(INPUT.nextLine().trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
}
