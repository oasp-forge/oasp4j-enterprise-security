Xvfb :99
export DISPLAY=:99
java -cp "/var/lib/jenkins/workspace/SampleApplication/oasp4j/Selenium/bin:/var/lib/jenkins/workspace/SampleApplication/oasp4j/Selenium/lib/*:/var/lib/jenkins/workspace/SampleApplication/oasp4j/Selenium/testng_lib/*" org.testng.TestNG "oasp4j/testng.xml"