package kr.spring.mypage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Delete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.spring.delivery.vo.DeliveryVO;
import kr.spring.member.service.MemberService;
import kr.spring.member.vo.MemberVO;
import kr.spring.mypage.dao.MypageMapper;
import kr.spring.mypage.service.MypageService;
import kr.spring.util.PagingUtil;

@Controller
public class MypageController {

	private static final Logger logger = LoggerFactory.getLogger(MypageController.class);
	private int rowCount = 6;
	private int pageCount = 10;


	@Autowired
	private MypageService mypageService;
	@Autowired
	private MemberService memberService;

	//자바빈 초기화
	@ModelAttribute
	public MemberVO initCommand2() {
		return new MemberVO();
	}
	
	//마이페이지 호출
	@RequestMapping("/mypage/myPage.do")
	public String view(HttpSession session, Model model) {
		
		Integer user_num = (Integer)session.getAttribute("user_num");
		MemberVO member = memberService.selectMember(user_num); //레코드 하나 읽어옴
		
		logger.debug("<<마이페이지 호출>>");
		
		model.addAttribute("member", member);
		
		return "mypageView";
	}
	//후원프로젝트 호출
	@RequestMapping("/mypage/mySupport.do")
	public String mySupportView() {
		
		logger.debug("<<후원프로젝트 호출>>");
		
		return "mySupportView";
	}
	//나의프로젝트 호출
	@RequestMapping("/mypage/myProject.do")
	public String myProjectView() {
			
		logger.debug("<<나의프로젝트 호출>>");
			
		return "myProjectView";
	}
	//내문의게시판 호출
	@RequestMapping("/mypage/myQuestion.do")
	public String myQuestionView() {
			
		logger.debug("<<문의페이지 호출>>");
			
		return "myQuestionView";
	} 
	//설정 호출
	@RequestMapping("/mypage/mySettings.do")
	public String mySettingView() {
		
		logger.debug("<<설정페이지 호출>>");
		
		return "mySettingsView";
	}
	//설정 - 기본정보 호출
	@RequestMapping("/mypage/myInformation.do")
	public String myInformationView(HttpSession session, Model model) {
		
		Integer user_num = (Integer)session.getAttribute("user_num");
		MemberVO member = memberService.selectMember(user_num); //레코드 하나 읽어옴
		//memberService.updateNickname(user_num);
		
		model.addAttribute("member", member);
		logger.debug("<<설정-기본정보 호출>>");
		
		return "myInformationView";
	}
	//기본정보 - 닉네임 변경
	@RequestMapping("/mypage/nicknameUpdate.do")
	@ResponseBody
	public Map<String, String> nicknameUpdate(HttpSession session, @RequestParam String nickname) {
		
		logger.debug("<<닉넴>> : " + nickname);
		
		Map<String, String> map = new HashMap<String, String>();
		
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		if(user_num==null) {//로그인 안된 상태
			map.put("result", "logout");
		}else {//로그인 상태
			
			int check = mypageService.checkNickname(nickname); //null이 안나옴 ㅠ~ㅠ~ㅠ
	
			if(check > 0) {//닉네임 중복
				map.put("result", "nicknameDuplicated");
			}else {
				MemberVO member = new MemberVO();
				member.setNum(user_num);
				member.setNickname(nickname);
				logger.debug("<<엘스>>" + member);
				mypageService.updateNickname(member);
				
				logger.debug("<<닉네임>> : " + member.getNickname());
				session.setAttribute("user_nickname", member.getNickname());
				
				map.put("result", "success");
			}
		}
		
		return map;
		
	}
	//기본정보 - 회원 프로필 사진 업데이트
	@RequestMapping("/mypage/updateMyphoto.do")
	@ResponseBody
	public Map<String,String> processProfile(MemberVO memberVO, HttpSession session){
		Map<String,String> map = new HashMap<String,String>();
		
		Integer user_num = (Integer)session.getAttribute("user_num");
		if(user_num==null) {//로그인 안된 상태
			map.put("result", "logout");
		}else {//로그인 상태
			memberVO.setNum(user_num);
			mypageService.updateProfile(memberVO);
			
			//이미지를 업로드한 후 세션에 저장된 회원 프로필 사진 정보 갱신
			session.setAttribute("user_photo", memberVO.getPhoto());
			
			map.put("result", "success");
		}
		
		return map;
	}
	//기본정보 - 프로필 사진 출력
	@RequestMapping("/mypage/photoView.do")
	public ModelAndView viewImage(HttpSession session) {
		
		Integer user_num = (Integer)session.getAttribute("user_num");
		MemberVO memberVO = memberService.selectMember(user_num);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("imageView");
		mav.addObject("imageFile", memberVO.getPhoto());
		mav.addObject("filename", memberVO.getPhoto_name()); //파일명 가지고 확장자 체크 함
		
		return mav;
	}
	
