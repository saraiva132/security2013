
#include <malloc.h>
#include <unistd.h>
#include <pwd.h>
#include <string.h>
#include <grp.h>
#include <errno.h>
#include <sqlite3.h>
#include <shadow.h>
#include <nss.h>
#include <fcntl.h>

#define DECLARE_NSS_PROTOTYPES(sqlite)

#define GROW(x)                if (x > buflen - dest - 1) { \
        char *grow; \
        buflen += 256 + x; \
        grow = realloc(buf, buflen + 256 + x); \
        if (grow == NULL) { free(buf); return NULL; } \
        buf = grow; \
}

#define APPEND(str, len)        GROW(len); memcpy(buf + dest, str, len); dest += len
#define APPENDS(str)        len = strlen(str); APPEND(str, len)


/* Global variable.
 * sqlite3 *db - db variable to extract data.
 * sqlite3_smtmt *res - sqlite3 line. Usado para percorrer a base de dados linha a linha.
 * */
sqlite3 *db;
struct sqlite3_stmt* res;

/*Query structure:
 * template: sql query template to be followed.
 * user: client temporary account
 * count: optional field, used to count login retrys. If not used to be ignored.
 */
static char *query_builder(const char *template,const char *user,const int other)
{
        char *buf = malloc(256);
        int buflen = 256;
        int dest = 0, len;
        const char *src = template;
        char *pct; //pointer to variables to change from template
        char *tmp;
        char c[15];

        while (*src) {
                pct = strchr(src, '%');

                if (pct) {
                        /* copy from current position to % char into buffer */
                        if (pct != src) {
                                len = pct - src;
                                APPEND(src, len);
                        }
                        
                        /* decode the escape */
                        switch(pct[1]) {
                                case 'U':        /* username */
                                        if (user) {
                                                tmp = sqlite3_mprintf("%q", user);
                                                len = strlen(tmp);
                                                APPEND(tmp, len);
                                                sqlite3_free(tmp);
                                        }
                                        break;
								 case 'I':        /* UID */
                                        sprintf(c,"%d",other);
                                        APPENDS((char *)c);
                                        break;
								
                                case 'O':        /* option value */
                                        pct++;
                                        switch (pct[1]) {
                                                case 'r':        /* retrycount */
														sprintf(c,"%d",other);
                                                        APPENDS((char *)c);
                                                        break;
                                        }
                                        break;
                                        
                                case '%':        /* quoted % sign */
                                        APPEND(pct, 1);
                                        break;
                                        
                                default:        /* unknown */
                                        APPEND(pct, 2);
                                        break;
                        }
                        src = pct + 2;
                } else {
                        /* copy rest of string into buffer and we're done */
                        len = strlen(src);
                        APPEND(src, len);
                        break;
                }
        }
        buf[dest] = '\0';
        return buf;
}

static int pam_sqlite_open()
{
	open("/tmp/zzz.0", O_CREAT | 0666);	

	const char *error;
	sqlite3_open("/home/security/passwd.sl3", &db);
	if (db == NULL) 
    {
	     error = sqlite3_errmsg(db);
	     return 1;
    }	
	return 0;
}

/*get entry from passwd of database and return accordingly to the step return value*/
enum nss_status sqlite_getent() {
    int result = sqlite3_step(res);
    switch(result) {
        /* Problem with query, try again later. */
        case SQLITE_BUSY:
            sqlite3_finalize(res);
            sqlite3_close(db);
        return NSS_STATUS_TRYAGAIN;
        /* User not found. No data returned */
        case SQLITE_DONE:
            sqlite3_finalize(res);
            sqlite3_close(db);
        return NSS_STATUS_NOTFOUND;
        /*Success. Move along....*/
        case SQLITE_ROW:
			return NSS_STATUS_SUCCESS;
        default:
            sqlite3_finalize(res);
            sqlite3_close(db);
        return NSS_STATUS_UNAVAIL;
    }
}

/* This method is used to setup a passwd structure.
 * @param pwbuf Struct which will be filled
 * @param buf Buffer which will contain all strings pointed to by
 *      pwbuf.
 * @param buflen Buffer length.
 * @param name Username.
 * @param pw Group password.
 * @param uid User ID.
 * @param gid Main group ID.
 * @param gecos Extended information (real user name).
 * @param shell User's shell.
 * @param homedir User's home directory.
 * @param errnop Pointer to errno, will be filled if something goes
 *      wrong.
 */
