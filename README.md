# springboot-websocket
spring boot websocket

主要是 springboot 整合websocket 的maven项目，页面只是使用的html，没有使用 thymeleaf

思考：如果springboot做rest + nginx代理前端页面 该怎样配置nginx？
	1.nginx在1.3.X版本后已经开始支持websocket代理，关键配置
		location /wsapp/ {
			proxy_pass http://wsbackend;
			proxy_http_version 1.1;
			proxy_set_header Upgrade $http_upgrade;
			proxy_set_header Connection "upgrade";
		}
	
	
	 
	WebSocket代理  连接：http://nginx.org/en/docs/http/websocket.html
	要将客户机和服务器之间的连接从HTTP / 1.1转换为WebSocket，使用HTTP / 1.1中可用的协议切换机制。

然而，有一个微妙的地方：由于“升级”是一个 逐跳 标题，它不会从客户端传递到代理的服务器。使用转发代理，客户端可以使用该CONNECT 方法来规避此问题。然而，这不适用于反向代理，因为客户端不知道任何代理服务器，并且需要在代理服务器上进行特殊处理。

1.3.13版本以来，nginx实现了特殊的操作模式，如果代理的服务器使用代码101（交换协议）返回响应，则客户机和代理服务器之间建立隧道，客户端通过请求中的“升级”头。

如上所述，包括“升级”和“连接”的逐跳报头不会从客户端传递到代理服务器，因此为了使代理的服务器知道客户端将协议切换到WebSocket的意图，这些头文件必须明确地通过：

location /chat/ {
    proxy_pass http://backend;
    proxy_http_version 1.1;
    proxy_set_header Upgrade $http_upgrade;
    proxy_set_header Connection "upgrade";
}
一个更复杂的例子，其中对代理服务器的请求中的“连接”头字段的值取决于客户端请求头中的“升级”字段的存在：

http {
    map $http_upgrade $connection_upgrade {
        default upgrade;
        ''      close;
    }

    server {
        ...

        location /chat/ {
            proxy_pass http://backend;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection $connection_upgrade;
        }
    }
默认情况下，如果代理的服务器在60秒内没有传输任何数据，则连接将被关闭。这个超时可以通过proxy_read_timeout指令来增加 。或者，代理服务器可以配置为定期发送WebSocket ping帧以重置超时并检查连接是否仍然存在。


NGINX作为WebSocket代理 node.js  http://blog.csdn.net/xiaoyu19910321/article/details/78244148
	