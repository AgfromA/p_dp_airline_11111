package app.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EmailControllerIT extends IntegrationTestBase {

    // FIXME Тест (как и сама логика) не будет работать без логина и пароля для почты в пропертях
    // @Test
    @DisplayName("Успешная отправка письма")
    void whenSendEmail_thenReturnSuccessMessage() throws Exception {
        String testEmail = "test@mail.ru";
        mockMvc.perform(get("/email/" + testEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Please check your inbox"));
    }

    @Test
    @DisplayName("Ввод невалидного адреса электронной почты")
    void whenSendEmail_withInvalidEmail_thenReturnErrorMessage() throws Exception {
        String invalidEmail = "invalid_email";
        mockMvc.perform(get("/email/" + invalidEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unable to send email"));
    }
}