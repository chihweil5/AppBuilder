FROM tomcat:7-jre8
RUN ["apt-get", "update"]
RUN ["rm", "-fr", "/usr/local/tomcat/webapps/ROOT"]
RUN ["rm", "-fr", "/root/.gradle"]
RUN wget https://services.gradle.org/distributions/gradle-3.3-all.zip
RUN unzip gradle-3.3-all.zip
ENV GRADLE_HOME=/usr/local/tomcat/gradle-3.3
ENV PATH=$PATH:$GRADLE_HOME/bin
ENV GRADLE_USER_HOME=/usr/local/tomcat/gradle-3.3

RUN wget https://dl.google.com/android/repository/tools_r25.2.3-linux.zip?hl=sk
RUN unzip tools_r25.2.3-linux.zip?hl=sk
RUN mkdir Android
RUN mv /usr/local/tomcat/tools /usr/local/tomcat/Android/
RUN cd /usr/local/tomcat/Android/tools && ./android update sdk --no-ui | echo "y"
RUN mv /usr/local/tomcat/Android/ $HOME
ENV ANDROID_HOME=/root/Android
ENV PATH=/root/Android/tools:$PATH
RUN mkdir /root/Android/licenses
RUN echo -e "\n8933bad161af4178b1185d1a37fbf41ea5269c55" > "/root/Android/licenses/android-sdk-license"
ADD ./target/ApplicationBuilder-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]