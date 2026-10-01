package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.TeacherMapper;
import mthree.com.fullstackschool.model.Teacher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TeacherDaoImpl implements TeacherDao {

    private final JdbcTemplate jdbcTemplate;

    public TeacherDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Teacher createNewTeacher(Teacher teacher) {
        //YOUR CODE STARTS HERE

        final String CREATE_NEW_TEACHER = "INSERT INTO teacher (tFName, tLName, dept) VALUES (?, ?, ?)";

        jdbcTemplate.update(CREATE_NEW_TEACHER, teacher.getTeacherFName(), teacher.getTeacherLName(), teacher.getDept());

        return teacher;
        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Teacher> getAllTeachers() {
        //YOUR CODE STARTS HERE

        final String GET_ALL_TEACHERS = "SELECT * FROM teacher";

        return jdbcTemplate.query(GET_ALL_TEACHERS, new TeacherMapper());

        //YOUR CODE ENDS HERE
    }

    @Override
    public Teacher findTeacherById(int id) {
        //YOUR CODE STARTS HERE

        final String FIND_TEACHER_BY_ID = "SELECT * FROM teacher WHERE tid = ?";

        return jdbcTemplate.queryForObject(FIND_TEACHER_BY_ID, new TeacherMapper(), id);

        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateTeacher(Teacher t) {
        //YOUR CODE STARTS HERE

        //        final String UPDATE_STUDENT = "UPDATE student SET fName = ?, lName = ? WHERE sid = ?";
        //
        //        jdbcTemplate.update(UPDATE_STUDENT, student.getStudentFirstName(), student.getStudentLastName(), student.getStudentId());

        final String UPDATE_TEACHER = "UPDATE teacher SET tFName = ?, tLName = ?, dept=? WHERE tid = ?";

        jdbcTemplate.update(UPDATE_TEACHER, t.getTeacherFName(), t.getTeacherLName(), t.getDept(), t.getTeacherId());

        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteTeacher(int id) {
        //YOUR CODE STARTS HERE

        final String DELETE_TEACHER = "DELETE FROM teacher WHERE tid = ?";

        jdbcTemplate.update(DELETE_TEACHER, id);

        //YOUR CODE ENDS HERE
    }
}