	//기본정보 - 프로필 사진 출력
	@GetMapping("/mypage/mphotoView.do")
	public ModelAndView memberImage(int num) {
		MemberVO memberVO = memberService.selectMember(num);
	      
	    ModelAndView mav = new ModelAndView();
	    mav.setViewName("imageView");
	    mav.addObject("imageFile", memberVO.getPhoto());
	    mav.addObject("filename", memberVO.getPhoto_name());
	      
	    return mav;
	}
	//기본정보 - 연락처 변경
	@RequestMapping("/mypage/updateMyPhone.do")
	@ResponseBody
	public Map<String,String> updatePhone(MemberVO memberVO, HttpSession session){
		Map<String,String> map = new HashMap<String,String>();
		
		Integer user_num = (Integer)session.getAttribute("user_num");
		if(user_num==null) {//로그인 안된 상태
			map.put("result", "logout");
		}else {//로그인 상태
			memberVO.setNum(user_num);
			mypageService.updatePhone(memberVO);
	
			map.put("result", "success");
		}
		
		return map;
	}
	//기본정보 - 비밀번호 변경
	@RequestMapping("/mypage/updatePassword.do")
	@ResponseBody
	public Map<String,String> updatePass(MemberVO memberVO, HttpSession session){
		Map<String,String> map = new HashMap<String,String>();
		
		Integer user_num = (Integer)session.getAttribute("user_num");
		MemberVO originVO = memberService.selectMember(user_num); //비번 일치비교하기 위해 값가져옴
		
		if(user_num==null) {//로그인 안된 상태
			map.put("result", "logout");
		}else if(user_num != memberVO.getNum()){
			map.put("result", "differentUser");
		}else if(!originVO.getPass().equals(memberVO.getPass())) {
			logger.debug("<<기존비번 >> " + originVO.getPass() + " <<비교하는비번>> : " + memberVO.getPass());
			map.put("result", "passNotFound");
		}else{//로그인 상태
			logger.debug("<<변경한다>>");
			memberVO.setNum(user_num);
			mypageService.updatePass(memberVO);
	
			map.put("result", "success");
		}
		
		return map;
	}
	//기본정보 - 탈퇴 폼 호출
	@GetMapping("/mypage/deleteform.do")
	public String deleteformView(HttpSession session, Model model) {
		
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		MemberVO member = memberService.selectMember(user_num);
		model.addAttribute("member", member);
		
		logger.debug("<<회원탈퇴 호출>> : " + member);
		
		return "mydeleteView";
	}
	//기본정보 - 탈퇴 처리
	@PostMapping("/mypage/deleteform.do")
	@ResponseBody
	public Map<String,String> deleteMember(MemberVO member, HttpSession session) {
		Map<String,String> map = new HashMap<String,String>();
		
		Integer user_num = (Integer)session.getAttribute("user_num");
		MemberVO originVO = memberService.selectMember(user_num); //비번 일치비교하기 위해 값가져옴
		
		if(user_num==null) {//로그인 안된 상태
			map.put("result", "logout");
		}else if(user_num != member.getNum()){//회원번호 일치여부 확인
			map.put("result", "differentUser");
		}else if(!originVO.getPass().equals(member.getPass())) { //비번 일치여부 확인
			logger.debug("<<기존비번 >> " + originVO.getPass() + " <<비교하는비번>> : " + member.getPass());
			map.put("result", "passNotFound");
		}else{//탈퇴
			logger.debug("<<회원 탈퇴>>");
			member.setNum(user_num);
			mypageService.deleteMember(member);
			
			map.put("result", "success");
			
			//로그 아웃
			session.invalidate();
		}
		return map;
	}

