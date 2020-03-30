package com.cscec;

import java.io.IOException;
import java.util.Base64;

import com.example.demo.system.mapper.CommonMapper;
import com.example.demo.system.service.CommonService;
import com.example.demo.system.service.UserService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

@SpringBootTest
class CscecApplicationTests {

	@Test
	void contextLoads() throws IOException {
//		System.out.println(new Date());
//		String fileName="C:\\Users\\29106\\Documents\\Tencent Files\\848378295\\FileRecv\\J5合页立面.dwg";
//		 FileInputStream input = new FileInputStream(fileName);
//		    byte[] buffer = new byte[6];
//		    int len = input.read(buffer);
//		    String str = new String(buffer);
//		    System.out.println(str);
//		    System.out.println(len);
//		    input.close();
//		User user=new User();
//		user.setType(Constant.POWER_super_admin);
//		user.setType(Constant.POWER_normal_admin);
//		user.setType(Constant.POWER_normal_user);
//		user.setId(1001);
//		String [] ids="1,2,3,4,5,6,7,8,9".split(",");
// 		service.selectPathInId(ids,user);
//	List list=commonMapper.executeSql("SELECT  u.real_name name,count(0) count from  xcmt x left join  user u  on  x.user_id = u.id where   x.company_gs =3 GROUP BY  x.user_id  ");

//		String sql="SELECT  u.real_name name,count(0) count from  xcmt x left join  user u  on  x.user_id = u.id where   x.company_gs =3 GROUP BY  x.user_id  ";
//		Object s= controller.mttypeAnalysis(false);
//		System.out.println(s);
//		System.out.println(JSON.toJSONString(list));

//		SystemConfig config= commonService.selectSysConfigByCode(Constant.Sysconfig.userTimedDeletion);
//		System.out.println(JSON.toJSONString(config));
//		List<User>  users=userService.selectCanDel(7);
//		System.out.println(JSON.toJSONString(users));
//		SpringUtil.getBean(Scheduler.class).applicationInit();
//		SpringUtil.getBean(Scheduler.class).delUser();
//		int userId=1018;
//		int i=SpringUtil.getBean(UserServiceImpl.class).getXcmtCountByUserId(userId);
//		logger.info("count:"+i);

		//压缩图片
//		Thumbnails.of("D:/a/timg.jpg").size(300, 1).toFile("D:/a/timg2.jpg");


		//计算宽高
//		String imgFile="D:/a/timg.jpg";
//		BufferedImage bim = ImageIO.read(new File(imgFile));
//		int imgWidth = bim.getWidth();
//		int imgHeight = bim.getHeight();
//
//		int width=200;
//		if(imgWidth >width){
////			BigDecimal scale=new BigDecimal(imgWidth).divide(new BigDecimal(width),4,RoundingMode.DOWN);
//// 		    int desHeight = new BigDecimal(imgHeight).divide(scale,1, RoundingMode.DOWN).intValue();
////			Thumbnails.of(imgFile).size(width, width).toFile("D:/a/timg3.jpg");
//			Thumbnails.of(imgFile).size(imgWidth, imgWidth).toFile("D:/a/timg2.jpg");
//		}else{//复制一份
//			Thumbnails.of(imgFile).size(imgWidth, imgHeight).toFile("D:/a/timg2.jpg");
//		}

	}

	/**
	 *
	 * @param key 秘钥
	 * @param text 需要加密的数据
	 * @return
	 * @throws Exception
	 * 简单了解下 ： DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。DES加密算法出自IBM的研究，
	 * 后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，较为容易破解。
	 */
	public static String encrypt(String key,String text) throws  Exception {
		try {
			byte[] src = text.getBytes("utf-8");
			//DESedeKeySpec会帮你生成24位秘钥，key可以是任意长度
			DESedeKeySpec spec = new DESedeKeySpec(key.getBytes("utf-8"));
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
			SecretKey secretKey = factory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] res = cipher.doFinal(src);
			//encodeBase64会对字符串3位一组自动补全，因而最后可能会出现 == 或者 =
			return new String(Base64.getEncoder().encode(res), "utf-8");
		} catch (Exception e) {
			System.out.println("error");
		}
		return null;
	}

	@Autowired
      private JdbcTemplate jdbcTemplate;
	@Autowired
	CommonMapper commonMapper;

	@Autowired
	CommonService commonService;

	@Autowired
	UserService userService;


	  Logger logger= LoggerFactory.getLogger(this.getClass());
}
