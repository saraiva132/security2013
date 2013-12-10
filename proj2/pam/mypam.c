#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sqlite3.h>
#include <security/pam_appl.h>
#include <security/pam_modules.h>
#include <crypt.h>
#include <time.h>
#include <pwd.h>

#define GROW(x)                if (x > buflen - dest - 1) { \
        char *grow; \
        buflen += 256 + x; \
        grow = realloc(buf, buflen + 256 + x); \
        if (grow == NULL) { free(buf); return NULL; } \
        buf = grow; \
}

#define APPEND(str, len)        GROW(len); memcpy(buf + dest, str, len); dest += len
#define APPENDS(str)        len = strlen(str); APPEND(str, len)

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

static sqlite3 *pam_sqlite3_open()
{
	const char * error;
	sqlite3 *db = NULL;
	sqlite3_open("/home/security/passwd.sl3", &db);
	if (db == NULL) 
    {
	     error = sqlite3_errmsg(db);
    }
	return db;
}

static sqlite3_stmt *sqlite3_get_user(sqlite3 *db, char *username)
{
	char *query;
	sqlite3_stmt *res;
	const char *tail;
	query = query_builder("select * from passwd where username='%U'",username,0);    
    if (sqlite3_prepare_v2(db,query,1000, &res, &tail) != SQLITE_OK) 
    {
		//printf("query failed %p", res);         
    }
    free(query);  
    return res;
}

static void sqlite3_retry_update(sqlite3 *db,char *username, int count)
{
	char *query;
	char *error;
	sqlite3_stmt *res;
	query = query_builder("update passwd set retrycount=%Or where username='%U'",username,count); 
	printf("%s \n",query); 
    int result = sqlite3_exec(db, query, NULL, NULL, &error);
    free(query);  
    
    if (SQLITE_OK != result) 
    {
		printf("query failed: %s", error);
		sqlite3_free(error); // error strings rom sqlite3_exec must be freed              
    }
}

static void sqlite3_expire_acc(sqlite3 *db,char *username)
{
	char *query;
	char *error;
	sqlite3_stmt *res;
	query = query_builder("update passwd set expflag=1 where username='%U'",username,0); 
	printf("%s \n",query); 
    int result = sqlite3_exec(db, query, NULL, NULL, &error);
    free(query);  
    
    if (SQLITE_OK != result) 
    {
		printf("query failed: %s", error);
		sqlite3_free(error); // error strings rom sqlite3_exec must be freed              
    }
}

PAM_EXTERN int pam_sm_setcred( pam_handle_t *pamh, int flags, int argc, const char **argv ) {
	    printf("Set Credentials here\n");
        return PAM_SUCCESS;
}

PAM_EXTERN int pam_sm_acct_mgmt(pam_handle_t *pamh, int flags, int argc, const char **argv) {
       
        /*PAM variables*/
        int retval;
        const char* pUsername;
        char *crypt_password, *password;
        int result;
        
        /*SQlite variables*/
        sqlite3 *db;
        sqlite3_stmt    *res;
        int     error = 0;
        const char      *errMSG;
        
        result = pam_get_user(pamh, &pUsername, "Username: ");
        printf("Acct mgmt here\n");
        db = pam_sqlite3_open();
        res = sqlite3_get_user(db,(char*)pUsername);
        if(sqlite3_step(res) == SQLITE_ROW)
		{
			const long int date = sqlite3_column_int(res, 6);
			const int expired = sqlite3_column_int(res, 7);
			long int now = time(NULL);
			printf("%lu-%lu-%d",date,now,expired);
			if(expired)
			{
				printf("expired \n");
				result = PAM_AUTH_ERR;
			}
			else if(now > date)
			{	
				printf("date-expired \n");
				result = PAM_AUTH_ERR;
			}
			else
			{
				result = PAM_SUCCESS;
			}
		}
		else
		{
			result = PAM_AUTH_ERR;
		}
		sqlite3_finalize(res); 
		sqlite3_close(db);   
        return result;
}

