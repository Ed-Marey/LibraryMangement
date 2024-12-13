package com.library.lms.service;

import com.library.lms.entity.Member;
import com.library.lms.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Optional<Member> getMemberById(Integer id) {
        // Validate ID before fetching
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return memberRepository.findById(id);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member saveMember(Member member) {
        // Example logic: Validate member details
        if (member == null || member.getName() == null || member.getEmail() == null) {
            throw new IllegalArgumentException("Invalid member details");
        }
        return memberRepository.save(member);
    }

    public void deleteMemberById(Integer id) {
        if (!memberRepository.existsById(id)) {
            throw new IllegalArgumentException("Member record not found");
        }
        memberRepository.deleteById(id);
    }
}

// Staff Service
package com.library.lms.service;

import com.library.lms.entity.Staff;
import com.library.lms.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    public Optional<Staff> getStaffById(Integer id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return staffRepository.findById(id);
    }

    public Staff findStaffByMemberId(Integer memberId) {
        if (memberId <= 0) {
            throw new IllegalArgumentException("Member ID must be greater than 0");
        }
        return staffRepository.findMemberId(memberId);
    }
}

// Students Service
package com.library.lms.service;

import com.library.lms.entity.Students;
import com.library.lms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Optional<Students> getStudentById(Integer id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return studentRepository.findById(id);
    }

    public Students findStudentByMemberId(Integer memberId) {
        if (memberId <= 0) {
            throw new IllegalArgumentException("Member ID must be greater than 0");
        }
        return studentRepository.findByMemberId(memberId);
    }

    public List<Students> getAllStudents() {
        return studentRepository.findAll();
    }

    public Students saveStudent(Students student) {
        if (student == null || student.getMember() == null) {
            throw new IllegalArgumentException("Invalid student details");
        }
        return studentRepository.save(student);
    }
}

// Controller
package com.library.lms.controller;

import com.library.lms.entity.Member;
import com.library.lms.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable Integer id) {
        try {
            Optional<Member> member = memberService.getMemberById(id);
            if (member.isPresent()) {
                return ResponseEntity.ok(member.get());
            } else {
                return ResponseEntity.status(404).body("Member record not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody Member member) {
        try {
            Member savedMember = memberService.saveMember(member);
            return ResponseEntity.ok(savedMember);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMemberById(@PathVariable Integer id) {
        try {
            memberService.deleteMemberById(id);
            return ResponseEntity.ok("Member record deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

package com.library.lms.controller;

import com.library.lms.entity.Staff;
import com.library.lms.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staffs")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getStaffById(@PathVariable Integer id) {
        try {
            Optional<Staff> staff = staffService.getStaffById(id);
            if (staff.isPresent()) {
                return ResponseEntity.ok(staff.get());
            } else {
                return ResponseEntity.status(404).body("Staff record not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getStaffByMemberId(@PathVariable Integer memberId) {
        try {
            Staff staff = staffService.findStaffByMemberId(memberId);
            return ResponseEntity.ok(staff);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

package com.library.lms.controller;

import com.library.lms.entity.Students;
import com.library.lms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Integer id) {
        try {
            Optional<Students> student = studentService.getStudentById(id);
            if (student.isPresent()) {
                return ResponseEntity.ok(student.get());
            } else {
                return ResponseEntity.status(404).body("Student record not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getStudentByMemberId(@PathVariable Integer memberId) {
        try {
            Students student = studentService.findStudentByMemberId(memberId);
            return ResponseEntity.ok(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Students>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Students student) {
        try {
            Students savedStudent = studentService.saveStudent(student);
            return ResponseEntity.ok(savedStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
