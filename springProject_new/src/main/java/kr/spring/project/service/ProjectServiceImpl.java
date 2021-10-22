package kr.spring.project.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.project.dao.ProjectMapper;
import kr.spring.project.vo.ProjectVO;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService{

	@Autowired
	private ProjectMapper projectMapper;
	
	@Override
	public void insertProject(ProjectVO project) {
		projectMapper.insertProject(project);
	}

	@Override
	public ProjectVO selectCheckProject(String name) {
		return projectMapper.selectCheckProject(name);
	}
	
	@Override
	public int selectNum() {
		return projectMapper.selectNum();
	}
	
	public void updateProject(ProjectVO project) {
		projectMapper.updateProject(project);
	}

	@Override
	public void updatePhoto(ProjectVO project) {
		projectMapper.updatePhoto(project);
	}
	
	@Override
	public void deletePhoto(ProjectVO project) {
		projectMapper.deletePhoto(project);
	}
	
	public List<ProjectVO> selectList(Map<String, Object> map) {
		return projectMapper.selectList(map);
	}

	@Override
	public int selectRowCount(Map<String, Object> map) {
		return projectMapper.selectRowCount(map);
	}

	@Override
	public ProjectVO selectProject(Integer num) {
	    return projectMapper.selectProject(num);
	}

	@Override
	public int selectProjectSupporter(Integer num) {
		return projectMapper.selectProjectSupporter(num);
	}

	@Override
	public Date selectFinish(int pnum) {
		return projectMapper.selectFinish(pnum);
	}

}