	//설정 - 배송지 호출 및 리스트
	@RequestMapping("/mypage/myDelivery.do")
	public ModelAndView myDeliveryView(
						HttpSession session,
						DeliveryVO deliveryVO,
						@RequestParam(value="pageNum", defaultValue="1") int currentPage) {
					
		Integer user_num = (Integer)session.getAttribute("user_num");
		deliveryVO.setM_num(user_num);
		
		logger.debug("<<배송지호출 - deliveryVO 값>> : " + deliveryVO);
		
		int count = mypageService.getdelCount(user_num); //배송지 총 개수 가져옴
		
		logger.debug("<<배송지호출 - 총 배송목록 수>> : " + count);
		
		PagingUtil page = new PagingUtil(currentPage, count, rowCount, pageCount, 
										"myDelivery.do");
		
		logger.debug("<<배송지호출 - 페이지 정보>> : " + page);
		
		//목록 호출
		List<DeliveryVO> list = null;
		if(count > 0) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("start", page.getStartCount());
			map.put("end", page.getEndCount());
			map.put("m_num", deliveryVO.getM_num());
			
			list = mypageService.delSelectList(map);
		}
		ModelAndView mav = new ModelAndView();
		//뷰 이름 설정
		mav.setViewName("myDeliveryView");
		//데이터 저장
		mav.addObject("count", count);
		mav.addObject("list", list);
		mav.addObject("pagingHtml", page.getPagingHtml());
		
		return mav;
	}
	
	//설정 - 배송지 추가
	@RequestMapping("/mypage/myDeliverysubmit.do")
	public String myDelivery(HttpSession session, DeliveryVO deliveryVO) {
	
		Integer user_num = (Integer)session.getAttribute("user_num");
		deliveryVO.setM_num(user_num);
		
		logger.debug("<<배송지 추가>> : " + deliveryVO);
		
		if(deliveryVO.getAddress_check()==1) {//기본배송지로 변경하는 값 들어있는 경우
			mypageService.updateDelCheck(deliveryVO);
			mypageService.insertDelivery(deliveryVO);
		}else {//기본배송지 변경아니고 그냥 추가만 하는경우
			mypageService.insertDelivery(deliveryVO);
		}

		return "redirect:/mypage/myDelivery.do";
	}
	//설정 - 배송지 수정 폼 호출
	@RequestMapping("/mypage/myDeliveryUpdateForm.do")
	public String updateDeliveryForm(@RequestParam(value="num") int num,
									 @RequestParam(value="m_num") int m_num,
									 Model model) {
		
		DeliveryVO deliveryVO = mypageService.selectDelivery(num);
		
		logger.debug("<<배송지 수정 폼 호출>>");
		
		model.addAttribute("deliveryVO", deliveryVO);
		model.addAttribute("num", num);
		model.addAttribute("m_num", m_num);

		return "myDeliveryUpdateForm";
	}
	//설정 - 배송지 수정
	@RequestMapping("/mypage/myDeliveryUpdate.do")
	public String updateDelivery(DeliveryVO deliveryVO, HttpSession session) {
		
		Integer user_num = (Integer)session.getAttribute("user_num");
		
		logger.debug("<<배송지수정>> : " + deliveryVO);
		
		int check = deliveryVO.getAddress_check();
		if(check==1) {//기본배송지 설정 체크할 시 기존에 있던 1 찾아서 0으로 바꿈
			mypageService.updateDelCheck(deliveryVO);
			mypageService.updateDelivery(deliveryVO);
		}else {//기본배송지 설정 안할 시
			mypageService.updateDelivery(deliveryVO);
		}

		return "redirect:/mypage/myDelivery.do";
	}
	//설정 - 배송지 삭제
	@RequestMapping("/mypage/myDeliveryDelete.do")
	public String deleteDelivery(Integer num) {
		System.out.println("목록호출삭제1");
		DeliveryVO deliveryVO = mypageService.selectDelivery(num);
		logger.debug("<<배송지삭제>> : " + deliveryVO);
		
		mypageService.deleteDelivery(deliveryVO.getNum());
		
		return "redirect:/mypage/myDelivery.do";
	}
	
	//설정 - 결제수단 호출
	@RequestMapping("/mypage/myPayment.do")
	public String myPaymentView() {
				
		logger.debug("<<설정-결제수단 호출>>");
				
		return "myPaymentView";
	}
	
}









