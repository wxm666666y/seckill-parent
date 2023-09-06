import com.bmw.seckill.SeckillAdminApplication;
import com.bmw.seckill.model.SeckillAdmin;
import com.bmw.seckill.security.AdminUser;
import com.bmw.seckill.security.MyUserDetailsService;
import com.bmw.seckill.service.impl.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SeckillAdminApplication.class)
@Slf4j
public class DemoTest {

	@Autowired
	MyUserDetailsService myUserDetailsService;

	@Autowired
	AdminService adminService;

	@Test
	public void test() throws InvocationTargetException, IllegalAccessException {
//		System.out.println(myUserDetailsService.loadUserByUsername("zhangsan"));
//		System.out.println(adminService.findByUsername("zhangsan"));
		SeckillAdmin seckillAdmin = adminService.findByUsername("zhangsan");
		AdminUser adminUser = new AdminUser();
		BeanUtils.copyProperties(adminUser, seckillAdmin);
		System.out.println(adminUser);
		System.out.println("=================================");
		System.out.println(seckillAdmin);
	}
}
