package com.accolite.miniau.accesscontrol.utility;

public class Query {

	private Query() {

	}

	// AdminDAO
	public static final String CREATEADMIN = "INSERT INTO ADMIN(ADMIN_ID,ADMIN_NAME,PASSWORD, DESCRIPTION) VALUES (?,?,?,?)";
	public static final String DELETEADMIN = "DELETE FROM ADMIN WHERE ADMIN_ID=?";
	public static final String CHANGEPASSWORD = "UPDATE ADMIN SET PASSWORD=? WHERE ADMIN_ID=?";

	// GroupDAO
	public static final String ADDNEWGROUP = "INSERT INTO GROUP(GROUP_ID, GROUP_NAME) VALUES(?,?)";
	public static final String GETALLGROUPNAMES = "SELECT GROUP_NAME FROM GROUP";
	public static final String GETALLGROUPS = "SELECT * FROM GROUP";
	public static final String GETUSERSINGROUP = "SELECT * FROM USER JOIN USER_GROUP ON USER.USER_ID=USER_GROUP.USER_ID WHERE USER_GROUP.GROUP_ID=?";
	public static final String ADDUSERTOGROUP = "INSERT INTO USER_GROUP(GROUP_ID, USER_ID) VALUES(?,?)";
	public static final String REMOVEUSERFROMGROUP = "DELETE FROM USER_GROUP WHERE USER_ID=? AND GROUP_ID=?";
	public static final String ADDPERMISSION = "INSERT INTO GROUP_PERMISSION(GROUP_ID, PERMISSION_ID) VALUES(?,?)";
	public static final String REMOVEPERMISSION = "DELETE FROM GROUP_PERMISSION WHERE GROUP_ID=? AND PERMISSION_ID=?";

	// PermissionDAO
	public static final String CREATEPERMISSION = "INSERT INTO PERMISSION(PERMISSION_NAME,PERMISSION_DESCRIPTION) VALUES (?,?)";
	public static final String DELETEPERMISSION = "DELETE FROM PERMISSION WHERE PERMISSION_ID = ?";
	public static final String GETALLPERMISSIONLIST = "SELECT * FROM PERMISSION";

	// UserDAO
	public static final String ADDNEWUSER = "INSERT INTO USER(USER_ID, USER_NAME, PASSWORD) VALUES (?,?,?)";
	public static final String GETUSER = "SELECT * FROM USER WHERE USER_ID=?";
	public static final String GETALLUSERS = "SELECT * FROM USER";
	public static final String GETALLUSERNAMES = "SELECT USER_NAME FROM USER";
	public static final String ADDPERMISSIONTOUSER = "INSERT INTO GROUP_PERMISSION(USER_ID, PERMISSION_ID) VALUES(?,?)";
	public static final String REMOVEPERMISSIONFROMUSER = "DELETE FROM USER_PERMISSION WHERE USER_ID=? AND PERMISSION_ID=?";

}
