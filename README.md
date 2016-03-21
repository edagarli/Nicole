# Nicole

一款轻量级的开发框架(其实这个框架就是为了减少开发时间,更多时间陪女朋友,陪家人,我自己用的一个框架)

>可能有人说前辈们开发的框架已经很成熟了

>但我觉得能为开源社区贡献一点力量,同时体验这个从0到1过程很快乐

>我喜欢吸收,总结前辈们优秀的思想,尽量把它们融合在一起,所以你会发现这框架可能有其它的框架的影子,也可能某个优秀的部分直接拿来用,超越前辈毕竟不是一件容易事情

>如果有想从0开发框架的人,这个框架可能适合你学习,毕竟我也是从0开始开发

>如果有人想要快速开发项目,这个框架可能适合你学习,因为它就是为了加速开发,尽可能提高生产力,让开发人员将更多的精力集中到业务上

>如果有人想要了解更多框架,这个框架可能适合你学习,因为它就包含了很多框架的影子

>如果你对这个项目感兴趣,你可以使用 Issues 以及 Pull Request


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

项目本身已经有一个基于DatabaseHelper（Dao层），我觉得使用起来不方便。项目本身额外包含一个可以独立于项目之外可以单独使用的Dao层，见Dao包。所以引发了模块化设计的思考，每层都可以单独拿出来使用。这个可能需要等待一段时间再折腾出来。

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
        <!-- 注册MiniDao接口 -->
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

