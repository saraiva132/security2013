gcc -fPIC -DPIC -shared -rdynamic -o /lib/security/mypam.so mypam.c -lsqlite3 -lpam -lcrypt
gcc -o pam_test test.c -lpam -lpam_misc
gcc -fPIC -shared -o /lib/i386-linux-gnu/libnss_sqlite.so.2 libnss_sqlite.c -lsqlite3


       
