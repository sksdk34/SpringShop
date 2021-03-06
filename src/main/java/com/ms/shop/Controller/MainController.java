package com.ms.shop.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ms.shop.Dao.OrderDao;
import com.ms.shop.Dao.ProductDao;
import com.ms.shop.Dao.UserDao;
import com.ms.shop.Vo.OrderVo;
import com.ms.shop.Vo.ProductVo;
import com.ms.shop.Vo.UserVo;

@Controller
public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	ProductDao productDao;
	UserDao userDao;
	OrderDao orderDao;
	
	@Autowired
	public void setDao(ProductDao dao) {
		this.productDao = dao;
	}
	
	@Autowired
	public void setDao(UserDao dao) {
		this.userDao = dao;
	}
	
	@Autowired
	public void setDao(OrderDao dao) {
		this.orderDao = dao;
	}
	
	//초기 페이지
	@RequestMapping("/")
	public String index() {
		
		return "index";
	}
	
	//업로드 파일 경로
	@Resource(name = "uploadPath")
	private String uploadPath;

	//메인 페이지
	@RequestMapping("/Main")
	public String Main(Model model, HttpSession session) throws Exception {
		
		logger.info("Main");
		
		if(session.getAttribute("login") != null) {
			
			model.addAttribute("login", "login");
		}
		
		//제품 랭킹 출력
		List<ProductVo> productRank = productDao.productRank();
		model.addAttribute("productRank", productRank);
		
		//신상품 출력
		List<ProductVo> productNew = productDao.productNew();
		model.addAttribute("productNew", productNew);
		
		return "Main";
	}
	
	//성별별 리스트
	@RequestMapping(value="/list/{gender}", method=RequestMethod.GET)
	public String genderList(Model model, HttpSession session, @PathVariable String gender) throws Exception{
		
		if(session.getAttribute("login") != null) {

			model.addAttribute("login", "login");
		}
		//제품 출력
		ProductVo info = new ProductVo();
		info.setGender(gender);
		info.setNo(0);
		System.out.println(info.getNo());
		System.out.println(info.getGender());
		List<ProductVo> productList = productDao.productListGender(info);
		model.addAttribute("productList", productList);
		
		return "list";
	}
	
	//성별별 리스트 append ajax
	@RequestMapping(value="/list/{gender}", method=RequestMethod.POST)
	public @ResponseBody List<ProductVo> genderList(@RequestBody ProductVo productVo, @PathVariable String gender) throws Exception{
		
		System.out.println(productVo.getNo());
		ProductVo info = new ProductVo();
		info.setGender(gender);
		info.setNo(productVo.getNo() * 5); //스크롤 횟수
		List<ProductVo> appendList = productDao.productListGender(info);
		
		return appendList;
	}
	
	//카테고리별 리스트
	@RequestMapping(value="/list/{gender}/{category}", method=RequestMethod.GET)
	public String categoryList(Model model, HttpSession session,@PathVariable String gender, @PathVariable String category) throws Exception{
		
		if(session.getAttribute("login") != null) {

			model.addAttribute("login", "login");
		}
		//제품 출력
		ProductVo info = new ProductVo();
		info.setGender(gender);
		info.setCategory(category);
		List<ProductVo> productList = productDao.productListCategory(info);
		model.addAttribute("productList", productList);
		return "list";
	}
	
	//카테고리별 리스트 append ajax
	@RequestMapping(value="/list/{gender}/{category}", method=RequestMethod.POST)
	public @ResponseBody List<ProductVo> categoryList(@RequestBody ProductVo productVo, @PathVariable String gender, @PathVariable String category) throws Exception{
			
		System.out.println(productVo.getNo());
		ProductVo info = new ProductVo();
		info.setGender(gender);
		info.setCategory(category);
		info.setNo(productVo.getNo() * 5); //스크롤 횟수
		List<ProductVo> appendList = productDao.productListCategory(info);
		
		return appendList;
	}
	
	//상품 상세보기 페이지
	@RequestMapping(value="/detail/{no}", method=RequestMethod.GET)
	public String detail(Model model, HttpSession session, @PathVariable int no) throws Exception{
		
		if(session.getAttribute("login") != null) {
			
			model.addAttribute("login", "login");
		}
		//제품 출력
		ProductVo product = productDao.productDetail(no);
		model.addAttribute("product", product);
		
		//review 출력
		OrderVo rInfo = new OrderVo();
		rInfo.setNo(no);
		List<OrderVo> reviews = orderDao.reviewList(rInfo);
		model.addAttribute("reviews", reviews);
		
		//찜
		OrderVo info = new OrderVo();
		info.setId((String) session.getAttribute("login"));
		info.setNo(no);
		OrderVo pick = orderDao.havePick(info);
		model.addAttribute("pick", pick);
		
		return "detail";
	}
	
	//찜 ajax
	@ResponseBody
	@RequestMapping(value="/detail/{no}", method=RequestMethod.POST)
	public String pick(@RequestBody String check, HttpServletRequest request, HttpSession session, @PathVariable int no) throws Exception {
		
		//상세보기 정보
		OrderVo info = new OrderVo();
		info.setId((String) session.getAttribute("login"));
		info.setNo(no);
		
		//찜 pick unpick
		logger.info(check);
		if(check.equals("pick=")) {
			System.out.println("1");
			orderDao.unpick(info);
		}else if(check.equals("unpick=")) {
			System.out.println("2");
			orderDao.pick(info);
		}
		
		return "test";
		
	}
	
	//구매 페이지
	@RequestMapping(value="/detail/{no}/purchase", method=RequestMethod.GET)
	public String purchase(Model model, HttpSession session, @PathVariable int no) throws Exception{
		
		if(session.getAttribute("login") != null) {
			
			//제품 정보
			ProductVo product = productDao.productDetail(no);
			model.addAttribute("product", product);
			
			//사용자 정보
			List<UserVo> user = userDao.userInfo((String)session.getAttribute("login"));
			model.addAttribute("user", user.get(0));
			
			return "purchase";
		}
		return "err";
	}
	
	//구매 처리
	@RequestMapping(value="/detail/{no}/purchase", method=RequestMethod.POST)
	public String purchaseProcessing(Model model, HttpSession session, HttpServletRequest request, @PathVariable int no) throws Exception{
		
		if(session.getAttribute("login") != null) {
			
			String brand = request.getParameter("brand");
			String name = request.getParameter("name");
			String id = request.getParameter("id");
			String address = request.getParameter("address");
			String phone = request.getParameter("phone");
			
			//구매목록에 추가
			OrderVo info = new OrderVo();
			info.setBrand(brand);
			info.setName(name);
			info.setNo(no);
			info.setId(id);
			info.setAddress(address);
			info.setPhone(phone);
			
			orderDao.insertPurchase(info);
			
			//상품의 수량이 0이 되었을 때 status off
			if(productDao.productDetail(no).getStock() == 0) {
				
				Map map = new HashMap<String, Integer>();
				map.put("status", 0);
				map.put("stock", 0);
				map.put("no", no);
				
				productDao.productManagement(map);
			}else {
				
				//상품 수량 변경
				productDao.productStock(no);
			}
			
			return "redirect:/Main";
		}
		return "err";
	}
	
	//마이페이지
	@RequestMapping("/mypage")
	public String mypage(Model model, HttpSession session) throws Exception{
		if(session.getAttribute("login") != null) {
			
			//구매목록 출력
			String id = (String) session.getAttribute("login");
			List<OrderVo> purchase = orderDao.customerPurchaseList(id);
			model.addAttribute("purchase", purchase);
			
			//찜목록 출력
			List<OrderVo> pick = orderDao.customerPick(id);
			List<ProductVo> productList = new ArrayList<ProductVo>();
			for(int i=0; i<pick.size(); i++) {
				int no = pick.get(i).getNo();
				ProductVo product = productDao.productDetail(no);
				productList.add(product);
			}
			model.addAttribute("productList", productList);
			
			return "mypage";
		}
		return "err";
	}
	
	//리뷰 작성 페이지
	@RequestMapping(value="/review/{no}", method=RequestMethod.GET)
	public String review(Model model, HttpSession session, @PathVariable int no) throws Exception{
		
		if(session.getAttribute("login") != null) {
			model.addAttribute("no", no);
			
			return "review";
		}
		
		return "err";
	}
	
	//리뷰 작성 처리
	@RequestMapping(value="/review/{no}", method=RequestMethod.POST)
	public String reviewProcessing(Model model, HttpServletRequest request, HttpSession session, @PathVariable int no, MultipartFile file) throws Exception{
		
		if(session.getAttribute("login") != null) {
			String content = request.getParameter("content");
			String id = (String) session.getAttribute("login");
			
			//이미지 처리
			String savedName = null;
			if(!file.isEmpty()) {
				UUID uid = UUID.randomUUID(); //중복 방지하기 위해 임의로 이름 생성
				savedName = uid.toString() + "_" + file.getOriginalFilename();
				File target = new File(uploadPath, savedName);
				FileCopyUtils.copy(file.getBytes(), target); //업로드 디렉토리에 저장
			}
			
			OrderVo info = new OrderVo();
			info.setNo(no);
			info.setId(id);
			info.setContent(content);
			info.setImage(savedName);
			
			orderDao.insertReivew(info);
			
			return "redirect:/mypage";
		}
		
		return "err";
	}
	
	//상품 검색
	@RequestMapping("/search")
	public String search(Model model,HttpServletRequest request, HttpSession session) throws Exception{
		
		if(session.getAttribute("login") != null) {
			String keyword = request.getParameter("keyword");
			String str = '%' + keyword + '%';
			System.out.println(str);
			List<ProductVo> productList = productDao.productSearch(str);
			model.addAttribute("productList", productList);
			
			return "list";
		}
		return "err";
	}
}
