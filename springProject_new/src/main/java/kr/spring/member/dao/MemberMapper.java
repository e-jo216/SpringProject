package kr.spring.member.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.spring.member.vo.MemberVO;

public interface MemberMapper {
	@Select("SELECT member_seq.nextval FROM dual")
	public int select_num();
	
	@Insert("INSERT INTO member (num,id) VALUES (#{num},#{id})")
	public void insertMember(MemberVO member);
	
	@Insert("INSERT INTO member_detail (num,m_num,name,pass,phone,email,zipcode,address,address_detail) VALUES (member_detail_seq.nextval,#{num},#{name},#{pass},#{phone},#{email},#{zipcode},#{address},#{address_detail})")
	public void insertMember_detail(MemberVO member);
	
	@Update("UPDATE member_detail SET nickname='익명'||m_num WHERE m_num=#{num}")
	public void updateNickname(Integer m_num);
	
	public MemberVO selectCheckMember(String id);
	
	@Select("SELECT * FROM member m JOIN member_detail d ON m.num = d.m_num WHERE m.num=#{num}")
	public MemberVO selectMember(Integer m_num);
	
	@Select("SELECT m.id FROM member m JOIN member_detail d ON m.num=d.m_num WHERE d.name=#{name} AND d.email=#{email} AND d.phone=#{phone}")
	public String searchId(@Param("name")String name,@Param("email")String email,@Param("phone")String phone);
	
	//비밀번호 확인 시 회원 정보 검색
	@Select("SELECT d.pass FROM member m JOIN member_detail d ON m.num = d.m_num WHERE m.id=#{id} AND d.email=#{email} AND d.phone=#{phone}")
	public String searchPass(@Param("id")String id,@Param("email")String email,@Param("phone")String phone);
	//비밀번호 변경
	@Update("UPDATE member_detail SET pass=#{pass} WHERE email=#{email}")
	public void changePass(@Param("pass")String pass,@Param("email")String email);
	

	//구글회원가입
	public void registerBySocial(String id, String email);
	
	//구글로그인
	@Select("SELECT * FROM member m JOIN member_detail d ON m.num = d.m_num WHERE d.email=#{email}")
	public MemberVO loginBySocial(@Param("email")String email);
}