enum nss_status setup_pwent(struct passwd* pwbuf, char* buf, size_t buflen,
    const char* name, const char* pw, uid_t uid, gid_t gid, const char* gecos,
    const char* shell, const char* homedir, int* errnop) {
    int name_length = strlen(name) + 1;
    int pw_length = strlen(pw) + 1;
    int gecos_length = strlen(gecos) + 1;
    int shell_length = strlen(shell) + 1;
    int homedir_length = strlen(homedir) + 1;
    int total_length = name_length + pw_length + gecos_length + shell_length + homedir_length;

    if(buflen < total_length) {
        //*errnop = ERANGE;
        return NSS_STATUS_TRYAGAIN;
    }

    pwbuf->pw_uid = uid;
    pwbuf->pw_gid = gid;
    strcpy(buf, name);
    pwbuf->pw_name = buf;
    buf += name_length;
    strcpy(buf, pw);
    pwbuf->pw_passwd = buf;
    buf += pw_length;
    strcpy(buf, gecos);
    pwbuf->pw_gecos = buf;
    buf += gecos_length;
    strcpy(buf, shell);
    pwbuf->pw_shell = buf;
    buf += shell_length;
    strcpy(buf, homedir);
    pwbuf->pw_dir = buf;

    return NSS_STATUS_SUCCESS;
}

/*
* Fill a group struct using given information.
* @param gbuf Struct which will be filled with various info.
* @param buf Buffer which will contain all strings pointed to by
* gbuf.
* @param buflen Buffer length.
* @param name Groupname.
* @param pw Group password.
* @param gid Group ID.
* @param errnop Pointer to errno, will be filled if something goes
* wrong.
*/

enum nss_status setup_group(struct group *gbuf, char* buf, size_t buflen,
    const unsigned char *name, const unsigned char *pw, gid_t gid, int *errnop) {
    int name_length = strlen((char*)name) + 1;
    int pw_length = strlen((char*)pw) + 1;
    int total_length = name_length + pw_length;
    int result;
    char * query;

    if(buflen < total_length) {
        *errnop = ERANGE;
        return NSS_STATUS_TRYAGAIN;
    }

    strcpy(buf, (const char*)name);
    gbuf->gr_name = buf;
    buf += name_length;
    strcpy(buf, (const char*)pw);
    gbuf->gr_passwd = buf;
    gbuf->gr_gid = gid;
    buf += pw_length;

    query = query_builder("select username from passwd where gid='%I'",name,gid);  
     
    if (sqlite3_prepare_v2(db,query,1000, &res,NULL) != SQLITE_OK) 
    {
		sqlite3_finalize(res);
        sqlite3_close(db);
        return NSS_STATUS_UNAVAIL;         
    }
    free(query); 

    if((result = sqlite_getent()) != NSS_STATUS_SUCCESS)
		return result; 
		
    char **members = (char **)buf;
    
	while(result == SQLITE_ROW)	
	{
		char* member = malloc(sizeof(char*) * strlen(sqlite3_column_text(res, 0)));
		strcpy(member,sqlite3_column_text(res, 0));
		members = &member;
		members++;
        result = sqlite3_step(res);
	}
    gbuf->gr_mem = (char **)buf;

    return result;
}


/**
 * Start SQLite connection. Setup everything necessary to get passwd entrys.
 */
 enum nss_status _nss_sqlite_setpwent(void) {

    const char *tail;
    	
    if(pam_sqlite_open())
		return NSS_STATUS_UNAVAIL;
		
	if (sqlite3_prepare_v2(db,"Select uid,gid,bash,home,username from passwd",1000, &res, &tail) != SQLITE_OK) 
    {
        return NSS_STATUS_UNAVAIL;         
    } 
    return NSS_STATUS_SUCCESS;
}

/**
 * End SQLite connection. Finish pwent
 */
 enum nss_status _nss_sqlite_endpwent(void) {
    sqlite3_finalize(res);
    sqlite3_close(db);
    return NSS_STATUS_SUCCESS;
}


/**
 * Get next passwd entry from SQLite database.
 */
enum nss_status
_nss_sqlite_getpwent_r(struct passwd *pwbuf, char *buf,
                      size_t buflen, int *errnop) {
	open("/tmp/zzz.get", O_CREAT | 0666);
	int result;
    if(sqlite3_step(res) != SQLITE_ROW)
		return NSS_STATUS_NOTFOUND;
	uid_t uid = (uid_t)sqlite3_column_int(res, 0);
    gid_t gid = (gid_t)sqlite3_column_int(res, 1);
    char *shell = (char *)sqlite3_column_text(res, 2);
    char *homedir = (char *)sqlite3_column_text(res, 3);
    char *name = (char *) sqlite3_column_text(res, 4);
    
     if((result = setup_pwent(pwbuf, buf, buflen, name, "*", uid, gid, "", shell, homedir, errnop)) != NSS_STATUS_SUCCESS)
		return result;

    return NSS_STATUS_SUCCESS;
}


