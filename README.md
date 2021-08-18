# CSAssignment

Run Using command line:  <br />

1. Copy server.log file in local Dir. Eg. c:/server.log    <br />  I have added test server log file at below location  <br /> 
   https://github.com/keshavchainani/CSAssignment/blob/main/server.log  copy it at local path Eg. c:/server.log
2. To run project using command line follow below instructions.  <br />
3. Navigate to the root of the project(inside MavenEclipseProject) via command line and execute the below command.   <br />
4. mvn package  <br />
5. once build is complete run the below command.   <br />
6. java -jar target/LogAssess.jar c:/server.log  <br />


Run in eclipse:
1. Import as Maven project <br />
2. Give program argument as c://server.log before running. <br />
3. Run as Java project from CsAssignmentMain class <br />
4. To run the junit test cases: Right click on project > Run as JUnit Test  <br />
Make sure server.log file exist on the specified location
