package cn.book;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * 图书借阅系统
 * @author hezhao
 * @Time   2016年9月9日 下午2:13:42
 * @Description 无
 * @Version V 1.0
 */
public class BookManager implements Serializable{

	//将不需要序列化的属性前添加关键字，序列化对象的时候，这个属性就不会序列化到指定的目的地中
	private static final long serialVersionUID = 4344539042076684746L;
	
	//用户列表
	List<User> users = new ArrayList<>();
	//图书列表
	List<Book> books = new ArrayList<>();
	//借阅列表
	List<Borrow> borrows = new ArrayList<>();
	//当前登录用户
	User user = null;

	
	public static void main(String[] args) {
		//先从文件中读取数据
		final BookManager manager = readObjectFromFile();
		
		manager.startMenu();
		
		Runtime run=Runtime.getRuntime();//当前 Java 应用程序相关的运行时对象。  
        run.addShutdownHook(new Thread(){ //注册新的虚拟机来关闭钩子  
            @Override  
            public void run() {  
                //程序结束时进行的操作  
        		writeObjectToFile(manager);//将对象写入到文件中
            }  
        });  
	}
	
	
	//------[静态代码块]是在类加载时自动执行的，[非静态代码块]是在创建对象时自动执行的代码，不创建对象不执行该类的非静态代码块。
	
