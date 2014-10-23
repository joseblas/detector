package com.wh.integration.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.wh.integration.model.entity.TwitterMessage;


@Service
public interface TwitterMessageDao extends JpaRepository<TwitterMessage, Long >{

	TwitterMessage findById( Long  id );
	
}
