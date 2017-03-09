import api.*;
import api.core.impl.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestInstructor {
    private IAdmin admin;
    private IInstructor instructor;

    @Before
    public void setup() {
        this.admin = new Admin();
        this.instructor = new Instructor();
    }

    //ADDHOMEWORK()
    //"provided this instructor has been assigned to this class"
    @Test
    public void testAddHomeworkValidInstructor() {
        //assign Instructor to Class2017
        this.admin.createClass("Class", 2017, "Instructor", 1);

        //Instructor adds HW to Class2017
        this.instructor.addHomework("Instructor", "Class", 2017, "HW", "Description");

        //HW from Class2017 should exist since Instructor is valid
        assertTrue(this.instructor.homeworkExists("Class", 2017, "HW"));
    }

    @Test
    public void testAddHomeworkInvalidInstructor() {
        //assign Instructor1 to Class2017
        this.admin.createClass("Class", 2017, "Instructor1", 1);

        //Instructor2 adds HW to Class2017
        this.instructor.addHomework("Instructor2", "Class", 2017, "HW", "Description");

        //HW from Class2017 shouldn't exist since Instructor2 doesn't teach Class2017
        assertFalse(this.instructor.homeworkExists("Class", 2017, "HW"));
    }

    //===== ===== ===== ===== ===== ===== ===== ===== ===== =====//

    //ASSIGNGRADE()
    //"provided this instructor has been assigned to this class"
    @Test
    public void testAssignGradeValidInstructor() {
        IStudent student = new Student();

        //assign Instructor to Class2017
        this.admin.createClass("Class", 2017, "Instructor", 1);

        //Student registers for Class2017
        student.registerForClass("Student", "Class", 2017);

        //assign HW from Instructor to Class2017
        this.instructor.addHomework("Instructor", "Class", 2017, "HW", "Description");

        //Student submits HW with Answer to Class2017
        student.submitHomework("Student", "HW", "Answer", "Class", 2017);

        //Instructor assigns grade 100 to Student
        this.instructor.assignGrade("Instructor", "Class", 2017, "HW", "Student", 100);

        //grade should be assigned since Instructor is assigned to class
        assertTrue(this.instructor.getGrade("Class", 2017, "HW", "Student") == 100);
    }

    @Test
    public void testAssignGradeInvalidInstructor() {
        IStudent student = new Student();

        //assign Instructor1 to Class2017
        this.admin.createClass("Class", 2017, "Instructor1", 1);

        //Student registers for Class2017
        student.registerForClass("Student", "Class", 2017);

        //assign HW from Instructor1 to Class2017
        this.instructor.addHomework("Instructor1", "Class", 2017, "HW", "Description");

        //Student submits HW with Answer to Class2017
        student.submitHomework("Student", "HW", "Answer", "Class", 2017);

        //Instructor2 assigns grade 100 to Student
        this.instructor.assignGrade("Instructor2", "Class", 2017, "HW", "Student", 100);

        //grade shouldn't be assigned since Instructor2 is not assigned to class
        assertFalse(this.instructor.getGrade("Class", 2017, "HW", "Student") == 100);
    }

    //"provided the homework has been assigned"
    @Test
    public void testAssignGradeHWAssigned() {
        IStudent student = new Student();

        //assign Instructor to Class2017
        this.admin.createClass("Class", 2017, "Instructor", 1);

        //Student registers for Class2017
        student.registerForClass("Student", "Class", 2017);

        //assign  HW from Instructor to Class2017
        this.instructor.addHomework("Instructor", "Class", 2017, "HW", "Description");

        //Student submits HW with Answer to Class2017
        student.submitHomework("Student", "HW", "Answer", "Class", 2017);

        //Instructor assigns grade 100 to Student
        this.instructor.assignGrade("Instructor", "Class", 2017, "HW", "Student", 100);

        //grade should be successfully assigned since HW was assigned
        assertTrue(this.instructor.getGrade("Class", 2017, "HW", "Student") == 100);
    }

    @Test
    public void testAssignGradeHWNotAssigned() {
        IStudent student = new Student();

        //assign Instructor to Class2017
        this.admin.createClass("Class", 2017, "Instructor", 1);

        //Student registers for Class2017
        student.registerForClass("Student", "Class", 2017);

        //DON'T assign  HW from Instructor to Class2017
        //this.instructor.addHomework("Instructor", "Class", 2017, "HW", "Description");

        //Student submits HW with Answer to Class2017
        student.submitHomework("Student", "HW", "Answer", "Class", 2017);

        //Instructor assigns grade 100 to Student
        this.instructor.assignGrade("Instructor", "Class", 2017, "HW", "Student", 100);

        //grade shouldn't be assigned since HW was never assigned
        assertFalse(this.instructor.getGrade("Class", 2017, "HW", "Student") == 100);
    }

    //"provided student has submitted this homework"
    @Test
    public void testAssignGradeStudentSubmitted() {
        IStudent student = new Student();

        //assign Instructor to Class2017
        this.admin.createClass("Class", 2017, "Instructor", 1);

        //Student registers for Class2017
        student.registerForClass("Student", "Class", 2017);

        //assign  HW from Instructor to Class2017
        this.instructor.addHomework("Instructor", "Class", 2017, "HW", "Description");

        //Student submits HW with Answer to Class2017
        student.submitHomework("Student", "HW", "Answer", "Class", 2017);

        //Instructor assigns grade 100 to Student
        this.instructor.assignGrade("Instructor", "Class", 2017, "HW", "Student", 100);

        //grade should be successfully assigned since HW was submitted by student
        assertTrue(this.instructor.getGrade("Class", 2017, "HW", "Student") == 100);
    }

    @Test
    public void testAssignGradeStudentNotSubmitted() {
        IStudent student = new Student();

        //assign Instructor to Class2017
        this.admin.createClass("Class", 2017, "Instructor", 1);

        //Student registers for Class2017
        student.registerForClass("Student", "Class", 2017);

        //assign  HW from Instructor to Class2017
        this.instructor.addHomework("Instructor", "Class", 2017, "HW", "Description");

        //Student DOESN'T submit HW with Answer to Class2017
        //student.submitHomework("Student", "HW", "Answer", "Class", 2017);

        //Instructor assigns grade 100 to Student
        this.instructor.assignGrade("Instructor", "Class", 2017, "HW", "Student", 100);

        //grade shouldn't be assigned since HW was never submitted by student
        assertFalse(this.instructor.getGrade("Class", 2017, "HW", "Student") == 100);
    }
}