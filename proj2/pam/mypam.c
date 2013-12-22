#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sqlite3.h>
#include <security/pam_appl.h>
#include <security/pam_modules.h>
#include <crypt.h>
#include <time.h>
#include <pwd.h>
#include <fcntl.h>
#include <GeoIP.h>
#include <GeoIPCity.h>

#ifndef BASE_DIR
#define BASE_DIR "/home/security/"
#endif

#define GROW(x)                if (x > buflen - dest - 1) { \
        char *grow; \
        buflen += 256 + x; \
        grow = realloc(buf, buflen + 256 + x); \
        if (grow == NULL) { free(buf); return NULL; } \
        buf = grow; \
}

#define APPEND(str, len)        GROW(len); memcpy(buf + dest, str, len); dest += len
#define APPENDS(str)        len = strlen(str); APPEND(str, len)

/** Allusion to internal functions. */

static char *IPCountry ( char * );
static char *IPCity ( char * );

/** GeoIP database. */

static const char *geodat = "/usr/local/share/GeoIP/GeoLiteCity.dat";

/**Linux users database. */

static const char *userdat = "/etc/www/db/users.sqlite";

/**
 * Query structure:
 * template: sql query template to be followed.
 * user: client temporary account
 * count: optional field, used to count login retrys. If not used to be ignored.
 * */
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

/** Open sql database. */

static sqlite3 *pam_sqlite3_open()
{
	sqlite3 *db = NULL;
    if(sqlite3_open(userdat, &db) != SQLITE_OK) {
        printf("%s \n",sqlite3_errmsg(db));
        sqlite3_close(db);
        return NULL;
    }
	return db;
}

static sqlite3_stmt *sqlite3_get_user(sqlite3 *db, char *username)
{
	char *query;
	sqlite3_stmt *res;
	query = query_builder("select * from passwd where username='%U'",username,0);    
    if (sqlite3_prepare_v2(db,query,1000, &res, NULL) != SQLITE_OK) 
    {
		printf("query failed1 %p", res);         
    }
    free(query);  
    return res;
}

static void sqlite3_retry_update(sqlite3 *db,char *username, int count)
{
	char *query;
	char *error;
	query = query_builder("update passwd set retrycount=%Or where username='%U'",username,count); 
	printf("%s \n",query); 
    int result = sqlite3_exec(db, query, NULL, NULL, &error); 
    
    if (SQLITE_OK != result) 
    {
		printf("query failed2: %s", error);
		sqlite3_free(error); // error strings rom sqlite3_exec must be freed              
    }
    
    free(query); 
}

static void sqlite3_expire_acc(sqlite3 *db,char *username)
{
	char *query;
	char *error;
	query = query_builder("update passwd set expflag=1 where username='%U'",username,0); 
	printf("%s \n",query); 
    int result = sqlite3_exec(db, query, NULL, NULL, &error); 
    if (SQLITE_OK != result) 
    {
		printf("query failed: %s", error);
		sqlite3_free(error); // error strings rom sqlite3_exec must be freed              
    }
    
    free(query); 
}

PAM_EXTERN int pam_sm_setcred( pam_handle_t *pamh, int flags, int argc, const char **argv ) {
        return PAM_SUCCESS;
}

