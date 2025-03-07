package io.github.msj.doccave_notify.service;

import io.github.msj.doccave_notify.dto.NotificationDTO;
import io.github.msj.doccave_notify.dto.ReadingGoalResponseDTO;
import io.github.msj.doccave_notify.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final RestTemplate restTemplate;

    @Value("${doccave.url}")
    private String docCaveApiUrl;

    @Value("${sso.url}")
    private String ssoApiUrl;

    @Value("${client.clientId}")
    private String clientId;

    @Value("${client.clientSecret}")
    private String clientSecret;

    public List<ReadingGoalResponseDTO> findReadingGoals() {
        log.info("Notify DocCave");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + clientId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            Map<String, String> params = new HashMap<>();
            params.put("is_application", "true");
            params.put("client_id", clientId);
            params.put("client_secret", clientSecret);

            ResponseEntity<ReadingGoalResponseDTO[]> response = restTemplate.exchange(
                    docCaveApiUrl + "/reading-goals/not-achieved?is_application={is_application}&client_id={client_id}&client_secret={client_secret}",
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    ReadingGoalResponseDTO[].class,
                    params
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                log.error("Failed to notify DocCave: {}", (Object) response.getBody());
            }

            if (Objects.nonNull(response.getBody())) {
                for (ReadingGoalResponseDTO readingGoalResponseDTO : response.getBody()) {
                    UserResponseDTO userResponseDTO = findUserSSO(readingGoalResponseDTO.getUserId());
                    if (Objects.nonNull(userResponseDTO)) {
                        readingGoalResponseDTO.setUser(userResponseDTO);
                    }
                }
            }

            return List.of(Objects.requireNonNull(response.getBody()));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP Error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (RestClientException e) {
            log.error("Error connecting to the server: {}", e.getMessage());
        }

        return List.of();
    }

    private UserResponseDTO findUserSSO(UUID userId) {
        log.info("Find user SSO {}", userId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + clientId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            Map<String, String> params = new HashMap<>();
            params.put("client_id", clientId);

            ResponseEntity<UserResponseDTO> response = restTemplate.exchange(
                    ssoApiUrl + "/users/" + userId + "?client_id={client_id}",
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    UserResponseDTO.class,
                    params
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                log.error("Failed to notify DocCave: {}", (Object) response.getBody());
            }
            return Objects.nonNull(response.getBody()) ? response.getBody() : null;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP Error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (RestClientException e) {
            log.error("Error connecting to the server: {}", e.getMessage());
        }
        return null;
    }

    public void sendNotification(NotificationDTO message) {
        log.info("📩 Enviando notificação para o usuário {}", message.getUserId());
//        emailService.sendEmail(message.getUserId(), "📢 Notificação de Meta", message.getMessage());
//        whatsAppService.sendWhatsApp(message.getUserId(), message.getMessage());
    }

}