	/**
	 * 初始化
	 * @author hezhao
	 * @Time   2016年9月9日 下午6:18:55
	 */
	public void initial(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//初始化图书
		Book book1 = new Book();
		book1.setBookId("BN1");
		book1.setBookName("少年漂流奇幻记");
		book1.setAuthor("李安");
		book1.setPrice(1);
		
		Book book2 = new Book();
		book2.setBookId("BN2");
		book2.setBookName("解忧杂货店");
		book2.setAuthor("东野圭吾");
		book2.setPrice(1.8);
		
		Book book3 = new Book();
		book3.setBookId("BN3");
		book3.setBookName("追风筝的人");
		book3.setAuthor("马克·福斯特");
		book3.setPrice(0.5);
		
		Book book4 = new Book();
		book4.setBookId("BN4");
		book4.setBookName("如果巴黎不快乐");
		book4.setAuthor("白槿湖");
		book4.setPrice(1.2);
		
		Book book5 = new Book();
		book5.setBookId("BN5");
		book5.setBookName("灵域");
		book5.setAuthor("逆苍天");
		book5.setPrice(2.8);
		
		books.add(book1);
		books.add(book2);
		books.add(book3);
		books.add(book4);
		books.add(book5);
		
		//初始化用户
		User user1 = new User();
		user1.setUsername("admin");
		user1.setPwd("123456");
		
		users.add(user1);
		
		try {
			Borrow borrow1 = new Borrow();
			borrow1.setBorrowId((int)new Date().getTime());
			borrow1.setBookId(book1.getBookId());
			borrow1.setUserName(user1.getUsername());
			borrow1.setBorrowTime(sdf.parse("2016-08-09"));
			borrow1.setBackTime(sdf.parse("2016-09-09"));
			borrow1.setMoney(30);
			
			Borrow borrow2 = new Borrow();
			borrow2.setBorrowId((int)new Date().getTime());
			borrow2.setBookId(book2.getBookId());
			borrow2.setUserName(user1.getUsername());
			borrow2.setBorrowTime(sdf.parse("2016-08-19"));
			borrow2.setBackTime(sdf.parse("2016-09-19"));
			borrow2.setMoney(54);
			
			borrows.add(borrow1);
			borrows.add(borrow2);
			
			book1.setBcount(book1.getBcount()+1);
			book2.setBcount(book2.getBcount()+1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存对象到文件中
	 * @author hezhao
	 * @Time   2016年9月9日 下午6:16:13
	 * @param manager
	 */
	 public static void writeObjectToFile(BookManager manager){
		 File file =new File("data.dat");
         FileOutputStream out;
         try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut=new ObjectOutputStream(out);
            objOut.writeObject(manager);
            objOut.flush();
            objOut.close();
            System.out.println("write object success!");
        } catch (IOException e) {
            System.out.println("write object failed");
            e.printStackTrace();
        }
    }
	 
	 /**
	  * 从文件中读取对象
	  * @author hezhao
	  * @Time   2016年9月9日 下午6:16:05
	  * @return
	  */
	 public static BookManager readObjectFromFile(){
		BookManager temp=new BookManager();
		temp.initial();
		
		File file =new File("data.dat");
		FileInputStream in;
		try {
		    in = new FileInputStream(file);
		    ObjectInputStream objIn=new ObjectInputStream(in);
		    temp=(BookManager) objIn.readObject();
		    objIn.close();
		    System.out.println("read object success!");
		} catch (IOException e) {
		    System.out.println("read object failed");
		    e.printStackTrace();
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}
		return temp;
    }
	
	public void startMenu(){
		boolean flag = true;
		Scanner input = new Scanner(System.in);
		
		do{
			System.out.println("**********欢迎来到图书借阅系统**********");
			System.out.println("请选择菜单：");
			System.out.println("1、注册");
			System.out.println("2、登录");
			System.out.println("3、查看图书列表");
			System.out.println("4、借书");
			System.out.println("5、还书");
			System.out.println("6、借阅排行榜");
			System.out.println("7、查看借阅历史");
			System.out.println("8、新增图书");
			System.out.println("9、删除图书");
			System.out.println("10、退出");
			
			boolean flag2 = false;
			int no = 0;
			do{
				flag2 = false;
				
				String noStr = input.next();
				if(exit(noStr))	return;
				
				try {
					no = Integer.parseInt(noStr);
				} catch (NumberFormatException e) {
					System.out.println("请输入数字！");
					flag2 = true;
				}
			}while(flag2);
			
			//开始判断用户选择的操作
			switch (no) {
			case 1:
				System.out.println("图书借阅系统==>>注册");
				register();
				break;
			case 2:
				System.out.println("图书借阅系统==>>登录");
				user = login();
				break;
			case 3:
				System.out.println("图书借阅系统==>>查看图书");
				bookList();
				break;
			case 4:
				if(!checkLogin()){
					System.out.println("请先登录！");
					break;
				}
				System.out.println("图书借阅系统==>>借出图书");
				borrow();
				break;
			case 5:
				if(!checkLogin()){
					System.out.println("请先登录！");
					break;
				}
				System.out.println("图书借阅系统==>>归还图书");
				back();
				break;
			case 6:
				System.out.println("图书借阅系统==>>借阅排行榜");
				rankingList();
				break;
			case 7:
				if(!checkLogin()){
					System.out.println("请先登录！");
					break;
				}
				System.out.println("图书借阅系统==>>查看借阅历史");
				userInfo();
				break;
			case 8:
				if(!checkLogin()){
					System.out.println("请先登录！");
					break;
				}
				System.out.println("图书借阅系统==>>新增图书");
				addBook();
				break;
			case 9:
				if(!checkLogin()){
					System.out.println("请先登录！");
					break;
				}
				System.out.println("图书借阅系统==>>删除图书");
				delBook();
				break;
			case 10:
				System.out.println("正在退出图书借阅系统......");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("退出成功，感谢使用！");
				flag = false;
				break;
			default:
				System.out.println("请输入正确的序号！");
				break;
			}
			
		}while(flag);
	}
	
	//注册
	private void register(){
		Scanner input = new Scanner(System.in);
		
		boolean flag = false;
		
		do{
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			flag = false;
			
			System.out.println("请输入要注册的用户名：");
			
			String username = input.next();
			if(exit(username))	return;
			
			if(username.isEmpty()){
				System.out.println("用户名不能为空！");
				flag = true;
				continue;
			}
			
			if(users != null){
				User userByName = getUserByName(username);
				
				if(userByName != null){
					System.out.println("用户名已存在，是否重新输入？Y/N");
					
					String word = input.next();
					if(exit(word))	return;
					
					if("Y".equals(word.toUpperCase())){
						flag = true;
						continue;
					}else{
						System.out.println("注册失败！");
						break;
					}
				}
			}
			
			if(!flag){
				System.out.println("请输入用户密码：");
				String pwd = input.next();
				if(exit(pwd))	return;
				
				if(pwd.isEmpty()){
					System.out.println("密码不能为空！");
					flag = true;
					continue;
				}
				
				User user = new User(username.trim(), pwd.trim());
				
				users.add(user);
				
				System.out.println("注册成功！");
				break;
			}
			
		}while(flag);
	}

	//登录
	private User login(){
		Scanner input = new Scanner(System.in);
		boolean flag = true;
		
		User r_user = null;
		
		do{
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			flag = true;
			
			System.out.println("请输入登录的用户名：");
			
			String username = input.next();
			if(exit(username))	return null;
			
			if(username.isEmpty()){
				System.out.println("用户名不能为空！");
				continue;
			}
		
			User user = null;
			
			if(users != null){
				User userByName = getUserByName(username);
				if(userByName != null){
					flag = false;
					user = userByName;
				}
			}
			
			if(flag){
				System.out.println("用户名不存在，是否重新输入？Y/N");
				
				String word = input.next();
				if(exit(word))	return null;
				
				if("N".equals(word.toUpperCase())){
					break;
				}
				continue;
			}
			
			System.out.println("请输入用户密码：");
			String pwd = input.next();
			if(exit(pwd))	return null;
			
			if(pwd.isEmpty()){
				System.out.println("密码不能为空！");
				flag = true;
				continue;
			}
			
			if(pwd.trim().equalsIgnoreCase(user.getPwd())){
				System.out.println("登录成功！");
				r_user = user;
				flag = false;
				break;
			}else{
				System.out.println("密码错误！");
				flag = true;
				continue;
			}
			
		}while(flag);
		
		return r_user;
	}

	//图书列表
	private void bookList(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Scanner input = new Scanner(System.in);
		if(books == null){
			System.out.println("没有图书~~~");
			return;
		}
		
		do{
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("编号\t书名\t\t作者\t价格\t状态");
			for (int i = 0; i < books.size(); i++) {
				Book book = books.get(i);
				
				if (book == null)	break;
				
				System.out.println(book.getBookId()
					+ "\t" + book.getBookName()
					+ "\t\t" + book.getAuthor()
					+ "\t" + book.getPrice()
					+ "\t" + (book.getIsBorrow() ?"已借出":"可借")
				);
			}
			
			System.out.println("请输入图书编号以获取借阅记录：");
			String bookId = input.next();
			if(exit(bookId))	return;
			
			Book book = getBookByBookId(bookId);
			if(book == null ){
				System.out.println("图书不存在！");
			}else{
				
				List<Borrow> borrowsByBookId = getBorrowsByBookId(bookId);
				
				if( borrowsByBookId==null ||  borrowsByBookId.size() == 0){
					System.out.println("该图书没有被借阅过");
				}else{
					System.out.println("编号\t书名\t借阅人\t借出日期\t状态\t归还日期\t金额");
					for (int i = 0; i < borrowsByBookId.size(); i++) {
						Borrow borrow = borrowsByBookId.get(i);
						
						String status = borrow.getBackTime() == null?"借阅中":"已归还";
						
						String str =book.getBookId()
								+"\t"+book.getBookName()
								+"\t"+borrow.getUserName()
								+"\t"+sdf.format(borrow.getBorrowTime())
								+"\t"+status;
						if(borrow.getBackTime() != null){
							str+="\t"+sdf.format(borrow.getBackTime())+"\t"+borrow.getMoney();
						}
						
						System.out.println(str);
					}
				}
			}
		}while(true);
	}
	
	//借书
	private void borrow(){
		Scanner input = new Scanner(System.in);
		
		do{
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("请输入你要借的图书编号：");
			String bookId = input.next();
			if(exit(bookId))	return;
			
			Book book = getBookByBookId(bookId);
			if(book == null ){
				System.out.println("图书不存在！");
			}else{
				
				if(book.getIsBorrow()){
					System.out.println("图书已借出！");
				}else{
					Borrow borrow = new Borrow();
					borrow.setUserName(user.getUsername());
					borrow.setBorrowTime(new Date());
					
					book.setBcount(book.getBcount()+1);
					book.setIsBorrow(true);
					
					borrow.setBookId(bookId);
					
					borrows.add(borrow);
					
					System.out.println("借阅图书成功！");
					break;
				}
				
			}
		}while(true);
	}
	
	//还书
	private void back(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Scanner input = new Scanner(System.in);
		
		do{
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("请输入你要还的图书编号：");
			String bookId = input.next();
			if(exit(bookId))	return;
			
			Book book = getBookByBookId(bookId);
			if(book == null ){
				System.out.println("图书不存在！");
			}else{
				
				if(!book.getIsBorrow()){
					System.out.println("图书未借出！");
				}else{
					
					
					//取到 当前用户的借阅信息
					Borrow borrow = getBorrow(user.getUsername(), bookId);
					
					if(borrow != null){
						//保存还书时间
						borrow.setBackTime(new Date());
						//计算金额
						int days = new NumUtil().daysBetween(borrow.getBorrowTime(), borrow.getBackTime());
						
						if(days < 1)	days=1;
						
						borrow.setMoney(days * book.getPrice());
						
						book.setIsBorrow(false);
						
						System.out.println("还书成功！该书 ["+book.getPrice()+"] / 天 ，借出日期 ["+sdf.format(borrow.getBorrowTime())+"] ,归还日期 ["+sdf.format(borrow.getBackTime())+"] ,借出 ["+days+"] 天，  一共花费：["+borrow.getMoney()+"] 元");
						break;
						
					}else{
						System.out.println("还书失败！");
					}
					
				}
				
			}
		}while(true);
	}
	
	//排行榜
	private void rankingList(){
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		//按 借阅次数 排序
		Collections.sort(books, new Comparator<Book>() {

			@Override
			public int compare(Book arg0, Book arg1) {
				int i = arg1.getBcount() - arg0.getBcount();
				
				if( i == 0){ // 如果次数一样,比较书名,返回比较结果
					i = arg0.getBookName().compareTo(arg1.getBookName());// 使用字符串的比较
				}
				return i;
			}
		});
		
		System.out.println("编号\t书名\t借阅次数");
		
		for (int i = 0; i < books.size(); i++) {
			Book book = books.get(i);
			
			System.out.println(book.getBookId()+"\t"+book.getBookName()+"\t"+book.getBcount());
		}
		
	}
	
	//借阅记录
	private void userInfo(){
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		List<Borrow> borrows = getBorrowsByUserName(user.getUsername());
		
		if(borrows == null || borrows.size() == 0){
			System.out.println("没有借阅记录！");
		}else{
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			System.out.println("编号\t书名\t借阅人\t借出日期\t状态\t归还日期\t金额");
			for (int i = 0; i < borrows.size(); i++) {
				Borrow borrow = borrows.get(i);
				
				Book book = getBookByBookId(borrow.getBookId());
				
				String status = borrow.getBackTime() == null?"借阅中":"已归还";
				
				String str = book.getBookId()
						+"\t"+book.getBookName()
						+"\t"+borrow.getUserName()
						+"\t"+sdf.format(borrow.getBorrowTime())
						+"\t"+status;
				if(borrow.getBackTime() != null){
					str+="\t"+sdf.format(borrow.getBackTime())+"\t"+borrow.getMoney();
				}
				
				System.out.println(str);
			}
		}
		
	}	
	
	private void addBook(){
		Scanner input = new Scanner(System.in);
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		do{
		
		System.out.println("请输入图书编号：");
		String bookId = input.next();
		if(exit(bookId))	return;
		
		if(getBookByBookId(bookId) != null){
			System.out.println("图书编号已存在！");
			continue;
		}
		
		
		System.out.println("请输入图书名称：");
		String bookName = input.next();
		if(exit(bookName))	return;
		
		System.out.println("请输入作者：");
		String author = input.next();
		if(exit(author))	return;
		
		System.out.println("请输入价格(元/天)：");
		String priceStr = input.next();
		if(exit(priceStr))	return;
		
		double price;
		try {
			price = Double.parseDouble(priceStr);
		} catch (NumberFormatException e) {
			System.out.println("价格必须是数字类型，可以为小数~");
			continue;
		}
		
		Book book1 = new Book();
		book1.setBookId(bookId);
		book1.setBookName(bookName);
		book1.setAuthor(author);
		book1.setPrice(price);
		
		books.add(book1);
		
		System.out.println("新增《"+bookName+"》成功！");
		
		break;
		
		}while(true);
	}
	
	
	private void delBook(){
		Scanner input = new Scanner(System.in);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		do{
		
			System.out.println("请输入要删除的图书编号：");
			String bookId = input.next();
			if(exit(bookId))	return;
			
			Book book = getBookByBookId(bookId);
			
			if(book == null){
				System.out.println("图书编号不存在！");
				continue;
			}
		
			if(book.getIsBorrow()){
				System.out.println("《"+book.getBookName()+"》为借出状态，不能删除！");
				continue;
			}
			
			books.remove(book);
		
			System.out.println("删除《"+book.getBookName()+"》成功！");
			
			break;
		
		}while(true);
	}
	
	/**
	 * 根据用户名获取用户信息
	 * @author hezhao
	 * @Time   2016年9月9日 下午5:21:15
	 * @param username
	 * @return
	 */
	private User getUserByName(String username){
		for (User vo : users) {
			if(vo.getUsername().equalsIgnoreCase(username.trim())){
				return vo;
			}
		}
		return null;
	}
	
	/**
	 * 获取图书信息
	 * @author hezhao
	 * @Time   2016年9月9日 下午5:21:15
	 * @param bookId
	 * @return
	 */
	private Book getBookByBookId(String bookId){
		for (Book vo : books) {
			if(vo.getBookId().equalsIgnoreCase(bookId.trim())){
				return vo;
			}
		}
		return null;
	}
	
	/**
	 * 获取借阅信息
	 * @author hezhao
	 * @Time   2016年9月9日 下午5:21:15
	 * @param userName
	 * @return
	 */
	private List<Borrow> getBorrowsByUserName(String userName){
		
		List<Borrow> temp_borrows = new ArrayList<>();
		
		for (Borrow vo : borrows) {
			if(vo.getUserName().equalsIgnoreCase(userName.trim())){
				temp_borrows.add(vo);
			}
		}
		return temp_borrows;
	}
	
	/**
	 * 获取借阅信息
	 * @author hezhao
	 * @Time   2016年9月9日 下午5:21:15
	 * @param bookId
	 * @return
	 */
	private List<Borrow> getBorrowsByBookId(String bookId){
		
		List<Borrow> temp_borrows = new ArrayList<>();
		
		for (Borrow vo : borrows) {
			if(vo.getBookId().equalsIgnoreCase(bookId.trim())){
				temp_borrows.add(vo);
			}
		}
		return temp_borrows;
	}
	
	/**
	 * 获取借阅信息
	 * @author hezhao
	 * @Time   2016年9月9日 下午5:21:15
	 * @param bookId
	 * @return
	 */
	private Borrow getBorrow(String userName,String bookId){
		
		Borrow temp_borrow = null;
		
		for (Borrow vo : borrows) {
			if(vo.getBookId().equalsIgnoreCase(bookId.trim())
					&& vo.getUserName().equalsIgnoreCase(userName.trim())
					&& vo.getBackTime() == null){
				temp_borrow = vo;
			}
		}
		return temp_borrow;
	}
	
	/**
	 * 检查登录
	 * @author hezhao
	 * @Time   2016年9月9日 下午2:04:54
	 * @param keyword
	 * @return
	 */
	private boolean checkLogin(){
		if(user!=null)	return true;
		return false;
	}
	
	/**
	 * 退出
	 * @author hezhao
	 * @Time   2016年9月9日 下午2:04:54
	 * @param keyword
	 * @return
	 */
	private boolean exit(String keyword){
		if(keyword!=null && keyword.trim().equalsIgnoreCase("exit"))	return true;
		return false;
	}
	
}

/**
 * 用户信息
 * @author hezhao
 * @Time   2016年9月9日 下午2:05:00
 * @Description 无
 * @Version V 1.0
 */
class User implements Serializable{
	private static final long serialVersionUID = 3568895388678711241L;
	
	private String username;
	private String pwd;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public User() {
		super();
	}
	public User(String username, String pwd) {
		super();
		this.username = username;
		this.pwd = pwd;
	}
	
}

/**
 * 图书信息
 * @author hezhao
 * @Time   2016年9月9日 下午2:14:29
 * @Description 无
 * @Version V 1.0
 */
class Book implements Serializable{
	private static final long serialVersionUID = 8946131092486004353L;
	
	private String bookId;
	private String bookName;
	private String author;
	private double price;//价格  元/天
	private boolean isBorrow;//是否借出
	private int bcount;
	
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public boolean getIsBorrow() {
		return isBorrow;
	}
	public void setIsBorrow(boolean isBorrow) {
		this.isBorrow = isBorrow;
	}
	public int getBcount() {
		return bcount;
	}
	public void setBcount(int bcount) {
		this.bcount = bcount;
	}
}

/**
 * 借阅信息
 * @author hezhao
 * @Time   2016年9月9日 下午2:17:03
 * @Description 无
 * @Version V 1.0
 */
class Borrow implements Serializable{
	private static final long serialVersionUID = -5433676649092630485L;
	
	private int borrowId;
	private String bookId;
	private String userName;
	private Date borrowTime;//借出日期
	private Date backTime;//归还日期
	private double money;//金额
	
	
	public int getBorrowId() {
		return borrowId;
	}
	public void setBorrowId(int borrowId) {
		this.borrowId = borrowId;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getBorrowTime() {
		return borrowTime;
	}
	public void setBorrowTime(Date borrowTime) {
		this.borrowTime = borrowTime;
	}
	public Date getBackTime() {
		return backTime;
	}
	public void setBackTime(Date backTime) {
		this.backTime = backTime;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	
	
}

class NumUtil{
	/**
	 * 计算日期差
	 * @author hezhao
	 * @Time   2016年8月26日 上午9:48:50
	 * @param now 当前日期
	 * @param returnDate 到期日期
	 * @return
	 */
	public int daysBetween(Date startDate, Date endDate) {
	    Calendar cNow = Calendar.getInstance();
	    Calendar cReturnDate = Calendar.getInstance();
	    cNow.setTime(startDate);
	    cReturnDate.setTime(endDate);
	    setTimeToMidnight(cNow);
	    setTimeToMidnight(cReturnDate);
	    long todayMs = cNow.getTimeInMillis();
	    long returnMs = cReturnDate.getTimeInMillis();
	    long intervalMs = returnMs - todayMs;
	    return millisecondsToDays(intervalMs);
	  }

	  private int millisecondsToDays(long intervalMs) {
	     return (int) (intervalMs / (1000 * 86400));
	  }

	  private void setTimeToMidnight(Calendar calendar) {
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	  }
}