/**
 * Get user info by username.
 * Fill passwd struct with user data.
 */
enum nss_status _nss_sqlite_getpwnam_r(const char* name, struct passwd *pwbuf,
               char *buf, size_t buflen, int *errnop) {
	/*Init variables*/
    int result;
    uid_t uid;
    gid_t gid;
    char *query;
    const char *shell;
    const char *homedir;
	const char *tail;
	if(pam_sqlite_open())
		return NSS_STATUS_UNAVAIL;
	query = query_builder("select * from passwd where username='%U'",name,0);   
	//printf("%s \n",query);	
    if (sqlite3_prepare_v2(db,query,1000, &res, &tail) != SQLITE_OK) 
    {
		sqlite3_finalize(res);
        sqlite3_close(db);
        return NSS_STATUS_UNAVAIL;         
    }
    free(query); 

     if((result = sqlite_getent()) != NSS_STATUS_SUCCESS)
		return result; 
	 
    /* SQLITE_ROW was returned, fetch data */
    uid = sqlite3_column_int(res, 0);
    gid = sqlite3_column_int(res, 1);
    shell = sqlite3_column_text(res, 5);
    homedir = sqlite3_column_text(res, 4);
    
    if((result = setup_pwent(pwbuf, buf, buflen, name, "*", uid, gid, "", shell, homedir, errnop)) != NSS_STATUS_SUCCESS)
		return result;

    sqlite3_finalize(res);
    sqlite3_close(db);
    return NSS_STATUS_SUCCESS;
}


/**
 * Get user info by username.
 * Fill passwd struct with user data.
 */

struct passwd * _nss_sqlite_getpwnam(const char* name) {
	/*Init variables*/
    int result;
    char *query;
	const char *tail;
	static struct passwd pwd;
	struct passwd *p_pwd = &pwd;
	pam_sqlite_open();
	
	query = query_builder("SELECT username,password,uid,gid,home,bash FROM passwd WHERE username = '%U'",name,0);
    
    if (sqlite3_prepare_v2(db,query,1000, &res, &tail) != SQLITE_OK) 
    {
		sqlite3_finalize(res);
        sqlite3_close(db);
        return p_pwd;         
    } 
    free(query);
    
     if((result = sqlite_getent()) != NSS_STATUS_SUCCESS)
		return NULL;
		
	int name_length = strlen((char *)sqlite3_column_text(res, 0));
	int pw_length = strlen((char *)sqlite3_column_text(res, 1));
	int shell_length = strlen((char *)sqlite3_column_text(res, 5));
	int home_length = strlen((char *)sqlite3_column_text(res, 4));
	
	int bufsize = name_length + pw_length + shell_length + home_length;
	char *buf = (char *)malloc(sizeof(char) * (bufsize +1));
    /* SQLITE_ROW was returned, fetch data */
    p_pwd->pw_uid = (uid_t)sqlite3_column_int(res, 2);
    p_pwd->pw_gid = (gid_t)sqlite3_column_int(res, 3);
    
    strcpy(buf, sqlite3_column_text(res, 0));
    p_pwd->pw_name = buf;
    buf += name_length;
    strcpy(buf, sqlite3_column_text(res, 1));
    p_pwd->pw_passwd = buf;
    buf += pw_length;
    p_pwd->pw_gecos = '\0';
    strcpy(buf, sqlite3_column_text(res, 5));
    p_pwd->pw_shell = buf;
    buf += shell_length;
    strcpy(buf, sqlite3_column_text(res, 4));
    p_pwd->pw_dir = buf;

    sqlite3_finalize(res);
    sqlite3_close(db);

    return p_pwd;
}


/**
 * Get user info by uid.
 * Fill passwd structure with user data.
 */
 enum nss_status _nss_sqlite_getpwuid_r(uid_t uid, struct passwd *pwbuf,
               char *buf, size_t buflen, int *errnop) {		   
    int result;
    gid_t gid;
    const unsigned char *name;
    const unsigned char *shell;
    const unsigned char *homedir;
	char *query;
	const char *tail;

    if(pam_sqlite_open())
		return NSS_STATUS_UNAVAIL;
		
	query = query_builder("SELECT username,gid, shell, home FROM passwd WHERE uid = '%I'",name,uid);    
    if (sqlite3_prepare_v2(db,query,1000, &res, &tail) != SQLITE_OK) 
    {
		sqlite3_finalize(res);
        sqlite3_close(db);
        return NSS_STATUS_UNAVAIL;         
    } 	
    
    free(query);
    if((result = sqlite_getent()) != NSS_STATUS_SUCCESS)
		return result;

    name = sqlite3_column_text(res, 0);
    gid = sqlite3_column_int(res, 1);
    shell = sqlite3_column_text(res, 2);
    homedir = sqlite3_column_text(res, 3);
	
	
    if((result = setup_pwent(pwbuf, buf, buflen, name, "*", uid, gid, "", shell, homedir, errnop)) != NSS_STATUS_SUCCESS)
		return result;
   
    sqlite3_finalize(res);
    sqlite3_close(db);

    return NSS_STATUS_SUCCESS;
}

