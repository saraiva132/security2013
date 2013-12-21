all:
	gcc -fPIC -shared -o libnss_sqlite.so.2  libnss_sqlite.c -lsqlite3
	gcc -fPIC -DPIC -shared -rdynamic -o mypam.so mypam.c -lsqlite3 -lpam -lcrypt -lGeoIP
install:
	sudo install -m 0644 libnss_sqlite.so.2 /lib/i386-linux-gnu/
	sudo install -m 0644 mypam.so /lib/security/

