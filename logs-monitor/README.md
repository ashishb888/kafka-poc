# Log files monitor

> I have used Project Lombok, you may want to refer [this link](https://projectlombok.org/setup/eclipse) to configure Project Lombok in Eclipse

###### Architectural diagram 

![](https://github.com/ashishb888/kafka-poc/blob/master/logs-monitor/diagrams/logs-monitor.PNG)

###### Technologies stack

<pre>
Language: Java 8
Framework: Spring boot 2.1.6.RELEASE
Build system: Maven 3.2+
Filebeat 7.4.2
Kafka 2.3.0
</pre>

###### Package
` mvn package `

###### Run
` nohup $JAVA_HOME/bin/java -jar logs-monitor-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 & `