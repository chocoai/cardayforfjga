package com.cmdt.carrental.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.entity.Driver;
import com.cmdt.carrental.common.entity.Employee;
import com.cmdt.carrental.common.entity.PhoneVerificationCode;
import com.cmdt.carrental.common.entity.Rent;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.AllocateDepModel;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.EmployeeModel;
import com.cmdt.carrental.common.model.EnterpriseTypeModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UserModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.Pagination;
import com.cmdt.carrental.common.util.SqlUtil;
import com.cmdt.carrental.common.util.TimeUtils;

@Repository
public class UserDaoImpl implements UserDao
{
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public User createUser(final User user)
    {
        final String sql =
            "insert into sys_user(organization_id, username, password, salt, role_id,realname,phone,email,locked) values(?,?,?,?,?,?,?,?,?)";
        
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator()
        {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException
            {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setLong(count++, user.getOrganizationId());
                psst.setString(count++, user.getUsername());
                psst.setString(count++, user.getPassword());
                psst.setString(count++, user.getSalt());
                psst.setLong(count++, user.getRoleId());
                psst.setString(count++, user.getRealname());
                psst.setString(count++, user.getPhone());
                psst.setString(count++, user.getEmail());
                psst.setBoolean(count, user.getLocked());
                return psst;
            }
        }, keyHolder);
        
        user.setId(keyHolder.getKey().longValue());
        return user;
    }
    
    public User updateUser(User user)
    {
        String sql = "update sys_user set organization_id=?,role_id=?,realname=?,phone=?,email=?,locked=?,iam_id=? where id=?";
        jdbcTemplate.update(sql,
            user.getOrganizationId(),
            user.getRoleId(),
            user.getRealname(),
            user.getPhone(),
            user.getEmail(),
            user.getLocked(),
            user.getIamId(),
            user.getId());
        return user;
    }
    
    public void deleteUser(Long userId)
    {
        String sql = "delete from sys_user where id=?";
        jdbcTemplate.update(sql, userId);
    }
    
    @Override
    public User findOne(Long userId)
    {
        String sql =
            "select id, organization_id, username,password,salt,role_id,realname,phone,email,locked from sys_user where id=?";
        List<User> userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class), userId);
        if (userList.size() == 0)
        {
            return null;
        }
        return userList.get(0);
    }
    
    @Override
    public List<UserModel> findAll()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "    	select uo.id,uo.username,uo.organization_id,uo.organization_name,uo.role_id,r.role as role_name,r.template_id as user_category,uo.realname,uo.phone,uo.email,uo.locked");
        buffer.append("    	from");
        buffer.append(
            "    	(select u.id, u.username,u.organization_id,o.name as organization_name,u.role_id,u.realname,u.phone,u.email,u.locked");
        buffer.append("    	from sys_user u");
        buffer.append("    	left join sys_organization o");
        buffer.append("    	on");
        buffer.append("    	u.organization_id = o.id) uo");
        buffer.append("    	left join sys_role r");
        buffer.append("    	on");
        buffer.append("    	uo.role_id = r.id");
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class));
    }
    
    @Override
    public User findByUsername(String username)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "    	select ur.id, ur.organization_id,o.name as organization_name, ur.username,ur.password,ur.salt,ur.role_id,"
            + "ur.role_name,ur.user_category,ur.realname,ur.phone,ur.email,ur.locked,o.shortname, o.institution_code, o.institution_feature,o.institution_level,o.is_institution");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select u.id, u.organization_id, u.username,u.password,u.salt,u.role_id,r.role as role_name,r.template_id as user_category,u.realname,u.phone,u.email,u.locked");
        buffer.append("			from");
        buffer.append("			(");
        buffer.append(
            "				select id, organization_id, username,password,salt,role_id,realname,phone,email,locked from sys_user where username=?");
        buffer.append("			) u");
        buffer.append("			left join sys_role r");
        buffer.append("			on");
        buffer.append("			u.role_id = r.id");
        buffer.append("		) ur");
        buffer.append("		left join sys_organization o");
        buffer.append("		on ur.organization_id = o.id");
        List<User> userList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(User.class), username);
        if (userList.size() == 0)
        {
            return null;
        }
        // 如果是租户管理员，再次查询租户名
        User user = userList.get(0);
        if (user.isRentAdmin())
        {
            List<Rent> rentList = jdbcTemplate.query("select name from sys_rent where id = ?",
                new BeanPropertyRowMapper(Rent.class),
                user.getOrganizationId());
            if (rentList != null && rentList.size() > 0)
            {
                user.setOrganizationName(rentList.get(0).getName());
            }
        }
        
        // 如果是企业管理员，再次查询是否是租车企业管理员
        if (user.isEntAdmin())
        {
            List<EnterpriseTypeModel> enterpriseTypeModelList =
                jdbcTemplate.query("select id,enterprisestype from sys_organization where id = ?",
                    new BeanPropertyRowMapper(EnterpriseTypeModel.class),
                    user.getOrganizationId());
            if (enterpriseTypeModelList != null && enterpriseTypeModelList.size() > 0)
            {
                EnterpriseTypeModel enterpriseTypeModel = enterpriseTypeModelList.get(0);
                if ("0".equals(enterpriseTypeModel.getEnterprisestype()))
                {// 租车公司管理员
                    user.setUserCategory(Long.valueOf(6));
                }
            }
        }
        return user;
    }
    
    @Override
    public User changeUserPwd(User user)
    {
        String sql = "update sys_user set salt=?, username=?,password=? where id=?";
        jdbcTemplate.update(sql, user.getSalt(), user.getUsername(), user.getPassword(), user.getId());
        return user;
    }
    
    @Override
    public List<UserModel> listByOrgId(List<Long> orgIdList)
    {
        StringBuffer orgBuffer = new StringBuffer();
        for (int i = 0; i < orgIdList.size(); i++)
        {
            orgBuffer.append(orgIdList.get(i));
            if (i != orgIdList.size() - 1)
            {
                orgBuffer.append(",");
            }
        }
        
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uo.id,uo.username,uo.organization_id,uo.organization_name,uo.role_id,r.role as role_name, r.template_id as user_category,uo.realname,uo.phone,uo.email,uo.locked");
        buffer.append("		from");
        buffer.append(
            "		(select u.id, u.username,u.organization_id,o.name as organization_name,u.role_id,u.realname,u.phone,u.email,u.locked");
        buffer.append("		from sys_user u");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		u.organization_id = o.id");
        buffer.append("		where u.organization_id in(").append(orgBuffer.toString()).append(")");
        buffer.append("		) uo");
        buffer.append("		left join sys_role r");
        buffer.append("		on");
        buffer.append("		uo.role_id = r.id");
        buffer.append("     and r.template_id in (2,3)");
        
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper<>(UserModel.class));
    }
    
    @Override
    public List<User> findByRoleId(Long roleId)
    {
        String sql =
            "select id, organization_id, username,role_id,realname,phone,email,locked from sys_user where role_id=?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class), roleId);
    }
    
    @Override
    public UserModel findUserModel(Long userId)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "    	select uo.id,uo.username,uo.organization_id,uo.organization_name,uo.role_id,r.role as role_name,r.template_id as user_category,uo.realname,uo.phone,uo.email,uo.locked");
        buffer.append("    	from");
        buffer.append(
            "    	(select u.id, u.username,u.organization_id,o.name as organization_name,u.role_id,u.realname,u.phone,u.email,u.locked");
        buffer.append("    	from sys_user u");
        buffer.append("    	left join sys_organization o");
        buffer.append("    	on");
        buffer.append("    	u.organization_id = o.id where u.id=?) uo");
        buffer.append("    	left join sys_role r");
        buffer.append("    	on");
        buffer.append("    	uo.role_id = r.id");
        List<UserModel> userList =
            jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class), userId);
        if (userList.size() == 0)
        {
            return null;
        }
        return userList.get(0);
    }
    
    @Override
    public UserModel findUserModel(String name)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "    	select uo.id,uo.username,uo.organization_id,uo.organization_name,uo.role_id,r.role as role_name, r.template_id as user_category,uo.realname,uo.phone,uo.email,uo.locked");
        buffer.append("    	from");
        buffer.append(
            "    	(select u.id, u.username,u.organization_id,o.name as organization_name,u.role_id,u.realname,u.phone,u.email,u.locked");
        buffer.append("    	from sys_user u");
        buffer.append("    	left join sys_organization o");
        buffer.append("    	on");
        buffer.append("    	u.organization_id = o.id where u.username=?) uo");
        buffer.append("    	left join sys_role r");
        buffer.append("    	on");
        buffer.append("    	uo.role_id = r.id");
        List<UserModel> userList =
            jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class), name);
        if (userList.size() == 0)
        {
            return null;
        }
        return userList.get(0);
    }
    
    @Override
    public List<UserModel> listEnterpriseAdminListByRentId(Long rentId)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "    	select uo.id,uo.username,uo.organization_id,uo.organization_name,uo.role_id,r.role as role_name, r.template_id as user_category,uo.realname,uo.phone,uo.email,uo.locked");
        buffer.append("   	from");
        buffer.append(
            "   	(select u.id, u.username,u.organization_id,o.name as organization_name,u.role_id,u.realname,u.phone,u.email,u.locked");
        buffer.append("    	from sys_user u");
        buffer.append("    	left join sys_organization o");
        buffer.append("    	on");
        buffer.append("    	u.organization_id = o.id where u.organization_id in (");
        buffer.append("		    select orgid from sys_rent_org where retid = ?");
        buffer.append("		)) uo");
        buffer.append("   	left join sys_role r");
        buffer.append("    	on");
        buffer.append("    	uo.role_id = r.id");
        buffer.append("    	where r.template_id = 1 or r.template_id = 2");// 1:租户管理员 2:企业管理员
        List<UserModel> userList =
            jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class), rentId);
        return userList;
    }
    
    @Override
    public List<UserModel> listOrgAdminListByOrgId(Long orgId)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select sy.id,sy.username,sy.realname,sy.phone,sy.email,sy.role_id,sr.template_id as user_category from sys_user sy ");
        buffer.append("left join sys_role sr on sy.role_id = sr.id ");
        buffer.append("WHERE sy.organization_id in ");
        buffer.append("	(");
        buffer.append("	  select parent_id as ent_id from sys_organization where id = ? ");
        buffer.append("	  union ");
        buffer.append("	  SELECT id as ent_id from sys_organization where id = ? ");
        buffer.append("	  ) ");
        buffer.append("and sr.template_id in (2,3)");// 2:企业管理员 3:部门管理员
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class),orgId,orgId);
    }
    
    @Override
    public User findById(Long id)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select u.id, u.organization_id, u.username,u.role_id,r.template_id as user_category, u.realname,u.phone,u.email,u.locked");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append("		select id, organization_id, username,role_id,realname,phone,email,locked");
        buffer.append("		from sys_user");
        buffer.append("		where id=?");
        buffer.append("		) u");
        buffer.append("		left join sys_role r");
        buffer.append("		on u.role_id = r.id");
        List<User> userList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(User.class), id);
        if (userList.size() == 0)
        {
            return null;
        }
        return userList.get(0);
    }
    
    @Override
    public List<UserModel> listDirectUserListByOrgId(Long orgId)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uo.id,uo.realname,uo.phone,uo.organization_id,uo.role_id,uo.organization_name,r.template_id as user_category");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select u.id,u.realname,u.phone,u.organization_id,u.role_id,o.name as organization_name");
        buffer.append("			from sys_user u");
        buffer.append("			left join sys_organization o");
        buffer.append("			on u.organization_id = o.id");
        buffer.append("			where u.organization_id = ?");
        buffer.append("		)uo");
        buffer.append("		left join sys_role r");
        buffer.append("		on uo.role_id = r.id");
        buffer.append("		where r.template_id = 3 or r.template_id = 4");// 3.部门管理员 4：员工
        List<UserModel> userList =
            jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class), orgId);
        return userList;
    }
    
    @Override
    public List<UserModel> listEnterpriseRootNodeUserListByEntId(Long entId, Long userId)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uo.id,uo.phone,uo.realname,uo.organization_id,uo.role_id,r.template_id as user_category");
        buffer.append("		 from");
        buffer.append("		 (");
        buffer.append("			select u.id,u.phone,u.realname,u.organization_id,u.role_id");
        buffer.append("			from sys_user u");
        buffer.append("			where u.organization_id = ? and u.id != ?");
        buffer.append("		 ) uo");
        buffer.append("		 left join sys_role r");
        buffer.append("		 on uo.role_id = r.id");
