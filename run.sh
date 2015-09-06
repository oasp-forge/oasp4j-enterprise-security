Xvfb :99
export DISPLAY=:99
CLASSPATH=/var/lib/jenkins/workspace/SampleApplication/oasp4j/Selenium/bin:/var/lib/jenkins/workspace/SampleApplication/oasp4j/Selenium/lib/*
java -cp "oasp4j/Selenium/bin:oasp4j/Selenium/lib/*" org.testng.TestNG "oasp4j/testng.xml"