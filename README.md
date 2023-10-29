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

Added a parametised test for the durationInMinutes() method in the test folder.

Had to add pom.xml to include the below

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.8.2</version>
            <scope>test</scope>
        </dependency>