//        buffer.append("		 where r.template_id = 2 or r.template_id = 3 or r.template_id = 4");// 2.企业管理员 3.部门管理员 4：员工
        buffer.append("		 where r.template_id = 3 or r.template_id = 4");// 2.企业管理员 3.部门管理员 4：员工
        List<UserModel> userList =
            jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class), entId, userId);
        return userList;
    }
    
    @Override
    public void removeUserToEnterpriseRootNode(Long userId, Long entId)
    {
        String sql = "update sys_user set organization_id=? where id=?";
        jdbcTemplate.update(sql, entId, userId);
    }
    
    @Override
    public void changeUserOrganization(Long orgId, Long userId)
    {
        String sql = "update sys_user set organization_id=? where id=?";
        jdbcTemplate.update(sql, orgId, userId);
    }
    
    @Override
    public void batchChangeUserOrganization(Long orgId, String[] userIds_arr)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("update sys_user set organization_id=? where id in(");
        
        int arr_size = userIds_arr.length;
        for (int i = 0; i < arr_size; i++)
        {
            buffer.append(userIds_arr[i]);
            if (i != arr_size - 1)
            {
                buffer.append(",");
            }
        }
        buffer.append(")");
        jdbcTemplate.update(buffer.toString(), orgId);
    }
    
    @Override
    public User createEmployee(final User user, final Employee emp)
    {
        final String sql =
            "insert into sys_user(organization_id, username, password, salt, role_id,realname,phone,email,locked,idnumber) values(?,?,?,?,?,?,?,?,?,?)";
        
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator()
        {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException
            {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setLong(count++, user.getOrganizationId());
                psst.setString(count++, user.getUsername());
                psst.setString(count++, user.getPassword());
                psst.setString(count++, user.getSalt());
                psst.setLong(count++, user.getRoleId());
                psst.setString(count++, user.getRealname());
                psst.setString(count++, user.getPhone());
                psst.setString(count++, user.getEmail());
                psst.setBoolean(count++, user.getLocked());
                psst.setString(count, user.getIDNumber());
                return psst;
            }
        }, keyHolder);
        
        user.setId(keyHolder.getKey().longValue());
        
        emp.setId(user.getId());
        final String sql1 =
            "insert into sys_employee(id, city, month_limitvalue, order_customer, order_self, order_app, order_web,month_limit_left) values(?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql1,
            emp.getId(),
            emp.getCity(),
            emp.getMonthLimitvalue(),
            emp.getOrderCustomer(),
            emp.getOrderSelf(),
            emp.getOrderApp(),
            emp.getOrderWeb(),
            emp.getMonthLimitLeft());
        	
        return user;
    }
    
    @Override
    public User createDriver(final User user, final Driver driver)
    {
        final String sql =
            "insert into sys_user(organization_id, username, password, salt, role_id,realname,phone,email,locked) values(?,?,?,?,?,?,?,?,?)";
        
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator()
        {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException
            {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setLong(count++, user.getOrganizationId());
                psst.setString(count++, user.getUsername());
                psst.setString(count++, user.getPassword());
                psst.setString(count++, user.getSalt());
                psst.setLong(count++, user.getRoleId());
                psst.setString(count++, user.getRealname());
                psst.setString(count++, user.getPhone());
                psst.setString(count++, user.getEmail());
                psst.setBoolean(count, user.getLocked());
                return psst;
            }
        }, keyHolder);
        
        user.setId(keyHolder.getKey().longValue());
        
        driver.setId(user.getId());
        final String sql1 =
            "insert into sys_driver(id, sex, birthday, age, license_type, license_number,license_begintime,license_expiretime,driving_years,license_attach,dep_id,station_id,drv_status,salary) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql1,
            driver.getId(),
            driver.getSex(),
            driver.getBirthday(),
            driver.getAge(),
            driver.getLicenseType(),
            driver.getLicenseNumber(),
            driver.getLicenseBegintime(),
            driver.getLicenseExpiretime(),
            driver.getDrivingYears(),
            driver.getLicenseAttach(),
            driver.getDepId(),
            driver.getStationId(),
            driver.getDrvStatus(),
                driver.getSalary());
        return user;
    }
    
    @Override
    public List<UserModel> findSuperAdmin()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uo.id,uo.username,uo.organization_id,uo.organization_name,uo.role_id,r.role as role_name,r.template_id as user_category,uo.realname,uo.phone,uo.email,uo.locked");
        buffer.append("    	from");
        buffer.append("    	(");
        buffer.append(
            "			select u.id, u.username,u.organization_id,o.name as organization_name,u.role_id,u.realname,u.phone,u.email,u.locked");
        buffer.append("			from sys_user u");
        buffer.append("			left join sys_organization o");
        buffer.append("			on");
        buffer.append("			u.organization_id = o.id");
        buffer.append("		) uo");
        buffer.append("    	left join sys_role r");
        buffer.append("    	on");
        buffer.append("    	uo.role_id = r.id");
        buffer.append("		where r.template_id <= 0");// 0:超级管理员
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class));
    }
    
    @Override
    public List<UserModel> findRentAdmin()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uo.id,uo.username,uo.organization_id,uo.organization_name,uo.role_id,r.role as role_name,r.template_id as user_category,uo.realname,uo.phone,uo.email,uo.locked");
        buffer.append("    	from");
        buffer.append("    	(");
        buffer.append(
            "			select u.id, u.username,u.organization_id,o.name as organization_name,u.role_id,u.realname,u.phone,u.email,u.locked");
        buffer.append("			from sys_user u");
        buffer.append("			left join sys_rent o");
        buffer.append("			on");
        buffer.append("			u.organization_id = o.id");
        buffer.append("		) uo");
        buffer.append("    	left join sys_role r");
        buffer.append("    	on");
        buffer.append("    	uo.role_id = r.id");
        buffer.append("		where r.template_id = 1");// 1:租户管理员
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class));
    }
    
    @Override
    public List<EmployeeModel> listEmployeeByOrgId(List<Long> orgIdList)
    {
        StringBuffer orgBuffer = new StringBuffer();
        for (int i = 0; i < orgIdList.size(); i++)
        {
            orgBuffer.append(orgIdList.get(i));
            if (i != orgIdList.size() - 1)
            {
                orgBuffer.append(",");
            }
        }
        
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uer.id,uer.realname,uer.username,uer.phone,uer.organization_id,uer.role_id,uer.role_name,uer.month_limitvalue,uer.user_category,o.name as organization_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select ue.id,ue.realname,ue.username,ue.phone,ue.organization_id,ue.role_id,r.role as role_name,ue.month_limitvalue,r.template_id as user_category");
        buffer.append("			from");
        buffer.append("			(");
        buffer.append(
            "				select u.id,u.realname,u.username,u.phone,u.organization_id,u.role_id,emp.month_limitvalue");
        buffer.append("				from sys_user u");
        buffer.append("				left join sys_employee emp");
        buffer.append("				on u.id = emp.id");
        buffer.append("				where");
        buffer.append("				u.organization_id in (").append(orgBuffer.toString()).append(")");
        buffer.append("			) ue");
        buffer.append("			left join sys_role r");
        buffer.append("			on ue.role_id = r.id");
        buffer.append("			where r.template_id = 3 or r.template_id = 4");// 3:部门管理员 4:员工 //部门管理员也是员工
        buffer.append("		) uer");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		uer.organization_id = o.id");
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(EmployeeModel.class));
    }
    
    @Override
    public PagModel listEmployeeByOrgIdByPage(List<Long> orgIdList, String json)
    {
        StringBuffer orgBuffer = new StringBuffer();
        for (int i = 0; i < orgIdList.size(); i++)
        {
            orgBuffer.append(orgIdList.get(i));
            if (i != orgIdList.size() - 1)
            {
                orgBuffer.append(",");
            }
        }
        
        Map<String, Object> jsonMap = null;
        int currentPage = 1;
    	int numPerPage = 20;
        if (StringUtils.isNotBlank(json)) {
        	jsonMap = JsonUtils.json2Object(json, Map.class);
        	if(jsonMap.get("currentPage") != null){
        		currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
        	}
        	if(jsonMap.get("numPerPage") != null){
        		numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
        	}
        }
        
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uer.id,uer.realname,uer.username,uer.phone,uer.organization_id,uer.role_id,uer.role_name,uer.month_limitvalue,uer.user_category,o.name as organization_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select ue.id,ue.realname,ue.username,ue.phone,ue.organization_id,ue.role_id,r.role as role_name,ue.month_limitvalue,r.template_id as user_category");
        buffer.append("			from");
        buffer.append("			(");
        buffer.append(
            "				select u.id,u.realname,u.username,u.phone,u.organization_id,u.role_id,emp.month_limitvalue");
        buffer.append("				from sys_user u");
        buffer.append("				left join sys_employee emp");
        buffer.append("				on u.id = emp.id");
        buffer.append("				where");
        buffer.append("				u.organization_id in (").append(orgBuffer.toString()).append(")");
        buffer.append("			) ue");
        buffer.append("			left join sys_role r");
        buffer.append("			on ue.role_id = r.id");
        buffer.append("			where r.template_id = 3 or r.template_id = 4");// 3:部门管理员 4:员工 //部门管理员也是员工
        buffer.append("		) uer");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		uer.organization_id = o.id");
        buffer.append("		order by uer.id DESC");
        Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,EmployeeModel.class,jdbcTemplate);
        return page.getResult();
    }
    
    @Override
    public List<DriverModel> listAllDriver(String realname)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select ud.id,ud.realname,ud.sex,ud.phone,ud.organization_id,o.name as organization_name,ud.license_type,ud.license_number,ud.driving_years,ud.license_begintime,ud.license_expiretime,ud.dep_name,ud.station_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select u.id,u.realname,driver.sex,u.phone,u.organization_id,driver.license_type,driver.license_number,driver.driving_years,to_char(driver.license_begintime,'yyyy-MM-dd') as license_begintime,to_char(driver.license_expiretime,'yyyy-MM-dd') as license_expiretime");
        buffer.append(
            "		,(select name from sys_organization where id=driver.dep_id) dep_name,(select station_name from busi_station where id=driver.station_id) station_name");
        buffer.append("			from sys_user u,sys_driver driver");
        buffer.append("			where u.id = driver.id");
        if (StringUtils.isNotBlank(realname))
        {
            buffer.append("			and u.realname like "+SqlUtil.processLikeInjectionStatement(realname));
        }
        buffer.append("		) ud");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		ud.organization_id = o.id");
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(DriverModel.class));
    }
    
    @Override
    public List<DriverModel> listDriverByEntId(List<Long> orgIdList, String realname)
    {
        StringBuffer orgBuffer = new StringBuffer();
        for (int i = 0; i < orgIdList.size(); i++)
        {
            orgBuffer.append(orgIdList.get(i));
            if (i != orgIdList.size() - 1)
            {
                orgBuffer.append(",");
            }
        }
        
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select ud.id,ud.realname,ud.sex,ud.phone,ud.organization_id,o.name as organization_name,ud.license_type,ud.license_number,ud.driving_years,ud.salary,ud.license_begintime,ud.license_expiretime,ud.dep_name,ud.station_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select u.id,u.realname,driver.sex,u.phone,u.organization_id,driver.license_type,driver.license_number,driver.driving_years,to_char(driver.license_begintime,'yyyy-MM-dd') as license_begintime,to_char(driver.license_expiretime,'yyyy-MM-dd') as license_expiretime,driver.salary");
        buffer.append(
            "		,(select name from sys_organization where id=driver.dep_id) dep_name,(select station_name from busi_station where id=driver.station_id) station_name");
        buffer.append("			from sys_user u,sys_driver driver");
        buffer.append("			where");
        buffer.append("			u.id = driver.id");
        if (StringUtils.isNotBlank(realname))
        {
            buffer.append("			and u.realname like "+SqlUtil.processLikeInjectionStatement(realname));
        }
        buffer.append("			and u.organization_id in (").append(orgBuffer.toString()).append(")");
        buffer.append("		) ud");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		ud.organization_id = o.id");
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(DriverModel.class));
    }
    
    @Override
    public PagModel listDriverPageByEntId(List<Long> orgIdList, DriverModel driver) {
    	StringBuffer orgBuffer = new StringBuffer();
        for (int i = 0; i < orgIdList.size(); i++)
        {
            orgBuffer.append(orgIdList.get(i));
            if (i != orgIdList.size() - 1)
            {
                orgBuffer.append(",");
            }
        }
        
        int currentPage = 1;
    	int numPerPage = 10;
    	String phone = "";
    	String realname = "";
    	String organizationId = "";
    	if (driver.getCurrentPage() != 0) {
    		currentPage = driver.getCurrentPage();
    	}
    	if (driver.getNumPerPage() != 0) {
    		numPerPage = driver.getNumPerPage();
    	}
    	if (StringUtils.isNotBlank(driver.getPhone())) {
    		phone = driver.getPhone();
    	}
    	if (StringUtils.isNotBlank(driver.getRealname())) {
    		realname = driver.getRealname();
    	}
    	if (driver.getOrganizationId() != null) {
    		organizationId = String.valueOf(driver.getOrganizationId());
    	}

        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select ud.id,ud.realname,ud.sex,ud.phone,ud.organization_id,o.name as organization_name,ud.license_type,ud.license_number,ud.driving_years,ud.license_begintime,ud.drv_status,ud.license_expiretime,ud.dep_id,ud.dep_name,ud.station_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select u.id,u.realname,driver.sex,u.phone,u.organization_id,driver.license_type,driver.license_number,driver.driving_years,driver.dep_id,driver.drv_status,to_char(driver.license_begintime,'yyyy-MM-dd') as license_begintime,to_char(driver.license_expiretime,'yyyy-MM-dd') as license_expiretime");
        buffer.append(
            "		,(select name from sys_organization where id=driver.dep_id) dep_name,(select station_name from busi_station where id=driver.station_id) station_name");
        buffer.append("			from sys_user u,sys_driver driver");
        buffer.append("			where");
        buffer.append("			u.id = driver.id");
        
        //增加司机状态查询
        if(driver.getDrvStatus() != null && driver.getDrvStatus() != -1){
            buffer.append("         and driver.drv_status = ").append(driver.getDrvStatus());
        }
        
        if (StringUtils.isNotBlank(realname))
        {
            buffer.append("			and u.realname like "+SqlUtil.processLikeInjectionStatement(realname));
        }
        if (StringUtils.isNotBlank(phone)) {
        	buffer.append("			and u.phone like "+SqlUtil.processLikeInjectionStatement(phone));
        }
        if (StringUtils.isNotBlank(organizationId)) {
        	buffer.append("			and u.organization_id = ").append(organizationId);
        }
        buffer.append("		) ud");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		ud.organization_id = o.id");
        buffer.append(" LEFT JOIN sys_organization so ON ud.dep_id= so.ID");
        if (null != orgIdList && orgIdList.size() > 0) {
        	buffer.append("		where ud.dep_id in (").append(orgBuffer.toString()).append(")");
        }
        buffer.append(" order by (case when ud.dep_id=-1 then 0 else so.orgindex end),so.orgindex ASC, ud.id desc");
        Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,DriverModel.class,jdbcTemplate);
        return page.getResult();
    }
    
    @Override
    public List<DriverModel> listDriverByDepId(Long depId, String realname)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select ud.id,ud.realname,ud.sex,ud.phone,ud.organization_id,o.name as organization_name,ud.license_type,ud.license_number,ud.driving_years,ud.license_begintime,ud.license_expiretime,ud.dep_name,ud.station_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select u.id,u.realname,driver.sex,u.phone,u.organization_id,driver.license_type,driver.license_number,driver.driving_years,to_char(driver.license_begintime,'yyyy-MM-dd') as license_begintime,to_char(driver.license_expiretime,'yyyy-MM-dd') as license_expiretime");
        buffer.append(
            "		,(select name from sys_organization where id=driver.dep_id) dep_name,(select station_name from busi_station where id=driver.station_id) station_name");
        buffer.append("			from sys_user u,sys_driver driver");
        buffer.append("			where");
        buffer.append("			u.id = driver.id");
        if (StringUtils.isNotBlank(realname))
        {
            buffer.append("			and u.realname like "+SqlUtil.processLikeInjectionStatement(realname));
        }
        buffer.append("			and driver.dep_id = (").append(depId).append(")");
        buffer.append("		) ud");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		ud.organization_id = o.id");
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(DriverModel.class));
    }
    
    @Override
    public List<DriverModel> listDriverByDepId(Long depId)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select ud.id,ud.realname,ud.sex,ud.phone,ud.organization_id,o.name as organization_name,ud.license_type,ud.license_number,ud.driving_years,ud.license_begintime,ud.license_expiretime,ud.dep_name,ud.station_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select u.id,u.realname,driver.sex,u.phone,u.organization_id,driver.license_type,driver.license_number,driver.driving_years,to_char(driver.license_begintime,'yyyy-MM-dd') as license_begintime,to_char(driver.license_expiretime,'yyyy-MM-dd') as license_expiretime");
        buffer.append(
            "		,(select name from sys_organization where id=driver.dep_id) dep_name,(select station_name from busi_station where id=driver.station_id) station_name");
        buffer.append("			from sys_user u,sys_driver driver");
        buffer.append("			where");
        buffer.append("			u.id = driver.id");
        buffer.append("			and driver.dep_id = (").append(depId).append(")");
        buffer.append("		) ud");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		ud.organization_id = o.id");
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(DriverModel.class));
    }
    
    @Override
    public PagModel listDriverPageByDepId(Long depId, DriverModel driver)
    {
        int currentPage = 1;
    	int numPerPage = 10;
    	String phone = "";
    	String realname = "";
    	if (driver.getCurrentPage() != 0) {
    		currentPage = driver.getCurrentPage();
    	} 
    	if (driver.getNumPerPage() != 0) {
    		numPerPage = driver.getNumPerPage();
    	} 
    	if (StringUtils.isNotBlank(driver.getPhone())) {
    		phone = driver.getPhone();
    	}
    	if (StringUtils.isNotBlank(driver.getRealname())) {
    		realname = driver.getRealname();
    	}
    	
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select ud.id,ud.realname,ud.sex,ud.phone,ud.organization_id,o.name as organization_name,ud.license_type,ud.license_number,ud.driving_years,ud.license_begintime,ud.license_expiretime,ud.dep_name,ud.station_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select u.id,u.realname,driver.sex,u.phone,u.organization_id,driver.license_type,driver.license_number,driver.driving_years,to_char(driver.license_begintime,'yyyy-MM-dd') as license_begintime,to_char(driver.license_expiretime,'yyyy-MM-dd') as license_expiretime");
        buffer.append(
            "		,(select name from sys_organization where id=driver.dep_id) dep_name,(select station_name from busi_station where id=driver.station_id) station_name");
        buffer.append("			from sys_user u,sys_driver driver");
        buffer.append("			where");
        buffer.append("			u.id = driver.id");
        if (StringUtils.isNotBlank(realname))
        {
            buffer.append("			and u.realname like "+SqlUtil.processLikeInjectionStatement(realname));
        }
        if (StringUtils.isNotBlank(phone)) {
        	buffer.append("			and u.phone like "+SqlUtil.processLikeInjectionStatement(phone));
        }
        buffer.append("			and driver.dep_id = (").append(depId).append(")");
        buffer.append("		) ud");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		ud.organization_id = o.id");
        buffer.append(" 	order by ud.id desc");
        Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,DriverModel.class,jdbcTemplate);
        return page.getResult();
    }
    
    @Override
    public EmployeeModel findEmployeeModel(Long id)
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(
            "		select ueo.id,ueo.IDNumber,ueo.realname,ueo.username,ueo.phone,ueo.email,r.template_id as user_category,ueo.organization_id,ueo.organization_name,ueo.role_id,r.role as role_name,ueo.city,ueo.month_limitvalue,ueo.order_customer,ueo.order_self,ueo.order_app, ueo.order_web");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select ue.id,ue.IDNumber,ue.realname,ue.username,ue.phone,ue.email,ue.organization_id,o.name as organization_name,ue.role_id,ue.city,ue.month_limitvalue,ue.order_customer,ue.order_self, ue.order_app, ue.order_web");
        buffer.append("			from");
        buffer.append("			(");
        buffer.append(
            "				select u.id,u.IDNumber,u.realname,u.username,u.phone,u.email,u.organization_id,u.role_id,emp.city,emp.month_limitvalue,emp.order_customer,emp.order_self, emp.order_app, emp.order_web");
        buffer.append("				from sys_user u");
        buffer.append("				left join sys_employee emp");
        buffer.append("				on u.id = emp.id");
        buffer.append("				where");
        buffer.append("				u.id=?");
        buffer.append("			) ue");
        buffer.append("			left join sys_organization o");
        buffer.append("			on");
        buffer.append("			ue.organization_id = o.id");
        buffer.append("		) ueo");
        buffer.append("		left join sys_role r");
        buffer.append("		on");
        buffer.append("		ueo.role_id = r.id");
        List<EmployeeModel> empList =
            jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(EmployeeModel.class), id);
        if (empList.size() == 0)
        {
            return null;
        }
        return empList.get(0);
    }
    
    @Override
    public EmployeeModel updateEmployee(EmployeeModel emp)
    {
        String sql =
            "update sys_user set organization_id=?,username=?,IDNumber=?,role_id=?,realname=?,phone=?,email=?,locked=? where id=?";
        jdbcTemplate.update(sql,
            emp.getOrganizationId(),
            emp.getUsername(),
            emp.getIDNumber(),
            emp.getRoleId(),
            emp.getRealname(),
            emp.getPhone(),
            emp.getEmail(),
            emp.getLocked(),
            emp.getId());
        
        String sql1 =
            "update sys_employee set city=?,month_limitvalue=?,order_customer=?,order_self=?,order_app=?, order_web=?, month_limit_left=? where id=?";
        jdbcTemplate.update(sql1,
            emp.getCity(),
            emp.getMonthLimitvalue(),
            emp.getOrderCustomer(),
            emp.getOrderSelf(),
            emp.getOrderApp(),
            emp.getOrderWeb(),
            emp.getMonthLimitLeft(),
            emp.getId());
        return emp;
    }
    
    @Override
    public DriverModel findDriverModel(Long id)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		   select udo.id,udo.realname,udo.username,udo.phone,udo.email,r.template_id as user_category,udo.organization_id,udo.organization_name,udo.role_id,r.role as role_name,");
        buffer.append(
            "			udo.sex,udo.birthday,udo.license_type,udo.license_number,udo.license_begintime,udo.license_expiretime,udo.driving_years,udo.license_attach,udo.dep_id,udo.station_id,udo.drv_status,udo.salary,");
        buffer.append(
            "			(select name from sys_organization where id=udo.dep_id) dep_name,(select station_name from busi_station where id=udo.station_id) station_name");
        buffer.append("			from");
        buffer.append("			(");
        buffer.append(
            "				select ud.id,ud.realname,ud.username,ud.phone,ud.email,ud.organization_id,o.name as organization_name,ud.role_id,");
        buffer.append(
            "				ud.sex,ud.birthday,ud.license_type,ud.license_number,ud.license_begintime,ud.license_expiretime,ud.driving_years,ud.license_attach,ud.dep_id,ud.station_id,ud.drv_status,ud.salary");
        buffer.append("				from");
        buffer.append("				(");
        buffer.append(
            "					select u.id,u.realname,u.username,u.phone,u.email,u.organization_id,u.role_id,");
        buffer.append(
            "					driver.sex,driver.birthday,driver.license_type,driver.license_number,driver.license_begintime,driver.license_expiretime,driver.driving_years,driver.license_attach,driver.dep_id,driver.station_id,driver.drv_status,driver.salary");
        buffer.append("					from sys_user u");
        buffer.append("					left join sys_driver driver");
        buffer.append("					on u.id = driver.id");
        buffer.append("					where");
        buffer.append("					u.id=?");
        buffer.append("				) ud");
        buffer.append("				left join sys_organization o");
        buffer.append("				on");
        buffer.append("				ud.organization_id = o.id");
        buffer.append("			) udo");
        buffer.append("			left join sys_role r");
        buffer.append("			on");
        buffer.append("			udo.role_id = r.id");
        List<DriverModel> driverList =
            jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(DriverModel.class), id);
        if (driverList.size() == 0)
        {
            return null;
        }
        return driverList.get(0);
    }
    
    @Override
    public DriverModel updateDriver(DriverModel driver)
    {
        String sql =
            "update sys_user set organization_id=?,username=?,role_id=?,realname=?,phone=?,email=?,locked=? where id=?";
        jdbcTemplate.update(sql,
            driver.getOrganizationId(),
            driver.getUsername(),
            driver.getRoleId(),
            driver.getRealname(),
            driver.getPhone(),
            driver.getEmail(),
            driver.getLocked(),
            driver.getId());
        
        String sql1 =
            "update sys_driver set sex=?,birthday=?,age=?,license_type=?,license_number=?,license_begintime=?,license_expiretime=?,driving_years=?,license_attach=?,dep_id=?,station_id=?,drv_status=?, salary=? where id=?";
        jdbcTemplate.update(sql1,
            driver.getSex(),
            TimeUtils.getDaytime(driver.getBirthday()),
            driver.getAge(),
            driver.getLicenseType(),
            driver.getLicenseNumber(),
            TimeUtils.getDaytime(driver.getLicenseBegintime()),
            TimeUtils.getDaytime(driver.getLicenseExpiretime()),
            driver.getDrivingYears(),
            driver.getLicenseAttach(),
            driver.getDepId(),
            driver.getStationId(),
            driver.getDrvStatus(),
            driver.getSalary(),
            driver.getId());
        return driver;
    }
    
    @Override
    public List<EmployeeModel> listAllEmployee()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uer.id,uer.realname,uer.username,uer.phone,uer.organization_id,uer.role_id,uer.month_limitvalue,uer.user_category,o.name as organization_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select ue.id,ue.realname,ue.username,ue.phone,ue.organization_id,ue.role_id,ue.month_limitvalue,r.template_id as user_category");
        buffer.append("			from");
        buffer.append("			(");
        buffer.append(
            "				select u.id,u.realname,u.username,u.phone,u.organization_id,u.role_id,emp.month_limitvalue");
        buffer.append("				from sys_user u");
        buffer.append("				left join sys_employee emp");
        buffer.append("				on u.id = emp.id");
        buffer.append("			) ue");
        buffer.append("			left join sys_role r");
        buffer.append("			on ue.role_id = r.id");
        buffer.append("			where r.template_id = 3 or r.template_id = 4");// 3:部门管理员 4:员工 //部门管理员也是员工
        buffer.append("		) uer");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		uer.organization_id = o.id");
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(EmployeeModel.class));
    }
    
    @Override
    public PagModel listAllEmployeeByPage(String json)
    {
    	Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
		int currentPage = 1;
    	int numPerPage = 10;
    	if(jsonMap.get("currentPage") != null){
    		currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
    	}
    	if(jsonMap.get("numPerPage") != null){
    		numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
    	}
    	
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uer.id,uer.realname,uer.username,uer.phone,uer.organization_id,uer.role_id,uer.month_limitvalue,uer.user_category,o.name as organization_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select ue.id,ue.realname,ue.username,ue.phone,ue.organization_id,ue.role_id,ue.month_limitvalue,r.template_id as user_category");
        buffer.append("			from");
        buffer.append("			(");
        buffer.append(
            "				select u.id,u.realname,u.username,u.phone,u.organization_id,u.role_id,emp.month_limitvalue");
        buffer.append("				from sys_user u");
        buffer.append("				left join sys_employee emp");
        buffer.append("				on u.id = emp.id");
        buffer.append("			) ue");
        buffer.append("			left join sys_role r");
        buffer.append("			on ue.role_id = r.id");
        buffer.append("			where r.template_id = 3 or r.template_id = 4");// 3:部门管理员 4:员工 //部门管理员也是员工
        buffer.append("		) uer");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		uer.organization_id = o.id");
        buffer.append(" 	order by uer.id desc");
        Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,UserModel.class,jdbcTemplate);
        return page.getResult();
    }
    
    
    
    @Override
    public void deleteEmployee(Long id)
    {
        String sql = "delete from sys_employee where id=?";
        jdbcTemplate.update(sql, id);
        String sql1 = "delete from sys_user where id=?";
        jdbcTemplate.update(sql1, id);
    }
    
    @Override
    public void deleteDriver(Long id)
    {
        String sql = "delete from sys_driver where id=?";
        jdbcTemplate.update(sql, id);
        String sql1 = "delete from sys_user where id=?";
        jdbcTemplate.update(sql1, id);
    }
    
    @Override
    public List<UserModel> findEntAdmin()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select ur.id,ur.username,ur.realname,ur.phone,ur.email,ur.locked,ur.organization_id,o.name as organization_name,ur.role_id,ur.role_name,ur.user_category");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select u.id,u.username,u.realname,u.phone,u.email,u.locked,u.organization_id,u.role_id,r.role as role_name,r.template_id as user_category");
        buffer.append("			from sys_user u");
        buffer.append("			left join sys_role r");
        buffer.append("			on");
        buffer.append("			u.role_id = r.id");
        buffer.append("			where r.template_id = 2");// 2:企业管理员
        buffer.append("		) ur");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("    	ur.organization_id = o.id ");
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class));
    }
    
    @Override
    public UserModel findEntUserModelById(Long id)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uo.id,uo.username,uo.organization_id,uo.organization_name,uo.role_id,r.role as role_name,r.template_id as user_category,uo.realname,uo.phone,uo.email,uo.locked");
        buffer.append("    	from");
        buffer.append("    	(");
        buffer.append(
            "			select u.id, u.username,u.organization_id,o.name as organization_name,u.role_id,u.realname,u.phone,u.email,u.locked");
        buffer.append("			from");
        buffer.append("			sys_user u,sys_rent o");
        buffer.append("			where");
        buffer.append("			u.organization_id = o.id");
        buffer.append("			and u.id = ?");
        buffer.append("		) uo");
        buffer.append("   	left join sys_role r");
        buffer.append("   	on");
        buffer.append("   	uo.role_id = r.id");
        List<UserModel> rentList =
            jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class), id);
        if (rentList != null && rentList.size() > 0)
        {
            return rentList.get(0);
        }
        return null;
    }
    
    @Override
    public List<EmployeeModel> listAllEmployeeMathRealname(String realname)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uer.id,uer.realname,uer.username,uer.phone,uer.organization_id,uer.role_id,uer.month_limitvalue,uer.user_category,o.name as organization_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select ue.id,ue.realname,ue.username,ue.phone,ue.organization_id,ue.role_id,ue.month_limitvalue,r.template_id as user_category");
        buffer.append("			from");
        buffer.append("			(");
        buffer.append(
            "				select u.id,u.realname,u.username,u.phone,u.organization_id,u.role_id,emp.month_limitvalue");
        buffer.append("				from sys_user u");
        buffer.append("				left join sys_employee emp");
        buffer.append("				on u.id = emp.id");
        buffer.append("				where u.realname like "+SqlUtil.processLikeInjectionStatement(realname));
        buffer.append("			) ue");
        buffer.append("			left join sys_role r");
        buffer.append("			on ue.role_id = r.id");
        buffer.append("			where r.template_id = 3 or r.template_id = 4");// 3:部门管理员 4:员工 //部门管理员也是员工
        buffer.append("		) uer");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		uer.organization_id = o.id");
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(EmployeeModel.class));
    }
    
    @Override
    public PagModel listAllEmployeeMathRealnameByPage(String realname,String json)
    {
    	Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);

		int currentPage = 1;
    	int numPerPage = 10;
    	if(jsonMap.get("currentPage") != null){
    		currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
    	}
    	if(jsonMap.get("numPerPage") != null){
    		numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
    	}
    	
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uer.id,uer.realname,uer.username,uer.phone,uer.organization_id,uer.role_id,uer.month_limitvalue,uer.user_category,o.name as organization_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select ue.id,ue.realname,ue.username,ue.phone,ue.organization_id,ue.role_id,ue.month_limitvalue,r.template_id as user_category");
        buffer.append("			from");
        buffer.append("			(");
        buffer.append(
            "				select u.id,u.realname,u.username,u.phone,u.organization_id,u.role_id,emp.month_limitvalue");
        buffer.append("				from sys_user u");
        buffer.append("				left join sys_employee emp");
        buffer.append("				on u.id = emp.id");
        buffer.append("				where u.realname like "+SqlUtil.processLikeInjectionStatement(realname));
        buffer.append("			) ue");
        buffer.append("			left join sys_role r");
        buffer.append("			on ue.role_id = r.id");
        buffer.append("			where r.template_id = 3 or r.template_id = 4");// 3:部门管理员 4:员工 //部门管理员也是员工
        buffer.append("		) uer");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		uer.organization_id = o.id");
        Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,EmployeeModel.class,jdbcTemplate);
        return page.getResult();
    }
    
    @Override
    public List<EmployeeModel> listEmployeeByOrgIdMathRealname(List<Long> orgIdList, String realname)
    {
        StringBuffer orgBuffer = new StringBuffer();
        for (int i = 0; i < orgIdList.size(); i++)
        {
            orgBuffer.append(orgIdList.get(i));
            if (i != orgIdList.size() - 1)
            {
                orgBuffer.append(",");
            }
        }
        
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uer.id,uer.realname,uer.username,uer.phone,uer.organization_id,uer.role_id,uer.month_limitvalue,uer.user_category,o.name as organization_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select ue.id,ue.realname,ue.username,ue.phone,ue.organization_id,ue.role_id,ue.month_limitvalue,r.template_id as user_category");
        buffer.append("			from");
        buffer.append("			(");
        buffer.append(
            "				select u.id,u.realname,u.username,u.phone,u.organization_id,u.role_id,emp.month_limitvalue");
        buffer.append("				from sys_user u");
        buffer.append("				left join sys_employee emp");
        buffer.append("				on u.id = emp.id");
        buffer.append("				where");
        buffer.append("				u.realname like "+SqlUtil.processLikeInjectionStatement(realname));
        buffer.append("				and u.organization_id in (").append(orgBuffer.toString()).append(")");
        buffer.append("			) ue");
        buffer.append("			left join sys_role r");
        buffer.append("			on ue.role_id = r.id");
        buffer.append("			where r.template_id = 3 or r.template_id = 4");// 3:部门管理员 4:员工 //部门管理员也是员工
        buffer.append("		) uer");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		uer.organization_id = o.id");
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(EmployeeModel.class));
    }
    
    @Override
    public PagModel listEmployeeByOrgIdByPageInSearch(List<Long> orgIdList, EmployeeModel emp)
    {
        StringBuffer orgBuffer = new StringBuffer();
        for (int i = 0; i < orgIdList.size(); i++)
        {
            orgBuffer.append(orgIdList.get(i));
            if (i != orgIdList.size() - 1)
            {
                orgBuffer.append(",");
            }
        }
        
        int currentPage = 1;
        int numPerPage = 10;
        String realname = "";
        String phone = "";
        String username = "";
        String roleId = "";
        if (emp.getCurrentPage() != 0) {
        	currentPage = emp.getCurrentPage();
        }
        if (emp.getNumPerPage() != 0) {
        	numPerPage = emp.getNumPerPage();
        }
        if (StringUtils.isNotBlank(emp.getPhone())) {
        	phone = emp.getPhone();
        }
        if (StringUtils.isNotBlank(emp.getRealname())) {
        	realname = emp.getRealname();
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uer.id,uer.IDNumber,uer.realname,uer.username,uer.phone,uer.organization_id,uer.role_id,uer.month_limitvalue,uer.user_category,uer.role_name,o.name as organization_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select ue.id,ue.IDNumber,ue.realname,ue.username,ue.phone,ue.organization_id,ue.role_id,ue.month_limitvalue,r.template_id as user_category,r.role as role_name");
        buffer.append("			from");
        buffer.append("			(");
        buffer.append(
            "				select u.id,u.IDNumber,u.realname,u.username,u.phone,u.organization_id,u.role_id,emp.month_limitvalue");
        buffer.append("				from sys_user u");
        buffer.append("				left join sys_employee emp");
        buffer.append("				on u.id = emp.id");
        buffer.append("				where");
        buffer.append("				u.realname like "+SqlUtil.processLikeInjectionStatement(realname));
        buffer.append("				and u.phone like "+SqlUtil.processLikeInjectionStatement(phone));
        buffer.append("				and u.organization_id in (").append(orgBuffer.toString()).append(")");
        
        if (StringUtils.isNotBlank(emp.getUsername())) {
        	username = String.valueOf(emp.getUsername());
        	buffer.append("			and u.username like "+SqlUtil.processLikeInjectionStatement(username));
        }
        if (emp.getRoleId() != null) {
        	roleId = String.valueOf(emp.getRoleId());
        	buffer.append("			and u.role_id = ").append(roleId);
        }
        
        buffer.append("			) ue");
        buffer.append("			left join sys_role r");
        buffer.append("			on ue.role_id = r.id");
//        buffer.append("			where r.template_id = 3 or r.template_id = 4");// 3:部门管理员 4:员工 //部门管理员也是员工
        buffer.append("         where r.template_id in (3,4,12,13,14)");// 3:部门管理员 4:员工 //部门管理员也是员工
        buffer.append("		) uer");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		uer.organization_id = o.id");
        buffer.append(" order by o.orgindex ASC, uer.id desc");
        Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,EmployeeModel.class,jdbcTemplate);
        return page.getResult();
    }
    
    @Override
    public List<DriverModel> listAllDriverMathRealname(String realname)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select ud.id,ud.realname,ud.sex,ud.phone,ud.organization_id,o.name as organization_name,ud.license_type,ud.license_number,ud.driving_years,ud.license_begintime,ud.license_expiretime,ud.dep_name,ud.station_name");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select u.id,u.realname,driver.sex,u.phone,u.organization_id,driver.license_type,driver.license_number,driver.driving_years,to_char(driver.license_begintime,'yyyy-MM-dd') as license_begintime,to_char(driver.license_expiretime,'yyyy-MM-dd') as license_expiretime");
        buffer.append(
            "		,(select name from sys_organization where id=driver.dep_id) dep_name,(select station_name from busi_station where id=driver.station_id) station_name");
        buffer.append("			from sys_user u,sys_driver driver");
        buffer.append("			where u.id = driver.id");
        buffer.append("			and u.realname like "+SqlUtil.processLikeInjectionStatement(realname));
        buffer.append("		) ud");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("		ud.organization_id = o.id");
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(DriverModel.class));
    }
    
    @Override
    public List<UserModel> listEnterpriseAdminListByRentIdMatchRealname(Long rentId, String realname)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "    	select uo.id,uo.username,uo.organization_id,uo.organization_name,uo.role_id,r.role as role_name, r.template_id as user_category,uo.realname,uo.phone,uo.email,uo.locked");
        buffer.append("   	from");
        buffer.append(
            "   	(select u.id, u.username,u.organization_id,o.name as organization_name,u.role_id,u.realname,u.phone,u.email,u.locked");
        buffer.append("    	from sys_user u");
        buffer.append("    	left join sys_organization o");
        buffer.append("    	on");
        buffer.append("    	u.organization_id = o.id where u.realname like "+SqlUtil.processLikeInjectionStatement(realname))
            .append("  and u.organization_id in (");
        buffer.append("		    select orgid from sys_rent_org where retid = ?");
        buffer.append("		)) uo");
        buffer.append("   	left join sys_role r");
        buffer.append("    	on");
        buffer.append("    	uo.role_id = r.id");
        buffer.append("    	where r.template_id = 1 or r.template_id = 2");// 1:租户管理员 2:企业管理员
        List<UserModel> userList =
            jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class), rentId);
        return userList;
    }
    
    @Override
    public List<UserModel> findSuperAdminMatchRealname(String realname)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uo.id,uo.username,uo.organization_id,uo.organization_name,uo.role_id,r.role as role_name,r.template_id as user_category,uo.realname,uo.phone,uo.email,uo.locked");
        buffer.append("    	from");
        buffer.append("    	(");
        buffer.append(
            "			select u.id, u.username,u.organization_id,o.name as organization_name,u.role_id,u.realname,u.phone,u.email,u.locked");
        buffer.append("			from sys_user u");
        buffer.append("			left join sys_organization o");
        buffer.append("			on");
        buffer.append("			u.organization_id = o.id where u.realname like '%").append(realname).append("%'");
        buffer.append("		) uo");
        buffer.append("    	left join sys_role r");
        buffer.append("    	on");
        buffer.append("    	uo.role_id = r.id");
        buffer.append("		where r.template_id = 0");// 0:超级管理员
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class));
    }
    
    @Override
    public List<UserModel> findRentAdminMatchRealname(String realname)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uo.id,uo.username,uo.organization_id,uo.organization_name,uo.role_id,r.role as role_name,r.template_id as user_category,uo.realname,uo.phone,uo.email,uo.locked");
        buffer.append("    	from");
        buffer.append("    	(");
        buffer.append(
            "			select u.id, u.username,u.organization_id,o.name as organization_name,u.role_id,u.realname,u.phone,u.email,u.locked");
        buffer.append("			from sys_user u");
        buffer.append("			left join sys_rent o");
        buffer.append("			on");
        buffer.append("			u.organization_id = o.id where u.realname like "+SqlUtil.processLikeInjectionStatement(realname));
        buffer.append("		) uo");
        buffer.append("    	left join sys_role r");
        buffer.append("    	on");
        buffer.append("    	uo.role_id = r.id");
        buffer.append("		where r.template_id = 1");// 1:租户管理员
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class));
    }
    
    @Override
    public List<UserModel> findEntAdminMatchRealname(String realname)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select ur.id,ur.username,ur.realname,ur.phone,ur.email,ur.locked,ur.organization_id,o.name as organization_name,ur.role_id,ur.role_name,ur.user_category");
        buffer.append("		from");
        buffer.append("		(");
        buffer.append(
            "			select u.id,u.username,u.realname,u.phone,u.email,u.locked,u.organization_id,u.role_id,r.role as role_name,r.template_id as user_category");
        buffer.append("			from sys_user u");
        buffer.append("			left join sys_role r");
        buffer.append("			on");
        buffer.append("			u.role_id = r.id");
        buffer.append("			where u.realname like ").append(SqlUtil.processLikeInjectionStatement(realname)).append(" and r.template_id = 2");// 2:企业管理员
        buffer.append("		) ur");
        buffer.append("		left join sys_organization o");
        buffer.append("		on");
        buffer.append("    	ur.organization_id = o.id ");
        return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class));
    }
    
    @Override
    public boolean usernameIsValid(String username)
    {
        List<UserModel> userList = jdbcTemplate.query("select id,username from sys_user where username=?",
            new BeanPropertyRowMapper(UserModel.class),
            username);
        if (userList != null && userList.size() > 0)
        {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean emialIsValid(String email)
    {
        List<UserModel> userList = jdbcTemplate.query("select id,email from sys_user where email=? and (email<>'' and email is not null)",
            new BeanPropertyRowMapper(UserModel.class),
            email);
        if (userList != null && userList.size() > 0)
        {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean phoneIsValid(String phone)
    {
        List<UserModel> userList = jdbcTemplate.query("select id,phone from sys_user where phone=?",
            new BeanPropertyRowMapper(UserModel.class),
            phone);
        if (userList != null && userList.size() > 0)
        {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean licenseNumberIsValid(String licenseNumber)
    {
        List<DriverModel> userList =
            jdbcTemplate.query("select id,license_number from sys_driver where license_number=?",
                new BeanPropertyRowMapper(DriverModel.class),
                licenseNumber);
        if (userList != null && userList.size() > 0)
        {
            return false;
        }
        return true;
    }
    
    @Override
    public void changeUserInfo(Long id, String phone, String email, String realname)
    {
        String sql = "update sys_user set phone=?,email=?,realname=? where id=?";
        jdbcTemplate.update(sql, phone, email, realname, id);
    }
    
    @Override
    public User findByPhoneNumber(String phoneNumber)
    {
        String sql =
            "select u.*, template_id as userCategory from sys_user u left join sys_role r on u.role_id=r.id where phone = ?";
        List<User> user = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class), phoneNumber);
        if (user.isEmpty())
        {
            return null;
        }
        else
        {
            return user.get(0);
        }
        
    }
    
    @Override
    public User findByEmail(String email)
    {
        String sql = "select * from sys_user where email = ?";
        List<User> user = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class), email);
        if (user.isEmpty())
        {
            return null;
        }
        else
        {
            return user.get(0);
        }
        
    }
    
    @Override
    public void saveCode(PhoneVerificationCode code)
    {
        String sql = "insert into phone_verificationCode values(?,?,?)";
        jdbcTemplate.update(sql, code.getPhoneNumber(), code.getCode(), code.getExpirationTime());
    };
    
    public PhoneVerificationCode getCode(String phoneNumber)
    {
        String sql = "select * from phone_verificationCode where phoneNumber = ?";
        List<PhoneVerificationCode> phoneCode =
            jdbcTemplate.query(sql, new BeanPropertyRowMapper(PhoneVerificationCode.class), phoneNumber);
        if (phoneCode.isEmpty())
        {
            return null;
        }
        else
        {
            return phoneCode.get(0);
        }
    }
    
    @Override
    public void updateCode(PhoneVerificationCode code)
    {
        String sql = "update phone_verificationCode set code=?,expiration_time=? where phoneNumber = ?";
        jdbcTemplate.update(sql, code.getCode(), code.getExpirationTime(), code.getPhoneNumber());
        
    }
    
    public PhoneVerificationCode checkCode(String phoneNumber)
    {
        String sql = "select * from phone_verificationCode where phoneNumber = ? and now()<expiration_time";
        List<PhoneVerificationCode> phoneCode =
            jdbcTemplate.query(sql, new BeanPropertyRowMapper(PhoneVerificationCode.class), phoneNumber);
        if (phoneCode.isEmpty())
        {
            return null;
        }
        else
        {
            return phoneCode.get(0);
        }
    }
    
    @Override
    public User findEntAdmin(Long entId)
    {
        String sql =
            "select * from sys_user a inner join sys_role b on a.role_id=b.id where b.template_id=2 and a.organization_id=?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class), entId);
        if (!users.isEmpty())
        {
            return users.get(0);
        }
        return null;
    }
    
    @Override
    public void updateUserInfoApp(String sql)
    {
        jdbcTemplate.update(sql);
    }
    
    @Override
    public Driver findDriverById(Long id)
    {
        String sql = "select * from sys_driver where id=?";
        List<Driver> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Driver.class), id);
        if (!users.isEmpty())
        {
            return users.get(0);
        }
        return null;
    }
    
    @Override
    public void updateAccessToken(Long userId, String token)
    {
        String sql = "update sys_user set token = ? where id = ?";
        jdbcTemplate.update(sql, token, userId);
    }
    
    @Override
    public boolean validateAccessToken(Long userId, String token)
    {
        String sql = "select * from sys_user where id=? and token = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class), userId, token);
        if (!users.isEmpty())
        {
            return true;
        }
        return false;
    }
    
    @Override
    public Employee findEmployeeByUserId(Long id)
    {
        // TODO Auto-generated method stub
        String sql =
            "select t.id as id, t.city as city, t.month_limitvalue as monthLimitvalue, t.order_customer as orderCustomer,"
                + " t.order_self as orderSelf, t.order_app as orderApp, t.order_web as orderWeb,t.month_limit_left as monthLimitLeft"
                + " from sys_employee t where t.id = ? ";
        List<Employee> employees = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Employee.class), id);
        if (null != employees && !employees.isEmpty())
        {
            return employees.get(0);
        }
        return null;
    }
    
    @Override
    public void updateEmployee(Employee employee)
    {
        // TODO Auto-generated method stub
        String sql = "update sys_employee set city = ? ,month_limitvalue = ? , order_customer = ?,"
            + " order_self = ? , order_app = ?, order_web = ?, month_limit_left = ? " + "where id = ?";
        jdbcTemplate.update(sql,
            employee.getCity(),
            employee.getMonthLimitvalue(),
            employee.getOrderCustomer(),
            employee.getOrderSelf(),
            employee.getOrderApp(),
            employee.getOrderWeb(),
            employee.getMonthLimitLeft(),
            employee.getId());
    }
    
    @Override
    public List<User> queryUserListByOrgId(Long orgId) {
    	String sql = "select * from sys_user where organization_id = ?";
    	return jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class), orgId);
    }
    
    public List<String> listUserListByOrgId(List<Long> orgIdList,Long roleId)
    {
        
    	StringBuffer orgIdsStr = new StringBuffer();
	    for(Long id : orgIdList){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
        String sql="select u.phone from sys_user u where u.role_id =? and u.organization_id in ("+orgIdsStr.toString()+")";
        List<String> userList = jdbcTemplate.queryForList(sql, String.class, roleId);
        return userList;
    }
    
    //查询该企业下的所有用户
    @Override
    public List<String> listUserListByOrgId(Long entId,Long roleId)
    {
        /*StringBuffer buffer = new StringBuffer();
        buffer.append("select u.id,u.username,u.realname,u.phone,u.email,u.role_id from sys_user u where u.organization_id=? or u.organization in");
        buffer.append(" (select id from sys_organization where parent_id=?)");
        List<UserModel> userList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class), entId);*/
        StringBuffer buffer = new StringBuffer();
        buffer.append("select u.phone from sys_user u where u.role_id =? and (u.organization_id=? or u.organization_id in");
        buffer.append(" (select id from sys_organization where parent_id=?))");
        List<String> userList = jdbcTemplate.queryForList(buffer.toString(), String.class, roleId,entId,entId);
        return userList;
    }

	@Override
	public PagModel findAllAdmin(UserModel user) {
		int currentPage = user.getCurrentPage();
		int numPerPage = user.getNumPerPage();
		List<Object> params = new ArrayList<Object>();
		StringBuffer buffer = new StringBuffer();
        buffer.append(
            "		select uo.id,uo.username,uo.organization_id,uo.organization_name,uo.role_id,r.role as role_name,r.template_id as user_category,uo.realname,uo.phone,uo.email,uo.locked");
        buffer.append("    	from");
        buffer.append("    	(");
        buffer.append(
            "			select u.id, u.username,u.organization_id,o.name as organization_name,u.role_id,u.realname,u.phone,u.email,u.locked");
        buffer.append("			from sys_user u");
        buffer.append("			left join sys_organization o");
        buffer.append("			on");
        buffer.append("			u.organization_id = o.id");
        buffer.append("		) uo");
        buffer.append("    	left join sys_role r");
        buffer.append("    	on");
        buffer.append("    	uo.role_id = r.id");
        buffer.append("		where r.template_id in (-1,0,2)");// 0:超级管理员
        if (StringUtils.isNotBlank(user.getRealname())) {
			buffer.append(" and uo.realname like "+SqlUtil.processLikeInjectionStatement(user.getRealname()));
		} 
        buffer.append(" order by uo.id desc");
        Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,UserModel.class,jdbcTemplate, params.toArray());
		PagModel pageModel = page.getResult();
		return pageModel;  
	}
	
	@Override
	public List<User> findUserListByOrgId(Long orgId,Long roleId) {
		String sql = "select * from sys_user "
				   + "where role_id = ? and organization_id = ? ";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class) , roleId, orgId);
	}
	
	@Override
	public List<UserModel> findAllChUser(String mobile) { 
	    StringBuffer buffer = new StringBuffer();
	    buffer.append(
	        "    	select uo.id,uo.username,uo.organization_id,uo.organization_name,uo.role_id,r.role as role_name, r.template_id as user_category,uo.realname,uo.phone,uo.email,uo.locked");
	    buffer.append("    	from");
	    buffer.append(
	        "    	(select u.id, u.username,u.organization_id,o.name as organization_name,u.role_id,u.realname,u.phone,u.email,u.locked");
	    buffer.append("    	from sys_user u");
	    buffer.append("    	left join sys_organization o");
	    buffer.append("    	on");
	    buffer.append("    	u.organization_id = o.id where u.organization_id = 0 and role_id = 6 ");
	    if(mobile != null && !"".equals(mobile))
	        buffer.append((new StringBuilder()).append(" and u.phone like ").append(SqlUtil.processLikeInjectionStatement(mobile)).toString());
	    
	    buffer.append(" ) uo");
	    buffer.append("	left join sys_role r");
	    buffer.append("	on");
	    buffer.append("	uo.role_id = r.id");
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UserModel.class));   
	}

	@Override
	public Boolean isDriverAllacateVehicle(Long driverId) {
		List<Object> params = new ArrayList<Object>();
		params.add(driverId);
		StringBuilder sql = new StringBuilder();
		sql.append("select vehicle_id id from busi_vehicle_driver where driver_id = ?");
		List<VehicleModel> retList = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(VehicleModel.class), params.toArray());
		if (retList !=null && retList.size()>0) {
			return true;
		}
		return false;
	}

	@Override
	public void updateDriverAndVehicle(Long driverId) {
		List<Object> params = new ArrayList<Object>();
		params.add(driverId);
		StringBuilder sql = new StringBuilder();
		sql.append("update busi_vehicle_driver set driver_id=-1 where driver_id = ?");
		jdbcTemplate.update(sql.toString(), params.toArray());
	}
	
	@Override
	public PagModel listDepDriverByEntIdDepId(Long entId, Long depId, DriverModel driver) {
		int currentPage = 1;
		int numPerPage = 10;
		String phone = "";
		String realname = "";
		String userName = "";
		if (driver.getCurrentPage() != 0) {
			currentPage = driver.getCurrentPage();
		} 
		if (driver.getNumPerPage() != 0) {
			numPerPage = driver.getNumPerPage();
		} 
		if (StringUtils.isNotBlank(driver.getPhone())) {
			phone = driver.getPhone();
		}
		if (StringUtils.isNotBlank(driver.getRealname())) {
			realname = driver.getRealname();
		}
		if (StringUtils.isNotBlank(driver.getUsername())) {
			userName = driver.getUsername();
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("		select u.id,u.username,u.realname,driver.sex,driver.age,u.phone,driver.driving_years,to_char(driver.license_expiretime,'yyyy-MM-dd') as license_expiretime");
		buffer.append("		from sys_user u,sys_driver driver");
		buffer.append("		where");
		buffer.append("		u.id = driver.id");
		buffer.append("		and driver.dep_id = ").append(depId);
		buffer.append("		and u.organization_id = ").append(entId);
		if (StringUtils.isNotBlank(realname))
		{
			buffer.append("		and u.realname like "+SqlUtil.processLikeInjectionStatement(realname));
		}
		if (StringUtils.isNotBlank(userName))
		{
			buffer.append("		and u.userName like "+SqlUtil.processLikeInjectionStatement(userName));
		}
		if (StringUtils.isNotBlank(phone)) {
			buffer.append("		and u.phone like "+SqlUtil.processLikeInjectionStatement(phone));
		}
		buffer.append(" 	order by u.id desc");
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,DriverModel.class,jdbcTemplate);
		return page.getResult();
	}
	
	@Override
	public PagModel listUnallocatedDepEmployee(Long entId, EmployeeModel emp) {
		int currentPage = 1;
		int numPerPage = 10;
		String phone = "";
		String realname = "";
		String userName = "";
		if (emp.getCurrentPage() != 0) {
			currentPage = emp.getCurrentPage();
		} 
		if (emp.getNumPerPage() != 0) {
			numPerPage = emp.getNumPerPage();
		}
		if (StringUtils.isNotBlank(emp.getPhone())) {
			phone = emp.getPhone();
		}
		if (StringUtils.isNotBlank(emp.getRealname())) {
			realname = emp.getRealname();
		}
		if (StringUtils.isNotBlank(emp.getUsername())) {
			userName = emp.getUsername();
		}
		
		StringBuffer buffer = new StringBuffer();

		buffer.append("		select u.id,u.username,u.realname,u.phone,r.id role_id,r.role role_name");
		buffer.append("		from sys_user u");
		buffer.append("		LEFT JOIN sys_organization o");
		buffer.append("		on o.id = u.organization_id");
		buffer.append("		LEFT JOIN sys_role r");
		buffer.append("		on r.id = u.role_id");
		buffer.append("		where o.parent_id = 0");
		buffer.append("		and (r.template_id = 3 or r.template_id = 4)");
		if (null != entId) {
			buffer.append("		and u.organization_id = ").append(entId);
		}
		if (StringUtils.isNotBlank(emp.getUsername())) {
			buffer.append("		and u.userName like "+SqlUtil.processLikeInjectionStatement(userName));
		}
		if (StringUtils.isNotBlank(emp.getRealname())) {
			buffer.append("		and u.realname like "+SqlUtil.processLikeInjectionStatement(realname));
		}
		if (StringUtils.isNotBlank(emp.getPhone())) {
			buffer.append("		and u.phone like "+SqlUtil.processLikeInjectionStatement(phone));
		}
		buffer.append("		order by u.id desc");
		
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,EmployeeModel.class,jdbcTemplate);
		return page.getResult();
	}
	
	@Override
    public void updateDepToEmployee(AllocateDepModel model) {
		StringBuffer sql = new StringBuffer();
		List<Object> params=new ArrayList<Object>();
		StringBuffer preparams = new StringBuffer();
		
		sql.append("update sys_user");
		
		if(null != model.getAllocateDepId()){
			sql.append(" set organization_id = ?");
			params.add(model.getAllocateDepId());
		}
		
		for(int i=0,num=model.getIdArray().length;i<num;i++){
			if(i==num-1){
				preparams.append("?");
			}else{
				preparams.append("?,");
			}
			params.add(model.getIdArray()[i]);
		}
		sql.append(" where id in (").append(preparams).append(")");
		jdbcTemplate.update(sql.toString(), params.toArray());
    }
}
