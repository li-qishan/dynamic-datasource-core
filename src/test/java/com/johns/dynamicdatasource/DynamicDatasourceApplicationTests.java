package com.johns.dynamicdatasource;

import com.johns.dynamicdatasource.entity.Demo;
import com.johns.dynamicdatasource.datasource.mappers.DemoMapper;
import com.johns.dynamicdatasource.service.depotItem.DepotItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
class DynamicDatasourceApplicationTests {


	@Autowired
	DemoMapper demoMapper;

	@Resource
	private DepotItemService depotItemService;

	@Test
	void contextLoads() {
	}


	@Test
	void slaveTest() {
		List<Demo> demos = demoMapper.selectAll();
		demos.forEach(System.out::println);
	}


	@Test
	void slaveTest1() {
		BigDecimal stockByParam = depotItemService.getStockByParam(new Long(14), new Long(588), null, null, new Long(63));
		System.out.println(stockByParam);
	}

}
