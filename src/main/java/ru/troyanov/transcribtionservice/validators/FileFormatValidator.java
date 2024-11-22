package ru.troyanov.transcribtionservice.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileFormatValidator implements ConstraintValidator<ValidFileFormatOrEmpty, MultipartFile> {

    private String[] allowedFormats;

    @Override
    public void initialize(ValidFileFormatOrEmpty constraintAnnotation) {
        this.allowedFormats = constraintAnnotation.allowedFormats();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File is empty.").addConstraintViolation();
            return false;
        }
        String fileName = multipartFile.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File name is empty. File must have an extension.").addConstraintViolation();
            return false;
        }
        String fileExtension = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        if(!Arrays.asList(allowedFormats).contains(fileExtension)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File extension is not supported. Load file in " + String.join(", ", allowedFormats)).addConstraintViolation();
            return false;
        }
        return true;
    }
}
