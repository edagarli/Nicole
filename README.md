# Nicole

>只是用来理解MVC原理练手的，如果用在生产环境的话，最好慎重下。

## 使用说明

整个项目核心是DispatcherServlet与HelperLoader

DispatcherServlet主要处理Request，Response分发，其实就是Servlet核心枢纽
HelperLoader用于项目启动时候加载到内存，包含:
	 
 * ClassHelper
 * BeanHelper
 * AopHelper
 * IocHelper
 * ControllerHelper

顾名思义，就知道啥意思。

项目本身已经有一个简单的DatabaseHelper（Dao层）。不过为了实践下类似mybatis的SQL怎么去搞的，项目本身额外也包含了一个可以独立于项目之外可以单独使用的Dao层，见Dao包。

###  独立的Dao层使用说明

轻量级持久层解决方案，类Mybatis的SQL方式，事务统一管理。 具有以下特征:

* 1.O/R mapping不用设置xml，零配置便于维护
* 2.不需要了解JDBC的知识
* 3.SQL语句和java代码的分离
* 4.可以自动生成SQL语句
* 5.接口和实现分离，不用写持久层代码，用户只需写接口，以及某些接口方法对应的sql 它会通过AOP自动生成实现类
* 6.支持自动事务处理和手动事务处理
* 7.支持与hibernate轻量级无缝集成
* 8.整合了Hibernate+mybatis的两大优势，支持实体维护和SQL分离
* 9.SQL支持脚本语言

### 接口定义[EmployeeDao.java]  
    @Dao
    public interface EmployeeDao {
     @Arguments("employee")
     public List<Map> getAllEmployees(Employee employee);
    
     @Arguments("empno")
     Employee getEmployee(String empno);
    
     @Arguments({"empno","name"})
     Map getMap(String empno,String name);

     @Sql("SELECT count(*) FROM employee")
     Integer getCount();

     @Arguments("employee")
     int update(Employee employee);

     @Arguments("employee")
     void insert(Employee employee);
   }
    
### SQL文件[EmployeeDao_getAllEmployees.sql]
    SELECT * FROM employee where 1=1 
    <#if employee.age ?exists>
	and age = :employee.age
    </#if>
    <#if employee.name ?exists>
	and name = :employee.name
    </#if>
    <#if employee.empno ?exists>
	and empno = :employee.empno
    </#if>

### Dao接口配置
	<bean class="org.edagarli.framework.dao.factory.MiniDaoBeanFactory">
		<property name="packagesToScan">
			<list>
				<value>examples.dao.*</value>
			</list>
		</property>
	</bean>

### 测试代码
    public class Client {
    public static void main(String args[]) {
		BeanFactory factory = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
     		
		EmployeeDao employeeDao = (EmployeeDao) factory.getBean("employeeDao");
		Employee employee = new Employee();
		List<Map> list =  employeeDao.getAllEmployees(employee);
		for(Map mp:list){
			System.out.println(mp.get("id"));
			System.out.println(mp.get("name"));
			System.out.println(mp.get("empno"));
			System.out.println(mp.get("age"));
			System.out.println(mp.get("birthday"));
			System.out.println(mp.get("salary"));
		}
	  }
    }

