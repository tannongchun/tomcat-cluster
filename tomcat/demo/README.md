
# Tomcat 集群搭建步骤
- 官方参考文档 https://tomcat.apache.org/tomcat-8.5-doc/manager-howto.html
- 统计 Session :https://tomcat.apache.org/tomcat-8.5-doc/manager-howto.html#Session_Statistics
## 演示环境说明

- Jdk 1.8
- Tomcat 8.5 +
- tomcat8 下载地址 (http://mirrors.hust.edu.cn/apache/tomcat/tomcat-8/v8.5.34/bin/apache-tomcat-8.5.34.zip)

## 配置 tomcat 用户管理 
注意：不同大版本的tomcat配置会有点小差异。
- 1.进入 TOMCAT_HOME /conf/tomcat-users.xml
- 2.不能删除webapps 里面的 manager、host-manager工程
- 添加内容如下(注意password):

`````
 <role rolename="manager-gui"/>
 <role rolename="manager-script"/>
 <role rolename="manager-jmx"/>
 <role rolename="manager-status"/>
 <user username="admin" password="admin" roles="manager-gui,manager-script,manager-jmx,manager-status"/>
 
`````

### 项目部署 
- 将本项目 webapp 拷贝至 tomcat 工程 webapps 重命名为 node1

### tomcat 集群部署

#### 基于 tomcat DeltaManager 管理session ,适用于小规模集群。
-  TOMCAT_HOME/conf/server.xml
-  在 <Engine name="Catalina" defaultHost="localhost" jvmRoute="node2">
- 添加 <Cluster className="org.apache.catalina.ha.tcp.SimpleTcpCluster"/> 调用tomcat 默认配置
具体默认值：参考 https://tomcat.apache.org/tomcat-8.5-doc/cluster-howto.html#For_the_impatient

#### 基于 redis 管理session。
- http://www.cnblogs.com/interdrp/p/4868740.html
- https://blog.csdn.net/fighterandknight/article/details/56843328
> 操作步骤如下：
   1.引入相关的jar 包tomcat-redis-session-manager（tomcat 8以上需要自己编译）、commons-pool2-2.2、jedis-2.5.2 
  2. 在TOMCAT_HOME/conf/context.xml 添加：
  

```		

	 <Valve className="com.orangefunction.tomcat.redissessions.RedisSessionHandlerValve" />  
	    <Manager className="com.orangefunction.tomcat.redissessions.RedisSessionManager"  
	        host="192.168.1.5"  
	        port="6379"  
	        database="0" />

```



### tomcat Session 管理 
> 1) StandardManager
      Tomcat7的默认会话管理器，用于非集群环境中对单个处于运行状态的Tomcat实例会话进行管理。
      当Tomcat关闭时，这些会话相关的数据会被写入磁盘上的一个名叫SESSION.ser的文件，并在Tomcat下次启动时读取此文35  
      
> 2) PersistentManager
        当一个会话长时间处于空闲状态时会被写入到swap会话对象，这对于内存资源比较吃紧的应用环境来说比较有用。

      
> 3) DeltaManager
        用于Tomcat集群的会话管理器，它通过将改变了会话数据同步给集群中的其它节点实现会话复制。
        这种实现会将所有会话的改变同步给集群中的每一个节点，也是在集群环境中用得最多的一种实现方式。
       
> 3) DeltaManager
        用于Tomcat集群的会话管理器，它通过将改变了会话数据同步给集群中的其它节点实现会话复制。
        这种实现会将所有会话的改变同步给集群中的每一个节点，也是在集群环境中用得最多的一种实现方式。       
        
> 4) BackupManager 
         集群的会话管理器， 与DeltaManager不同的是， 某节点会话的改变只会同步给集群中的另一个而非所有节点。

> 5) SimpleTcpReplicationManager 
           Tomcat4时用到的版本，过于老旧了。
         





