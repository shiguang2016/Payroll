# 员工工资管理系统（HashMap 键值对版）

课程实验：以自定义对象 `Employee` 为键、`Salary` 为值，用 `HashMap` 完成员工工资的登记、删除、涨薪与按工资降序遍历。

> 示例数据、变量名、方法名均与课堂示例不同，可直接作为独立作业提交。工程只保留 4 个类、约 340 行。

## 一、目录结构

```
Salary/
├─ src/cn/edu/payroll/
│  ├─ Employee.java     键：empId / empName / empAge，按工号重写 equals、hashCode
│  ├─ Salary.java       值：monthlyPay，含 raise(double) 涨薪
│  ├─ PayrollBook.java  业务层：初始数据 + 四项业务 + 两种降序写法
│  └─ PayrollApp.java   入口：无参数进菜单，demo 参数跑固定演示
├─ bin/                 Eclipse 编译输出
├─ .classpath / .project
├─ README.md
├─ 实验报告.md
└─ 实验代码_手写版.md     按类分页的抄写稿，4 个类 196 行
```

需要把代码誊抄到实验报告册时，直接用 `实验代码_手写版.md`：行宽控制在 80 字符内、每类另起一页、附手写易错点，该版本已单独抽取编译验证。

## 二、在 Eclipse 中运行

1. `File → Import → General → Existing Projects into Workspace`，选择本目录 `Salary`。
2. 若提示 JDK 版本不符：右键项目 → `Properties → Java Build Path → Libraries`，把 JRE System Library 改成本机 JDK（工程按 Java 17 编译，源码兼容 Java 8）。
3. 右键 `PayrollApp.java` → `Run As → Java Application`：
   - 直接运行：菜单交互，涨薪额度键盘输入；
   - `Run Configurations → Arguments` 填入 `demo`：一次性输出四项业务的实验结果。

## 三、命令行运行（Windows）

```bat
cd /d C:\Users\Lenovo\eclipse-workspace\Salary
javac -encoding UTF-8 -d bin src\cn\edu\payroll\*.java
java -cp bin cn.edu.payroll.PayrollApp
java -cp bin cn.edu.payroll.PayrollApp demo
```

命令提示符若中文乱码，先执行 `chcp 65001`；Eclipse 控制台一般无需处理。

## 四、四项业务与代码对应

| 业务 | 方法 | 关键点 |
| --- | --- | --- |
| 1. 登记并保证工号唯一 | `PayrollBook.enroll` | `containsKey` 依赖 `hashCode` + `equals` |
| 2. 按工号删除 | `PayrollBook.dismiss` | 先用 `keySet()` 定位真正的键，再 `remove(key)` |
| 3. 按工号涨薪 | `PayrollBook.giveRaise` | `get(key)` 取值后调用 `Salary.raise` |
| 4. 按工资降序 | `rankByPayDesc` / `rankByPayDescWithStream` | Map 转 List 后排序；Stream 写法对照 |

## 五、说明

- `Employee` 只把 `empId` 纳入 `equals` / `hashCode`：姓名可重名、年龄会变，工号唯一且稳定。
- 键对象放入 `HashMap` 后不要修改 `empId`，否则哈希值变化会导致原记录查不到。
- `Salary` 作为值不要求唯一，无需重写 `equals` / `hashCode`。
