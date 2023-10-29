package com.pluralsight.courseinfo.cli;
import com.pluralsight.courseinfo.cli.service.CourseRetrievalService;
import com.pluralsight.courseinfo.cli.service.PluralSightCourse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class CourseRetriever {
    private static final Logger LOG = LoggerFactory.getLogger(CourseRetriever.class);

    public static void main(String[] args){
        LOG.info("CourseRetriever Starting");
        if (args.length == 0) {
            LOG.warn("Please provide an author name as the first argument");
            return;
        }
        try {
            retrieveCourses(args[0]);
        } catch (Exception e) {
            LOG.error("Unexpected Error", e);
        }
    }

    private static void retrieveCourses(String authorId) {
        LOG.info("Retrieving courses for author '{}'", authorId);
        CourseRetrievalService courseRetrievalService = new CourseRetrievalService();

        List<PluralSightCourse> coursesToStore = courseRetrievalService.getCoursesFor(authorId)
                .stream()
        .filter(course -> !course.isRetired())
                .toList()
                ; // lambda in filter can also be done like this (not(PluralsightCourse::isRetired)
        LOG.info("Retrieved the following {} courses {}", coursesToStore.size(), coursesToStore);
    }
}
