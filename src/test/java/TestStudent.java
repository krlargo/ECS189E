import api.*;
import api.core.impl.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestStudent {
    private IAdmin admin;
    private IStudent student;

    @Before
    public void setup() {
        this.admin = new Admin();
        this.student = new Student();
    }

    /* == REGISTERFORCLASS =========================
        Conditions:
        - class exists
        - class has not met its enrollment capacity
    */

    @Test
    public void testClassExistsAndNotMetCapacity() {
        //create Class2017 with capacity 1
        this.admin.createClass("Class", 2017, "Instructor", 1);

        //Student registers for class
        this.student.registerForClass("Student", "Class", 2017);

        //should be able to register fine since Class exists and within capacity
        assertTrue(this.student.isRegisteredFor("Student", "Class", 2017));
    }

    @Test
    public void testClassDoesntExist() {
        //DON'T create Class2017
        //this.admin.createClass("Class", 2017, "Instructor", 1);

        //Student registers for class
        this.student.registerForClass("Student", "Class", 2017);

        //shouldn't be able to register since class doesn't exist
        assertFalse(this.student.isRegisteredFor("Student", "Class", 2017));
    }

    @Test
    public void testClassMetCapacity() {
        //create Class2017 with capacity 1
        this.admin.createClass("Class", 2017, "Instructor", 1);

        //register two Students to class
        student.registerForClass("Student1", "Class", 2017);
        student.registerForClass("Student2", "Class", 2017);

        //Student2 shouldn't be able to register since capacity has been reached
        assertFalse(this.student.isRegisteredFor("Student2", "Class", 2017));
    }

    /* == DROPCLASS =========================
        Conditions:
        - student is registered //redundant test
        - class has not ended
    */
    @Test
    public void testStudentRegisteredAndClassNotEnded() {
        //create Class2018
        this.admin.createClass("Class", 2018, "Instructor", 1);

        //register Student to Class2018
        this.student.registerForClass("Student", "Class", 2018);

        //drop Student from Class2018
        this.student.dropClass("Student", "Class", 2018);

        //Student should be able to drop Class2018 and should no longer be registered
        assertTrue(!this.student.isRegisteredFor("Student", "Class", 2018));
    }

    @Test
    public void testClassHasEnded() {
        //create a Class that has already ended in 2016
        this.admin.createClass("Class", 2016, "Instructor", 1);

        //register Student for Class2016
        this.student.registerForClass("Student", "Class", 2016);

        //Student attempts to drop Class2016
        this.student.dropClass("Student", "Class", 2016);

        //Student shouldn't be able to drop Class2016 since it's ended already;
        // -> Student should still be registered
        assertTrue(this.student.isRegisteredFor("Student", "Class", 2016));
    }

    /*SUBMITHOMEWORK
        Conditions:
        - homework exists
        - student is registered
        - class is taught in the current year
    */

    @Test
    public void testHwExistsStudentRegisteredClassCurrent() {
        IInstructor instructor = new Instructor();

        //class is taught in the current year
        this.admin.createClass("Class", 2017, "Instructor", 1);

        //student is registered
        this.student.registerForClass("Student", "Class", 2016);

        //HW exists
        instructor.addHomework("Instructor", "Class", 2017, "HW", "Description");

        //Student attempts to submit HW to Class2017
        this.student.submitHomework("Student", "HW", "Answer", "Class", 2017);

        //should succeed
        assertTrue(this.student.hasSubmitted("Student", "HW", "Class", 2017));
    }

    @Test
    public void testHwDoesntExist() {
        IInstructor instructor = new Instructor();

        //class is taught in the current year
        this.admin.createClass("Class", 2017, "Instructor", 1);

        //student is registered
        this.student.registerForClass("Student", "Class", 2017);

        //HW DOESN'T exist
        //instructor.addHomework("Instructor", "Class", 2017, "HW", "Description");

        //Student attempts to submit HW to Class2017
        this.student.submitHomework("Student", "HW", "Answer", "Class", 2017);

        //should succeed
        assertFalse(this.student.hasSubmitted("Student", "HW", "Class", 2017));
    }

    @Test
    public void testStudentNotRegistered() {
        IInstructor instructor = new Instructor();

        //class is taught in the current year
        this.admin.createClass("Class", 2017, "Instructor", 1);

        //student is NOT registered
        //this.student.registerForClass("Student", "Class", 2017);

        //HW exists
        instructor.addHomework("Instructor", "Class", 2017, "HW", "Description");

        //Student attempts to submit HW to Class2017
        this.student.submitHomework("Student", "HW", "Answer", "Class", 2017);

        //should succeed
        assertFalse(this.student.hasSubmitted("Student", "HW", "Class", 2017));
    }

    @Test
    public void testClassNotCurrentYear() {
        IInstructor instructor = new Instructor();

        //class is taught in the current year
        this.admin.createClass("Class", 2018, "Instructor", 1);

        //student is registered
        this.student.registerForClass("Student", "Class", 2018);

        //HW exists
        instructor.addHomework("Instructor", "Class", 2018, "HW", "Description");

        //Student attempts to submit HW to Class2017
        this.student.submitHomework("Student", "HW", "Answer", "Class", 2018);

        //should succeed
        assertFalse(this.student.hasSubmitted("Student", "HW", "Class", 2018));
    }
}
