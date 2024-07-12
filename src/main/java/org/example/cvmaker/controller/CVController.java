package org.example.cvmaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
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
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cv")
public class CVController {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final ObjectMapper objectMapper;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;

    @PostMapping
    public ResponseEntity<byte[]> createCV(@RequestParam("firstname") String firstname,
                                           @RequestParam("lastname") String lastname,
                                           @RequestParam("email") String email,
                                           @RequestParam("photo") MultipartFile photo,
                                           @RequestParam("educations") String educationsJson,
                                           @RequestParam("experiences") String experiencesJson,
//                                           @RequestParam("aboutMe") String aboutMe,
                                           @RequestParam("skills") String skillsJson) throws IOException {

        List<Education> educations = objectMapper.readValue(educationsJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Education.class));
        educationRepository.saveAll(educations);

        List<Experience> experiences = objectMapper.readValue(experiencesJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Experience.class));
        experienceRepository.saveAll(experiences);

        List<String> skillNames = objectMapper.readValue(skillsJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));

        List<Skill> skills = skillNames.stream().map(name -> skillRepository.findByName(name).orElseGet(() -> {
            Skill newSkill = new Skill();
            newSkill.setName(name);
            return skillRepository.save(newSkill);
        })).collect(Collectors.toList());

        User user = User.builder()
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
//                .aboutMe(aboutMe)
                .photo(photo.getBytes())
                .educations(educations)
                .experience(experiences)
                .skills(skills)
                .build();

        userRepository.save(user);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        // Set fonts and colors
        BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
        Font titleFont = new Font(baseFont, 18, Font.BOLD, Color.BLUE);
        Font headerFont = new Font(baseFont, 14, Font.BOLD, Color.DARK_GRAY);
        Font normalFont = new Font(baseFont, 12, Font.NORMAL, Color.BLACK);

        // Add title
        Paragraph title = new Paragraph("Curriculum Vitae", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n"));

        // Add user details
        PdfPTable userTable = new PdfPTable(2);
        userTable.setWidthPercentage(100);

        PdfPCell userDetailsCell = new PdfPCell();
        userDetailsCell.setBorder(Rectangle.NO_BORDER);
        userDetailsCell.addElement(new Paragraph("First Name: " + user.getFirstname(), normalFont));
        userDetailsCell.addElement(new Paragraph("Last Name: " + user.getLastname(), normalFont));
        userDetailsCell.addElement(new Paragraph("Email: " + user.getEmail(), normalFont));

        PdfPCell photoCell = new PdfPCell();
        photoCell.setBorder(Rectangle.NO_BORDER);
        if (user.getPhoto() != null && user.getPhoto().length > 0) {
            Image img = Image.getInstance(user.getPhoto());
            img.scaleToFit(150, 150);
            img.setAlignment(Element.ALIGN_RIGHT);
            img.setBorder(Image.BOX);
            img.setBorderWidth(10);
            img.setBorderColor(Color.WHITE);
            photoCell.addElement(img);
        }
        userTable.addCell(userDetailsCell);
        userTable.addCell(photoCell);
        document.add(userTable);
        document.add(new Paragraph("\n"));

        // Add educations
        document.add(new Paragraph("Education", headerFont));
        for (Education education : educations) {
            document.add(new Paragraph(" - " + education.getWhere() + " (" + education.getFrom() + " to " + education.getTo() + ")", normalFont));
        }
        document.add(new Paragraph("\n"));

        // Add experiences
        document.add(new Paragraph("Experience", headerFont));
        for (Experience experience : experiences) {
            document.add(new Paragraph(" - " + experience.getWhere() + " (" + experience.getFrom() + " to " + experience.getTo() + ")", normalFont));
        }
        document.add(new Paragraph("\n"));

        // Add skills
        document.add(new Paragraph("Skills", headerFont));
        for (Skill skill : skills) {
            document.add(new Paragraph(" - " + skill.getName(), normalFont));
        }

//        document.add(new Paragraph("About me",headerFont));
//        document.add(new Paragraph(aboutMe,normalFont));

        document.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=cv.pdf");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

    }

}
