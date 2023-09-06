import com.bmw.seckill.SeckillWebApplication;
import com.bmw.seckill.dao.SeckillUserDao;
import com.bmw.seckill.model.SeckillAdmin;
import com.bmw.seckill.model.SeckillUser;
import com.bmw.seckill.service.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SeckillWebApplication.class)
@Slf4j
public class DemoTest {

	@Autowired
	private SeckillUserDao seckillUserDao;

	@Autowired
	private IAdminService adminService;

	@Test
	public void test() {
//		SeckillUser seckillUser = new SeckillUser();
//		seckillUser.setPhone("123");
//		System.out.println(seckillUserDao.list(seckillUser));
		seckillUserDao.selectByPrimaryKey(Long.valueOf("1"));
	}

	@Test
	public void test2() {
		for ( SeckillAdmin s : adminService.listAdmin() ) {
			System.out.println(s.getIpRange());
			System.out.println(s);
		}

	}
}
