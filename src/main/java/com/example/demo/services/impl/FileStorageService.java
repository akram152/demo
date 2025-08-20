package com.example.demo.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    public FileStorageService() {
        // Définit le dossier où les fichiers seront stockés
        this.fileStorageLocation = Paths.get("uploads")
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Impossible de créer le dossier de stockage.", ex);
        }
    }

    public String storeFile(MultipartFile file, String type) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new RuntimeException("Nom de fichier invalide " + fileName);
            }

            // Vérifier le type MIME selon le type attendu
            String contentType = file.getContentType();

            if(type.equals("image")) {
                if(contentType == null || !contentType.startsWith("image/")) {
                    throw new RuntimeException("Le fichier doit être une image.");
                }
            } else if(type.equals("pdf")) {
                if(contentType == null || !contentType.equals("application/pdf")) {
                    throw new RuntimeException("Le fichier doit être un PDF.");
                }
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException ex) {
            throw new RuntimeException("Erreur lors du stockage du fichier " + fileName, ex);
        }
    }

    public String storePartageFile(MultipartFile file, long consultationId){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Pas de validation du type ici → on autorise tout

        try {
            Path targetDir = this.fileStorageLocation.resolve("consult_" + consultationId);
            Files.createDirectories(targetDir);

            Path targetLocation = targetDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "consult_" + consultationId + "/" + fileName;

        } catch (IOException ex) {
            throw new RuntimeException("Erreur lors du stockage du fichier " + fileName, ex);
        }
}
}
