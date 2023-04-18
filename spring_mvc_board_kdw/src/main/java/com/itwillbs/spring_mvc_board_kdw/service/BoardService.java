package com.itwillbs.spring_mvc_board_kdw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.spring_mvc_board_kdw.mapper.BoardMapper;
import com.itwillbs.spring_mvc_board_kdw.vo.BoardVO;

@Service
public class BoardService {

	@Autowired
	private BoardMapper mapper;

	public int registBoard(BoardVO board) {
		return mapper.insertBoard(board);
	}

	
}
