Pluralsight Course API written during "Building an Application Using Java SE 17 by Sander Mak"
This readme will be updated with progress and new learnings.

So far:

Project has been set up using maven

the pom.xml was updated to the below:

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding> # to stop encoding error when running mvn clean verify
        <maven.compiler.release>17</maven.compiler.release> # replaces the 2 existing lines
    </properties>

    <build>
        <plugins>
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId> # ensures that maven uses the correct java version
                <version>3.11.0</version>
            </plugin>
        </plugins>
    </build>

CourseRetrieve class:

Added the below to the pom.xml for slf4j

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>${slf4j.version}</version>
            <scope>runtime</scope>

Then created the main class, this checks for a cl arg and returns an error if this is empty.
If not then calls retrieveCourses(args[0])

retrieveCourses method:

Creates new object of type CourseRetrievalService
Creates new list variable of PluralSightCourse types (which is a record) and calls courseRetrievalService.getCoursesFor
streams the results and filters any that a retired (using lambda function) and sets the result back to a list

updated pom.xml

        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
            <version>${jackson.version}</version>
        </dependency>

This was for the PluralSightCourse.java (and to use jackson for working with json) where the record was created so we could use the jackson annotation @JsonIgnoreProperties(ignoreUnknown = true)
Record was created so that it can be used to retrieve the values from the api json
durationInMinutes() converts the duration into minutes

CourseRetrievalService

Created the PS.URI string for the api call (%s is a string placeholder)
Created a manual http client HttpClient CLIENT
Created an OBJECT_MAPPER (this is for the json)
getCoursesFor sends a get request to the URI (passing in the authorID) and returns a list of the courses using the toPluralSightCourses method which converts the json response.

Added a parametrized test for the durationInMinutes() method in the test folder.

Had to add pom.xml to include the below

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.8.2</version>
            <scope>test</scope>
        </dependency>

Split existing code into 3 modules, course-info, course-info-cli & course-info-repository, moved the existing src folder into the course-info-cli
folder, updated the child pom.xml file to move the dependencies from the 'parent' pom.xml file.
Had to open up modules settings on course-info and remove source, test and resource folders from coure-info-cli before 
running mvn clean verify in the terminal successfully.

Course

Created the course record under course-info-repository, this is to structure the coursed for db insertion.

The filled method ensures the String values are not blank or null, the constructor calls this method on the String values.

CourseTest

Created 3 test methods using junit assertThrows (had to move junit dependencies to course-info-repository pom.xml), these were created by myself as were provided as a challenge during the course.

CourseRepository

Created CourseRepository interface which has void  saveCourse(Course course) & List<Course> getAllCourses(); methods.

CourseJdbcRepository

This implements the CourseRepository interface and is responsible for connecting to the h2 db.

The connection string, INSERT_COURSE & DataSource fields are defined.

saveCourse & getAllCourses are overridden to save a course into the db and list the courses currently saved.

CourseStorageService

This has been created to handle to saving of the courses to the DB by looping through each course in the course list.

CourseStorageService.test

Due to CourseStorageService being package private our own implementation of this has been created in order to test this service
