package com.example.demo.services.impl;

import com.example.demo.Enum.Role;
import com.example.demo.Enum.StatutValidation;
import com.example.demo.entities.Roles;
import com.example.demo.entities.Utilisateur;
import com.example.demo.repository.IUtilisateurRepository;
import com.example.demo.repository.RolesRepository;
import com.example.demo.services.INotificationService;
import com.example.demo.services.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements IUtilisateurService {

    private final IUtilisateurRepository utilisateurRepository;

    private final PasswordEncoder passwordEncoder;

    private final RolesRepository rolesRepository;

    private final MailService mailService;




    @Override
    public Utilisateur addUtilisateur(Utilisateur utilisateur) {
        String encodedPassword = passwordEncoder.encode(utilisateur.getPassword());
        utilisateur.setPassword(encodedPassword);
        Set<Roles> roles = new HashSet<>();

        Roles devRole = rolesRepository.findByName(Role.CLIENT).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(devRole);
        utilisateur.setRoles(roles);

        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur getUtilisateurById(int id) {
        return utilisateurRepository.findById(id).get();
    }

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return (List<Utilisateur>) utilisateurRepository.findAll();
    }

    @Override
    public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
        Utilisateur utilisateur1 = utilisateurRepository.findById(utilisateur.getId()).get();
        utilisateur.setRoles(utilisateur1.getRoles());
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public void deleteUtilisateur(int id) {
        utilisateurRepository.deleteById(id);

    }

    @Override
    public Utilisateur findByNomAndPrenom(String nom, String prenom) {
        return utilisateurRepository.findByNomAndPrenom(nom, prenom);
    }



/*    @Override
    public Utilisateur findByRole(Role role) {
        return utilisateurRepository.findByRole(role);
    }*/

    

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }



    @Override
    public Utilisateur findUtilisateurByTelephone(String telephone) {
        return utilisateurRepository.findUtilisateurByTelephone(telephone);
    }

    @Override
    public List<Utilisateur> findExpertsByRoleAndStatut() {
        return utilisateurRepository.findExpertsByRoleAndStatut(Role.EXPERT, StatutValidation.EN_ATTENTE);
    }

    @Override
    public void validerExpert(int id) {
        Utilisateur expert = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expert non trouvé"));

        expert.setStatutValidation(StatutValidation.ACCEPTE);
        expert.setIsActive(true);
        utilisateurRepository.save(expert);

        mailService.envoyerEmail(
                expert.getEmail(),
                "Validation de votre compte",
                "Bonjour " + expert.getPrenom() + ",\n\n" +
                        "Votre compte expert a été validé par l'administrateur. " +
                        "Vous pouvez maintenant vous connecter à notre plateforme EXPERTING."
        );

    }

    @Override
    public void refuserExpert(int id) {
        Utilisateur expert = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expert non trouvé"));

        expert.setStatutValidation(StatutValidation.REFUSE);
        utilisateurRepository.save(expert);

        mailService.envoyerEmail(
                expert.getEmail(),
                "Refus de votre compte",
                "Bonjour " + expert.getPrenom() + ",\n\nNous sommes désolés, mais votre compte expert a été refusé par l'administrateur."
        );
    }

    @Override
    public List<Utilisateur> findByRoles_NameClient() {
        return utilisateurRepository.findByRoles_Name(Role.CLIENT);
    }

    @Override
    public void deleteClient(int id) {
        Utilisateur client = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé " ));

        utilisateurRepository.delete(client);
    }

    @Override
    public List<Utilisateur> findByRoles_NameExpert() {
        return utilisateurRepository.findByStatutValidation(StatutValidation.ACCEPTE);
    }

    @Override
    public long countUtilisateurs() {

        return utilisateurRepository.count();
    }

    @Override
    public long countUtilisateurByRoles_Name() {
        return utilisateurRepository.countUtilisateurByRoles_Name(Role.CLIENT);
    }

    @Override
    public long countUtilisateurByStatutValidation() {
        return utilisateurRepository.countUtilisateurByStatutValidation(StatutValidation.ACCEPTE);
    }

    @Override
    public long countUtilisateurByStatutValidationn() {
        return utilisateurRepository.countUtilisateurByStatutValidation(StatutValidation.EN_ATTENTE);
    }

    @Override
    public long countUtilisateurByStatutRefuse() {
        return utilisateurRepository.countUtilisateurByStatutValidation(StatutValidation.REFUSE);
    }

    @Override
    public Utilisateur getCurrentUtilisateur() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
    }

    @Override
    public boolean isExpert(Utilisateur utilisateur) {
        return utilisateur.getRoles().stream()
                .anyMatch(role -> role.getName().name().equals("EXPERT"));
    }

    @Override
    public boolean hasRole(Utilisateur utilisateur, Role role) {
        return utilisateur.getRoles().stream()
                .anyMatch(r -> r.getName().equals(role));
    }

    @Override
    public List<Utilisateur> findByDomaineExpertiseAndStatutValidation(String domaineExpertise) {
        return utilisateurRepository.findByDomaineExpertiseIgnoreCaseAndStatutValidation(domaineExpertise,StatutValidation.ACCEPTE);
    }

    @Override
    public List<String> findDistinctDomaineExpertiseAndStatutValidation() {
        return utilisateurRepository.findDistinctDomainesExpertiseValides(StatutValidation.ACCEPTE);
    }

    @Override
    public Double calculerMoyenneTarifExpertsValides() {
        List<Utilisateur> experts = utilisateurRepository.findExpertsByRoleAndStatut(Role.EXPERT, StatutValidation.ACCEPTE);
        double somme = 0;
        int count = 0;

        for (Utilisateur expert : experts) {
            String tarifStr = expert.getTarif();
            if (tarifStr != null && !tarifStr.isEmpty()) {
                try {
                    double tarif = Double.parseDouble(tarifStr);
                    somme += tarif;
                    count++;
                } catch (NumberFormatException e) {
                    // Ignorer les valeurs non convertibles
                }
            }
        }

        return count > 0 ? somme / count : null;

    }

    @Override
    public Double calculerMoyenneExperienceExpertsValides() {
        List<Utilisateur> experts = utilisateurRepository.findExpertsByRoleAndStatut(Role.EXPERT, StatutValidation.ACCEPTE);
        double somme = 0;
        int count = 0;

        for (Utilisateur expert : experts) {
            String expStr = expert.getAnneesExperience();
            if (expStr != null && !expStr.isEmpty()) {
                try {
                    double experience = Double.parseDouble(expStr);
                    somme += experience;
                    count++;
                } catch (NumberFormatException e) {
                    // Ignorer les valeurs non convertibles
                }
            }
        }

        return count > 0 ? somme / count : null;
    }


}



