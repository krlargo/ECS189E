import api.*;
import api.core.impl.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAdmin {
    private IAdmin admin;

    @Before
    public void setup() {
        this.admin = new Admin();
    }

    //CREATECLASS()
    // "className/year pair must be unique"
    @Test
    public void testSameClassNameSameYear() { ///?
        //make original Class, 2017 with capacity 1
        this.admin.createClass("Class", 2017, "Instructor1", 1);

        //make second Class, 2017 with capcity 2
        this.admin.createClass("Class", 2017, "Instructor2", 2);

        //if capacity == 2 (the second Class, 2017), then this test should fail;
        // second Class, 2017 should have never been created
        assertFalse(this.admin.getClassCapacity("Class", 2017) == 2);
    }

    @Test
    public void testDiffClassNameSameYear() {
        this.admin.createClass("Class1", 2017, "Instructor1", 1);
        this.admin.createClass("Class2", 2017, "Instructor2", 2);
        assertTrue(this.admin.classExists("Class1", 2017));
    }

    @Test
    public void testSameClassNameDiffYear() {
        this.admin.createClass("Class", 2017, "Instructor1", 1);
        this.admin.createClass("Class", 2018, "Insctuctor2", 2);
        assertTrue(this.admin.classExists("Class", 2018));
    }

    @Test
    public void testDiffClassNameDiffYear() {
        this.admin.createClass("Class1", 2017, "Instructor1", 1);
        this.admin.createClass("Class2", 2018, "Instructor2", 2);
        assertTrue(this.admin.classExists("Class2", 2018));
    }

    // "no instructor can be assigned to more than two courses in a year"
    @Test
    public void testOneCourse() {
        this.admin.createClass("Class", 2017, "Instructor", 1);
        assertTrue(this.admin.classExists("Class", 2017));
    }

    @Test
    public void testTwoCourses() {
        this.admin.createClass("Class1", 2017, "Instructor1", 1);
        this.admin.createClass("Class2", 2018, "Instructor2", 2);
        assertTrue(this.admin.classExists("Class2", 2018));
    }

    @Test
    public void testMoreThanTwoCourses() {
        this.admin.createClass("Class1", 2017, "Instructor1", 1);
        this.admin.createClass("Class2", 2017, "Instructor2", 2);
        this.admin.createClass("Class3", 2017, "Instructor3", 3);

        //third class should not have been allowed
        assertFalse(this.admin.classExists("Class3", 2017));
    }

    // "Calendar year in which the course is to be taught, cannot be in the past"
    @Test
    public void testValidYear() {
        this.admin.createClass("Class", 2017,"Instructor", 15);
        assertTrue(this.admin.classExists("Class", 2017));
    }

    @Test
    public void testInvalidYear() {
        this.admin.createClass("Class", 2016,"Instructor", 15);

        //class of 2016 should not be allowed
        assertFalse(this.admin.classExists("Class", 2016));
    }

    // "Maximum capacity of this class > 0"
    @Test
    public void testPositiveCapacity() {
        this.admin.createClass("Class", 2017, "Instructor", 1);
        assertTrue(this.admin.classExists("Class", 2017));
    }

    @Test
    public void testZeroCapacity() {
        this.admin.createClass("Class", 2017, "Instructor", 0);

        //class with 0 capacity should not be allowed
        assertFalse(this.admin.classExists("Class", 2017));
    }

    @Test
    public void testNegativeCapacity() {
        this.admin.createClass("Class", 2017, "Instructor", -1);
        assertFalse(this.admin.classExists("Class", 2017));
    }

    //===== ===== ===== ===== ===== ===== ===== ===== ===== =====//

    //CHANGECAPACITY()
    @Test public void testLessCapacityThanEnrolled() {
        //initial capacity is 2
        this.admin.createClass("Class", 2017, "Instructor", 2);

        //enroll 2 students
        IStudent student1 = new Student();
        IStudent student2 = new Student();
        student1.registerForClass("Student1", "Class", 2017);
        student2.registerForClass("Student2", "Class", 2017);
        int enrolled = 2;

        //change capacity to 1
        int newCapacity = 1;
        this.admin.changeCapacity("Class", 2017, newCapacity);

        //capacity count cannot be less than enroll
        assertFalse(this.admin.getClassCapacity("Class", 2017) < enrolled);
    }

    @Test public void testEqualCapacityToEnrolled() {
        //initial capacity is 3
        this.admin.createClass("Class", 2017, "Instructor", 3);

        //enroll 2 students
        IStudent student1 = new Student();
        IStudent student2 = new Student();
        student1.registerForClass("Student1", "Class", 2017);
        student2.registerForClass("Student2", "Class", 2017);
        int enrolled = 2;

        //change capacity to 2
        int newCapacity = 2;
        this.admin.changeCapacity("Class", 2017, newCapacity);

        //true if
        assertTrue(this.admin.getClassCapacity("Class", 2017) >= enrolled);
    }

    @Test public void testMoreCapacityThanEnrolled() {
        //initial capacity is 1
        this.admin.createClass("Class", 2017, "Instructor", 1);

        //enroll 1 student
        IStudent student = new Student();
        student.registerForClass("Student", "Class", 2017);
        int enrolled = 1;

        //change capacity to 2
        int newCapacity = 2;
        this.admin.changeCapacity("Class", 2017, newCapacity);
        assertTrue(this.admin.getClassCapacity("Class", 2017) >= enrolled);
    }
}
