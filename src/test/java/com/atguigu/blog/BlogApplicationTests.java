package com.atguigu.blog;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class BlogApplicationTests {

    @Test
    void contextLoads() {
        // 1、创建代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 2、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("wujie");
        gc.setOpen(false); //生成后是否打开资源管理器
        gc.setFileOverride(false); //重新生成时文件是否覆盖
        gc.setServiceName("%sService");	//去掉Service接口的首字母I
        gc.setIdType(IdType.ID_WORKER_STR); //主键策略
        gc.setDateType(DateType.ONLY_DATE);//定义生成的实体类中日期类型
        gc.setSwagger2(true);//开启Swagger2模式

        mpg.setGlobalConfig(gc);

        // 3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/blog");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 4、包配置
        PackageConfig pc = new PackageConfig();
//		pc.setModuleName("admin"); //模块名
        pc.setParent("com.atguigu.blog");
        pc.setController("controller");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 5、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("t" + "_\\w*");//设置要映射的表名
        strategy.setNaming(NamingStrategy.underline_to_camel);//数据库表映射到实体的命名策略

        strategy.setColumnNaming(NamingStrategy.underline_to_camel);//数据库表字段映射到实体的命名策略
        strategy.setEntityLombokModel(true); // lombok 模型 @Accessors(chain = true) setter链式操作

//		strategy.setLogicDeleteFieldName("is_deleted");//逻辑删除字段名
//		strategy.setEntityBooleanColumnRemoveIsPrefix(true);//去掉布尔值的is_前缀

        //自动填充
//		TableFill gmtCreate = new TableFill("gmt_create", FieldFill.INSERT);
//		TableFill gmtModified = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
//		ArrayList<TableFill> tableFills = new ArrayList<>();
//		tableFills.add(gmtCreate);
//		tableFills.add(gmtModified);
//		strategy.setTableFillList(tableFills);

        strategy.setVersionFieldName("version");//乐观锁列

        strategy.setRestControllerStyle(true); //restful api风格控制器
        strategy.setControllerMappingHyphenStyle(true); //url中驼峰转连字符

        mpg.setStrategy(strategy);

        // 6、执行
        mpg.execute();
    }

    @Test
    public void test(){
        ListNode node=new ListNode(4);
        node.next=new ListNode(19);
        node.next.next=new ListNode(14);
        node.next.next.next=new ListNode(5);
        node.next.next.next.next=new ListNode(3);
//        int[] nums={1,1,2,2,2,3};
//        System.out.println(frequencySort(nums));
        int[][] arr={{4,3,2,-1},{3,2,1,-1},{1,1,-1,-2}};
        int[] nums = {3,3,1,2,2,-1};
        System.out.println(findLucky(nums));
    }

    @Test
    public int findLucky(int[] arr) {
        int ans=-1;
        Map<Integer,Integer> map=new HashMap<>();
        for (int i : arr) {
            map.put(i,map.getOrDefault(i,0)+1);
        }
        //选出幸运数
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if(entry.getKey()==entry.getValue()){
                ans=entry.getKey();
            }
        }
        return ans;
    }

    @Test
    public int[] frequencySort(int[] nums) {
        int[] ans=new int[nums.length];
        Map<Integer,Integer> map=new HashMap<>();
        for (int num : nums) {
            map.put(num,map.getOrDefault(num,0)+1);
        }
        //根究value升序
        Map<Integer,Integer> valueMap=new LinkedHashMap <>();
        map.entrySet().stream().sorted(Map.Entry.comparingByValue())
                .forEachOrdered(b->valueMap.put(b.getKey(),b.getValue()));
        int index=0;
        for (Map.Entry<Integer, Integer> entry : valueMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                ans[index++]=entry.getKey();
            }
        }
        Set<Integer> set=new HashSet<>();
        return ans;
    }

    class ListNode {
      int val;
      ListNode next;
      ListNode() {}
     ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
  }


}
