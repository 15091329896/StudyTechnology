import java.text.DecimalFormat;

public class TestRegex {
	public static void main(String[] args) {

		// new TestRegex().test();
//	/new TestRegex().test2();
		
		System.out.println(1024 *1024 *100);
		
		

	}

	/**
	 * 
	* @Title: test2 
	* @Description: TODO void
	* @throws
	* @author: dyk
	* @date 2020-07-07 14:36:31
	 */
	private void test2() {
		double result1 = 0.51111122111111;
		DecimalFormat df = new DecimalFormat("0.00%");
		String r = df.format(result1);
		System.out.println(r);// great
	}

	/**
	 * 
	* @Title: test 
	* @Description: TODO void
	* @throws
	* @author: dyk
	* @date 2020-07-07 14:35:30
	 */
	private void test() {
		String string = "192.168.8.2";
		System.out.println(string.matches("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+"));
	}

}