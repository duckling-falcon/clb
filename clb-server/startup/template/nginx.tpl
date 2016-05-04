user  root root;
worker_processes  8;
worker_rlimit_nofile 102400;

events {
    use epoll;
    worker_connections  65535;
}


http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;
    
    server {
        listen       80;
        server_name static.escience.cn;
		rewrite_log on; 

        location / {
            root   html;
            index  index.html index.htm;
        }

        location /pdf/ {
            gridfs docs
            root_collection=clb_pdf
            field=storageKey
            type=objectid;
            mongo $CLB_MONGO_IP:$CLB_MONGO_PORT;
        }

        location /doc/ {
            gridfs docs
            root_collection=fs
            field=storageKey
            type=objectid;
            mongo $CLB_MONGO_IP:$CLB_MONGO_PORT;
        }

	location /image/ {
            gridfs docs
            root_collection=clb_image
	    field=storageKey
            type=objectid;
            mongo $CLB_MONGO_IP:$CLB_MONGO_PORT;
        }
	
	location /trivial/ {
	    gridfs docs
            root_collection=clb_trivial
            field=filename
            type=string;
            mongo $CLB_MONGO_IP:$CLB_MONGO_PORT;
        }
	
        location /status {
            stub_status on;
            access_log logs/status.log;
            auth_basic "NginxStatus";
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

    }

}
