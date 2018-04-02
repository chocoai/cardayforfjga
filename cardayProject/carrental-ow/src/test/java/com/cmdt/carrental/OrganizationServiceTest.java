package com.cmdt.carrental;


import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.cmdt.carrental.common.entity.CreditHistory;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.CreditHistoryDto;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RecharageCreditDto;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.JsonUtils;

public class OrganizationServiceTest extends SuperTestCase{

	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private UserService userService;
	
	@Test
	public void testFindOrgCreditById(){
		Organization organization=organizationService.findOrgCreditById(1L);
		System.out.println(organization.getLimitedCredit());
		System.out.println(organization.getAvailableCredit());
		System.out.println(organization.getName());
	}

}