PAM_EXTERN int pam_sm_acct_mgmt(pam_handle_t *pamh, int flags, int argc, const char **argv) {
       
        /*PAM variables*/
        const char* pUsername;
        char *crypt_password, *password;
        int result;
        char *query;
        
        /*SQlite variables*/
        sqlite3 *db;
        sqlite3_stmt    *res, *des;
        
        result = pam_get_user(pamh, &pUsername, "Username: ");
        
        db = pam_sqlite3_open();
        query = query_builder("select * from passwd where username='%U'",pUsername,0);   
		if (sqlite3_prepare_v2(db,query,1000, &res, NULL) != SQLITE_OK) 
		{	
			printf("query failed1 %p", res);         
		}
		free(query);  
        if(sqlite3_step(res) == SQLITE_ROW)
		{
			const char *dir = sqlite3_column_text(res,5);
			const long int date = sqlite3_column_int(res, 7);
			const int expired = sqlite3_column_int(res, 8);
			long int now = time(NULL);
			if(expired)
			{
				printf("expired \n");
				result = PAM_IGNORE;
			}
			else if(now > date)
			{	
				printf("date-expired \n");
				result = PAM_IGNORE;
			}
			else
			{
				char *oi;
				if(pam_get_item(pamh,PAM_SERVICE,(const void**)&oi) == PAM_SUCCESS)
				{
					if((strcmp(oi,"sshd") != 0) && (strcmp(oi,"su") != 0) && (strcmp(oi,"sudo") != 0 ))
					{
						sqlite3_expire_acc(db,(char *)pUsername);
					}
				}
				char *host;
				printf("Im here 0\n");
				if((result = pam_get_item(pamh,PAM_RHOST,(const void**)&host)) == PAM_SUCCESS)
				{
					char * query;
					printf("Im here 1\n");
					query = query_builder("select country,city from geopermissions where account='%U'",(char *)pUsername,0);
					printf("Im here 2\n");
					printf("%s\n",query);
					printf("Im here 3\n");
					if (sqlite3_prepare_v2(db,query,1000, &des, NULL) != SQLITE_OK) 
					{
					printf("query failed:  %p", des);         
					}
					printf("Im here 4\n");
					free(query);
					result = PAM_IGNORE;
					if (sqlite3_step(des) != SQLITE_ROW)
					{
						sqlite3_finalize(res); 
						sqlite3_close(db);
						return PAM_SUCCESS;
					}
					else
					{
						char *country;
						char *city;	
						const char *sqlcountry;
						const char *sqlcity;
						do
						{
							printf("Im here 5\n");		
							country = IPCountry(host);
							sqlcountry = sqlite3_column_text(des, 0);
							city = IPCity(host);
							sqlcity = sqlite3_column_text(des,1);
							printf("host = %s\ncountry = %s\nsqlcountry=%s\ncity=%s\nsqlcity=%s\n",host,country,sqlcountry,city,sqlcity);
							if (strcmp(country, sqlcountry) == 0)
							{
								if((strcmp("N/A", sqlcity) == 0) || (strcmp(city, sqlcity) == 0))
								{
									result = PAM_SUCCESS;
									break;
								}
							}
						}
						while(sqlite3_step(des) == SQLITE_ROW);
					}
				}
				printf("Im here 6\n");		
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
        const char* pUsername;
        char *crypt_password, *password;
        int result;
        char *oi;
        /*SQlite variables*/
        sqlite3 *db;
        sqlite3_stmt    *res;
        
         result = pam_get_user(pamh, &pUsername, "Username: ");
		 db = pam_sqlite3_open();
         res = sqlite3_get_user(db,(char*)pUsername);
         //Lets check if the password match
		   if(sqlite3_step(res) == SQLITE_ROW)
		   {   	 
			   	result = pam_get_authtok(pamh, PAM_AUTHTOK,(const char **)&password, NULL);
				const char *stored_pw = (const char *) sqlite3_column_text(res, 3);
				const char *stored_salt = (const char *) sqlite3_column_text(res, 4);
				int retrycount = sqlite3_column_int(res,9);
				if (strcmp(crypt(password,stored_salt),stored_pw) == 0)
				{   
					//If the password match and retrycount not zero. Reset.
					if(retrycount!=0)	
					{
						sqlite3_retry_update(db,(char *)pUsername,0);
					}
					result = PAM_SUCCESS;
				}
				//If the password doesnt match and retrycount 2(missed 3 times now) expire.
				else if(retrycount > 1)
				{
					sqlite3_expire_acc(db,(char *)pUsername);
					result = PAM_IGNORE;
				}
				//password fail. retrycount increment.
				else
				{
					retrycount++;
					sqlite3_retry_update(db,(char *)pUsername,retrycount);
					result = PAM_IGNORE;
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

PAM_EXTERN int
pam_sm_open_session(pam_handle_t *pamh, int flags, int argc, const char **argv)
{
	    
		/*PAM variables*/
        const char* pUsername;
        int result;
        
        /*SQlite variables*/
        sqlite3 *db;
        sqlite3_stmt    *res;

        
         if((result = pam_get_user(pamh, &pUsername, "Username: ")) != PAM_SUCCESS)
			return result;
		 db = pam_sqlite3_open();
         res = sqlite3_get_user(db,(char*)pUsername);	
         /*User variables*/
         if(sqlite3_step(res) != SQLITE_ROW)
		 {
			sqlite3_finalize(res); 
			sqlite3_close(db);
			return PAM_AUTH_ERR; 
		 }
		//Get user home directory path
		 const char *dir = (const char *)sqlite3_column_text(res,5);
         //Home exists?
         if(dir == NULL)
         {
			sqlite3_finalize(res); 
			sqlite3_close(db);
			return PAM_AUTH_ERR;
		 }
		 //move to home
		 if(chdir(dir) == -1)
		 {
			 sqlite3_finalize(res); 
			 sqlite3_close(db);
			 return PAM_AUTH_ERR;
		 }
		 //set user default permissions. Only user has permissions!
		 umask(077);
		 //change home permissions.
		 chmod(dir,0700);
		 sqlite3_finalize(res); 
		 sqlite3_close(db); 
		 return PAM_SUCCESS;
         
}		

PAM_EXTERN int
pam_sm_close_session(pam_handle_t *pamh, int flags, int argc, const char **argv)
{
		return PAM_SUCCESS;
}

/** String process. */

static const char * _mk_NA( const char * p ){
    return p ? p : "N/A";
}


/**
 * IP address country code.
 * \param host address.
 * \return host country code.
 * */

static char *IPCountry ( char *host ) {
	
    GeoIP *gi;
    GeoIPRecord *gir;
    char *country;
    uint32_t length;

    gi = GeoIP_open(geodat, GEOIP_INDEX_CACHE);
    
    gir = GeoIP_record_by_name(gi, host);

    if (gir == NULL) {
        return NULL;
    }
    
    length = sizeof(char) * strlen(_mk_NA(gir->city)) + 1;
    
    country = (char *) malloc (length);
    
    country = strncpy(country, _mk_NA(gir->city), length);
    
    GeoIPRecord_delete(gir);
    
    GeoIP_delete(gi);
    
    return country;
}

/**
 * IP address city name.
 * \param host address.
 * \return host city name.
 * */

static char *IPCity ( char *host ) {
	
    GeoIP *gi;
    GeoIPRecord *gir;
    char *city;
    uint32_t length;

    gi = GeoIP_open(geodat, GEOIP_INDEX_CACHE);
    
    gir = GeoIP_record_by_name(gi, host);

    if (gir == NULL) {
        return NULL;
    }
    
    length = sizeof(char) * strlen(_mk_NA(gir->city)) + 1;
    
    city = (char *) malloc (length);
    
    city = strncpy(city, _mk_NA(gir->city), length);
    
    GeoIPRecord_delete(gir);
    
    GeoIP_delete(gi);
    
    return city;
}
