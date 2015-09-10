# Xvfb :99
export DISPLAY=:99
export CLASSPATH=/var/lib/jenkins/workspace/SampleApplication/oasp4j/Selenium/bin:/var/lib/jenkins/workspace/SampleApplication/oasp4j/Selenium/lib/*
ant -f "oasp4j/build_testng.xml"
chmod +x "oasp4j/Selenium/bin/com/example/tests/loginLogoutChiefTestNG.class"
java -cp "oasp4j/Selenium/bin:oasp4j/Selenium/lib/*" org.testng.TestNG "oasp4j/testng.xml"