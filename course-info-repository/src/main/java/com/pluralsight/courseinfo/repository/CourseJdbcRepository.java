package com.pluralsight.courseinfo.repository;

import com.pluralsight.courseinfo.domain.Course;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.util.json.JSONTarget;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

class CourseJdbcRepository implements CourseRepository {

    private static final String H2_DATABASE_URL =
            loadJdbcConnectionString();

    private static final String INSERT_COURSE = """
    MERGE INTO Courses (id, name, length, url)
    VALUES (?, ?, ?, ?)
    """;

    private static final String ADD_NOTES = """
            UPDATE Courses SET notes = ?
            WHERE id = ?
            """;

    private final String DELETE_COURSE = """
            DELETE FROM Courses
            WHERE id = ?
            """;

    private final String GET_SINGLE_COURSE = """
            SELECT * FROM Courses
            WHERE id = ?
            """;
    private final DataSource dataSource;

    private static String loadJdbcConnectionString() {
        try (InputStream propertiesStream =
                     CourseJdbcRepository.class.getResourceAsStream("/repository.properties")) {
            Properties properties = new Properties();
            properties.load(propertiesStream);
            return properties.getProperty("course-info.jdbcConnection");
        } catch (IOException e) {
            throw new IllegalStateException("Could not load database connection string");
        }

    }

    public CourseJdbcRepository(String databaseFile) {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL(H2_DATABASE_URL.formatted(databaseFile));
        this.dataSource = jdbcDataSource;
    }
    @Override
    public void saveCourse(Course course) {
       try (Connection connection = dataSource.getConnection()) {
           PreparedStatement statement = connection.prepareStatement(INSERT_COURSE);
           statement.setString(1, course.id());
           statement.setString(2, course.name());
           statement.setLong(3, course.length());
           statement.setString(4, course.url());
           statement.execute();
       } catch (SQLException e) {
           throw new RepositoryException("Failed to save " + course, e);
       }
    }

    @Override
    public List<Course> getAllCourses() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM COURSES");

            List<Course> courses = new ArrayList<>();

            while(resultSet.next()) {
                Course course = new Course(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getLong(3),
                        resultSet.getString(4),
                        Optional.ofNullable(resultSet.getString(5)));
                courses.add(course);
            }
            return Collections.unmodifiableList(courses);
        } catch (SQLException e) {
            throw new RepositoryException("Failed to retrieve courses ", e);
        }
    }

    @Override
    public void addNotes(String id, String notes) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(ADD_NOTES);
            statement.setString(1, notes);
            statement.setString(2, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RepositoryException("Failed to add note to " + id, e);
        }
    }

    @Override
    public void deleteCourse(String id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_COURSE);
            statement.setString(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RepositoryException("Failed to delete course " + id, e);
        }
    }
}
