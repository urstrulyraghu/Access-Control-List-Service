
package com.accolite.miniau.accesscontrol.daoimpl;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;

import com.accolite.miniau.accesscontrol.dao.UserDAO;
import com.accolite.miniau.accesscontrol.enums.UserType;
import com.accolite.miniau.accesscontrol.mapper.PermissionMapper;
import com.accolite.miniau.accesscontrol.mapper.UserMapper;
import com.accolite.miniau.accesscontrol.model.Permission;
import com.accolite.miniau.accesscontrol.model.User;
import com.accolite.miniau.accesscontrol.utility.HashUtility;
import com.accolite.miniau.accesscontrol.utility.MailUtility;
import com.accolite.miniau.accesscontrol.utility.Query;
import com.accolite.miniau.accesscontrol.utility.UriUtility;

public class UserDAOImpl implements UserDAO {

	@Autowired
	UriUtility uriUtil;

	@Autowired
	MailUtility mailUtil;

	private JdbcTemplate jdbcTemplate;
	private static final Logger logger = Logger.getLogger(com.accolite.miniau.accesscontrol.daoimpl.UserDAOImpl.class);

	@Override
	public boolean addNewUser(User user) {

		int rowsAffected;
		try {
			rowsAffected = jdbcTemplate.update(Query.ADDNEWUSER, user.getUserName(), user.getMailId());

		} catch (Exception e) {
			logger.error("Error creating User", e);
			rowsAffected = 0;
		}
		if (rowsAffected == 0) {
			logger.error("couldn't insert" + user.getUserName() + " into the user table");
			return false;
		}
		logger.info("inserted " + user.getUserId() + "into user table successfully");
		return true;
	}

	@Override
	public User getUser(int userId) {

		try {
			return jdbcTemplate.queryForObject(Query.GETUSER, new Object[] { userId }, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	@Override
	public boolean deleteUser(int userId) {
		int rowsAffected = jdbcTemplate.update(Query.DELETEUSER, userId);
		if (rowsAffected == 0) {
			logger.error("failed to delete user " + userId);
			return false;
		}
		logger.info("deleted user " + userId + "in both the user and user_group tables");
		return true;
	}

	@Override
	public List<User> getAllUsers() {

		logger.info("performing get all users operation");
		return jdbcTemplate.query(Query.GETALLUSERS, new BeanPropertyRowMapper<User>(User.class));

	}

	@Override
	public void setDataSource(DataSource dataSource) {

		jdbcTemplate = new JdbcTemplate(dataSource);

	}

	@Override
	public List<String> getAllUserNames() {
		logger.info("performing get all usernames operation");
		return jdbcTemplate.queryForList(Query.GETALLUSERNAMES, String.class);

	}

	@Override
	public boolean addPermissionToUser(int userId, int permissionId) {
		// TODO--review on update--exception
		int rowsAffected = jdbcTemplate.update(Query.ADDPERMISSIONTOUSER, userId, permissionId);
		if (rowsAffected == 0) {
			logger.info("failed to add permission " + permissionId + " to user");
			return false;
		}
		logger.info("successfully added permission" + permissionId + " to user");
		return true;
	}

	@Override
	public boolean removePermissionFromUser(int userId, int permissionId) {
		int rowsAffected = jdbcTemplate.update(Query.REMOVEPERMISSIONFROMUSER, userId, permissionId);
		if (rowsAffected == 0) {
			logger.info("failed to delete permission" + permissionId + " from user" + userId);
			return false;
		}
		logger.info("successfully deleted permission " + permissionId + "from user" + userId);
		return true;
	}

	@Override
	public List<Permission> getPermissionOfUser(int userId) {
		return jdbcTemplate.query(Query.GETUSERPERMISSIONS, new Object[] { userId }, new PermissionMapper());

	}

	@Override
	public boolean updatePassword(int userId, String password) {
		int rowsAffected = jdbcTemplate.update(Query.UPDATEPASSWORD, password, userId);
		if (rowsAffected == 0) {
			logger.info("failed to update passsword for user" + userId);
			return false;
		}
		logger.info("successfully updated password for user" + userId);
		return true;

	}

	@Override
	public int validateUser(User user) {
		String sql = "";
		// TODO
		return 0;
	}

	@Override
	public Integer getUserIdFromURI(String uri) {
		String sql = "SELECT USER_ID FROM USER_PASSWORD_URI WHERE URI=?";
		Integer userId;
		try {
			userId = jdbcTemplate.queryForObject(sql, new Object[] { uri }, Integer.class);
		} catch (Exception e) {
			userId = 0;
		}
		return userId;
	}

	@Override
	public Integer getUserIdUsingEmail(String email) {
		String sql = "SELECT USER_ID FROM ADMIN WHERE MAIL_ID = ?";
		Integer userId;
		try {
			userId = jdbcTemplate.queryForObject(sql, new Object[] { email }, Integer.class);
		} catch (Exception e) {
			userId = 0;
		}
		return userId;
	}

	@Override
	@Async
	public void sendPasswordLink(String email) {
		Integer adminId = getUserIdUsingEmail(email);
		String uri = HashUtility.createUniqueUriPath(adminId, email);
		boolean isStored = uriUtil.createURI(adminId, uri, UserType.USER);
		if (isStored) {
			String link = null; // TODO complete this link
			mailUtil.sendEmailAsync(email, "Update Password",
					"Hi,\nPlease update your password using the below link\n" + link);
		}
	}

}