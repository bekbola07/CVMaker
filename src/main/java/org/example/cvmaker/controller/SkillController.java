package org.example.cvmaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.example.cvmaker.entity.Education;
import org.example.cvmaker.entity.Experience;
import org.example.cvmaker.entity.Skill;
import org.example.cvmaker.entity.User;
import org.example.cvmaker.repo.EducationRepository;
import org.example.cvmaker.repo.ExperienceRepository;
import org.example.cvmaker.repo.SkillRepository;
import org.example.cvmaker.repo.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/skills")
public class SkillController {
    private final SkillRepository skillRepository;

    @GetMapping
    public ResponseEntity<List<String>> skills(){
        return new ResponseEntity<>(skillRepository.findAll().stream().map(Skill::getName).toList(),HttpStatus.OK);
    }

}
