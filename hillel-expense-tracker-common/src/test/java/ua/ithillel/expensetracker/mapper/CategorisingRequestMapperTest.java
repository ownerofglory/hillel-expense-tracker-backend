package ua.ithillel.expensetracker.mapper;

import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.dto.CategorisingRequestDTO;
import ua.ithillel.expensetracker.model.CategorisingRequest;

import static org.junit.jupiter.api.Assertions.*;

class CategorisingRequestMapperTest {

    @Test
    void requestToDto() {
        CategorisingRequest testRequest = new CategorisingRequest("I went to starbucks, got a coffee and payed 5 bucks");

        CategorisingRequestDTO requestDTO = CategorisingRequestMapper.INSTANCE.requestToDto(testRequest);

        assertNotNull(requestDTO);
        assertEquals(testRequest.getPrompt(), requestDTO.getPrompt());
    }

    @Test
    void dtoToRequest() {
        CategorisingRequestDTO testRequestDTO = new CategorisingRequestDTO("I went to starbucks, got a coffee and payed 5 bucks");

        CategorisingRequest request = CategorisingRequestMapper.INSTANCE.dtoToRequest(testRequestDTO);

        assertNotNull(request);
        assertEquals(testRequestDTO.getPrompt(), request.getPrompt());
    }
}