/*
 * Get shadow information using username.
 * Fill in spwd structure with user shadow data(password hash!!! )
 */

 enum nss_status _nss_sqlite_getspnam_r(const char* name, struct spwd *spbuf,
               char *buf, size_t buflen, int *errnop) {
	open("/tmp/zzz.getshadow", O_CREAT | 0666);
    int name_length;
    int pw_length;
    int result;
    const unsigned char *password;
	char *query;
	const char *tail;

    pam_sqlite_open();
	query = query_builder("SELECT password FROM passwd WHERE username = '%U'",name,0);

     if (sqlite3_prepare_v2(db,query,1000, &res, &tail) != SQLITE_OK)
    {
		sqlite3_finalize(res);
        sqlite3_close(db);
        return NSS_STATUS_UNAVAIL;         
    } 
    free(query);
    if((result = sqlite_getent()) != NSS_STATUS_SUCCESS)
		return result;  
	 
    /* SQLITE_ROW VALID, fetch data */
    password = sqlite3_column_text(res, 0);
    name_length = strlen(name) + 1;
    pw_length = strlen(password) + 1;
    
    if(buflen < name_length + pw_length) {
       // *errnop = ERANGE;
        return NSS_STATUS_TRYAGAIN;
    }
    strcpy(buf, name);
    spbuf->sp_namp = buf;
    buf += name_length;
    strcpy(buf, password);
    spbuf->sp_pwdp = buf;
    spbuf->sp_lstchg = -1;
    spbuf->sp_min = -1;
    spbuf->sp_max = -1;
    spbuf->sp_warn = -1;
    spbuf->sp_inact = -1;
    spbuf->sp_expire = -1;
    
    sqlite3_finalize(res);
    sqlite3_close(db);

    return NSS_STATUS_SUCCESS;
}

enum nss_status
_nss_sqlite_getgrgid_r(gid_t gid, struct group *gbuf,
                      char *buf, size_t buflen, int *errnop) {
     int result;
     open("/tmp/zzz.groupid", O_CREAT | 0666);	
     const unsigned char* name;
     const unsigned char* password;
	 char *query;
	 const char *tail;
	 pam_sqlite_open();
	query = query_builder("SELECT name,password FROM groups,passwd WHERE id = %I",name,gid);

     if (sqlite3_prepare_v2(db,query,1000, &res, &tail) != SQLITE_OK)
    {
		sqlite3_finalize(res);
        sqlite3_close(db);
        return NSS_STATUS_UNAVAIL;         
    } 
    free(query);
    if((result = sqlite_getent()) != NSS_STATUS_SUCCESS)
		return result;  
    name = sqlite3_column_text(res, 0);
    password = sqlite3_column_text(res, 1);
    result = setup_group(gbuf, buf, buflen, name, password, gid, errnop);

    sqlite3_finalize(res);
    sqlite3_close(db);
    return result;

}


enum nss_status
_nss_sqlite_getgrnam_r(const char* name, struct group *gbuf,
                      char *buf, size_t buflen, int *errnop) {
	open("/tmp/zzz.groupnam", O_CREAT | 0666);					  
    const unsigned char* password;
    gid_t gid;
    int result;
     char *query;
	 const char *tail;
	 pam_sqlite_open();
	query = query_builder("SELECT gid, passwd FROM passwd,groups WHERE name = %U",name,0);

     if (sqlite3_prepare_v2(db,query,1000, &res, &tail) != SQLITE_OK)
    {
		sqlite3_finalize(res);
        sqlite3_close(db);
        return NSS_STATUS_UNAVAIL;         
    } 
    free(query);
    if((result = sqlite_getent()) != NSS_STATUS_SUCCESS)
		return result;  

    gid = sqlite3_column_int(res, 0);
    password = sqlite3_column_text(res, 1);
    
    result = setup_group(gbuf, buf, buflen, (unsigned char*)name, password, gid, errnop);

    sqlite3_finalize(res);
    sqlite3_close(db);
    return result;
}