PAM_EXTERN int pam_sm_authenticate( pam_handle_t *pamh, int flags,int argc, const char **argv ) {
		/*PAM variables*/
        int retval;
        const char* pUsername;
        char *crypt_password, *password;
        int result;
        
        /*SQlite variables*/
        sqlite3 *db;
        sqlite3_stmt    *res;
        const char      *errMSG;
        
         result = pam_get_user(pamh, &pUsername, "Username: ");
		
         printf("Welcome. SQLite here. %s\n", pUsername);
		 db = pam_sqlite3_open();
         res = sqlite3_get_user(db,(char*)pUsername);
         
		   if(sqlite3_step(res) == SQLITE_ROW)
		   {   	 
			   	result = pam_get_authtok(pamh, PAM_AUTHTOK,(const char **)&password, NULL);
				const char *stored_pw = (const char *) sqlite3_column_text(res, 3);
				int retrycount = sqlite3_column_int(res,8);
				if (strcmp(crypt(password,stored_pw),stored_pw) == 0)
				{   
					if(retrycount!=0)	
					{
						sqlite3_retry_update(db,(char *)pUsername,0);
					}
					result = PAM_SUCCESS;
				}
				else if(retrycount > 2)
				{
					sqlite3_expire_acc(db,(char *)pUsername);
					result = PAM_AUTH_ERR;
				}
				else
				{
					retrycount++;
					sqlite3_retry_update(db,(char *)pUsername,retrycount);
					result = PAM_AUTH_ERR;
				}
			}
			else
			{
				result = PAM_AUTH_ERR;
			} 
		sqlite3_finalize(res); 
		sqlite3_close(db); 
		return result;
}

#include <fcntl.h>

PAM_EXTERN int
pam_sm_open_session(pam_handle_t *pamh, int flags, int argc, const char **argv)
{
	
		/*PAM variables*/
        const char* pUsername;
        int result;
        /*SQlite variables*/
        sqlite3 *db;
        sqlite3_stmt    *res;

        
         if((result = pam_get_user(pamh, &pUsername, "Username: ") != PAM_SUCCESS || pUsername == NULL))
			return result;
		 
         printf("Welcome. Session here. %s\n", pUsername);
		 db = pam_sqlite3_open();
         res = sqlite3_get_user(db,(char*)pUsername);
		struct passwd *pwd;
		pwd = getpwnam(pUsername);
		printf("%s \n",pwd->pw_dir);
         /*User variables*/
         if(sqlite3_step(res) != SQLITE_ROW)
		 {
			printf("Error \n");
			sqlite3_finalize(res); 
			sqlite3_close(db);
			return PAM_AUTH_ERR; 
		 }
		 const char *dir = (const char *)sqlite3_column_text(res,4);
         printf("%s \n",dir);  
         		
         if(dir == NULL)
         {
			printf("home not valid \n");
			sqlite3_finalize(res); 
			sqlite3_close(db);
			return PAM_AUTH_ERR;
		 }
			
		 if(chdir(dir) == -1)
		 {
			 printf("cant chdir \n");
			 sqlite3_finalize(res); 
			 sqlite3_close(db);
			 return PAM_AUTH_ERR;
		 }
		 if(chroot(dir)!=0);
		 {
			 printf("cant chroot \n");
			 /*sqlite3_finalize(res); 
			 sqlite3_close(db);
			 return PAM_AUTH_ERR;*/
		 }
		 
		 //pam_setenv(pamh,"HOME",dir,1);
		 sqlite3_finalize(res); 
		 sqlite3_close(db); 
		 return PAM_SUCCESS;
         
}		

PAM_EXTERN int
pam_sm_close_session(pam_handle_t *pamh, int flags, int argc, const char **argv)
{
		return PAM_SUCCESS;
}
